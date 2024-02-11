package cd.bnb.syprome.controllers;


import cd.bnb.syprome.daos.DueDiligenceDao;
import cd.bnb.syprome.daos.FournisseurDao;
import cd.bnb.syprome.daos.PartenariatDao;
import cd.bnb.syprome.daos.UsersDao;
import cd.bnb.syprome.entities.DueDiligence;
import cd.bnb.syprome.entities.Fournisseur;
import cd.bnb.syprome.entities.Users;
import cd.bnb.syprome.requests.DalValidRequest;
import cd.bnb.syprome.requests.TreatRequest;
import cd.bnb.syprome.responses.HttpBodyResponse;
import cd.bnb.syprome.services.FilesStorageService;
import cd.bnb.syprome.services.NotificationService;
import cd.bnb.syprome.utilities.AppInfo;
import cd.bnb.syprome.utilities.MailUtils;
import cd.bnb.syprome.utilities.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;



@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/due-diligence")
public class DueDiligenceController {


    private DueDiligenceDao duediligenecDao;
    private FilesStorageService storageService;
    private PartenariatDao partenariatDao;
    private NotificationService notificationService;
    private UsersDao usersDao;
    private AppInfo appInfo;
    private FournisseurDao fournisseurDao;
    @GetMapping("/list/{id}")
    public HttpBodyResponse findAllBY(@PathVariable Integer id){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
                bodyResponse.setResponse(duediligenecDao.findAllByDemandeurIdOrderByIdDesc(id));
        }catch (Exception e){
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @GetMapping("/notif/{email}")
    public HttpBodyResponse getNotif(@PathVariable String  email){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            Fournisseur f=fournisseurDao.findByEmail(email);
            MailUtils m = new MailUtils();
            m.setFrom("orange.portail.fournisseur@orange.com");
            m.setHtml(Utils.formatHtmlNotif(f.getNoms()));
            m.setSubject("Demande de Partenariat");
            m.setTo(Arrays.asList(email));
            notificationService.sendEmail(m);
            bodyResponse.setResponse(true);
        }catch (Exception e){
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @GetMapping("/list/dal")
    public HttpBodyResponse findAllBAL(){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(duediligenecDao.findAllByOrigineOrderByIdDesc("DAL"));
        }catch (Exception e){
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @GetMapping("/{id}")
    public HttpBodyResponse findAll(@PathVariable Integer id){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            Users u=usersDao.findById(id).get();
            if(u.getRole().getDescription().equalsIgnoreCase("fic"))
                bodyResponse.setResponse(duediligenecDao.findAllByFicOrderByIdDesc(true));
            else if (u.getRole().getDescription().equalsIgnoreCase("om")) {
                bodyResponse.setResponse(duediligenecDao.findAllByOmOrderByIdDesc(true));
            }
            else if (u.getRole().getDescription().equalsIgnoreCase("djr")||u.getRole().getDescription().equalsIgnoreCase("cco")) {
                bodyResponse.setResponse(duediligenecDao.findAll(Sort.by(Sort.Direction.DESC, "id")));
            }else
            bodyResponse.setResponse(duediligenecDao.findAllByDemandeurIdOrderByIdDesc(id));
        }catch (Exception e){
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @GetMapping("/fic")
    public HttpBodyResponse findAllFic(){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{

            bodyResponse.setResponse(duediligenecDao.findAllByFicOrderByIdDesc(true));
        }catch (Exception e){
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @GetMapping("/om")
    public HttpBodyResponse findAllOm(){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{

            bodyResponse.setResponse(duediligenecDao.findAllByOmOrderByIdDesc(true));
        }catch (Exception e){
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @PostMapping("/dal")
    public HttpBodyResponse valDal(@RequestBody DalValidRequest request){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            log.info("{}",request);
            DueDiligence d=duediligenecDao.findById(request.getIdDue()).get();
            Users u=usersDao.findById(request.getIdUser()).get();
            d.setDemandeur(u);
            d.setOrigine("DAL");
            d.setEntite(u.getNoms());
            if(request.getUrgent()==1) {
                d.setUrgent(true);
            }
            log.info("{}",d);
            duediligenecDao.save(d);
            bodyResponse.setResponse(duediligenecDao.findAllByOmOrderByIdDesc(true));
        }catch (Exception e){
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @PostMapping("/valide")
    public HttpBodyResponse findOne(@RequestBody TreatRequest request){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            log.info(" request {} ",request);
            DueDiligence dueDiligence= duediligenecDao.findById(request.getIdDue()).get();
            Users us=usersDao.findById(request.getIdUser()).get();
            dueDiligence.setDateCloture(new Date());
            dueDiligence.setJuriste(us);
            dueDiligence.setCommentaire(request.getCommentaire());
            dueDiligence.setStep(2);
            duediligenecDao.save(dueDiligence);
            if(dueDiligence.getOm()||dueDiligence.getFic()) {
                for (Users u : usersDao.findAll()) {

                    String test = "Bonjour " + u.getNoms() + ", La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire  est: " + dueDiligence.getObjet() + " est soumise ce "+ new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande()) +" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + ". Merci due diligence app";
                    String html = "<h4>Bonjour " + u.getNoms() + ", <br/> La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire est : " + dueDiligence.getObjet() + " est soumise ce "+new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande())+" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour  un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + "<br/> .  Merci due diligence app</h4>";
                    MailUtils m = new MailUtils();
                    m.setFrom("duediligence@orange.com");
                    m.setText(test);
                    m.setHtml(html);
                    m.setSubject("Demande de due diligence ");
                    m.setTo(Arrays.asList(u.getEmail()));
                    if (dueDiligence.getFic() && u.getRole().getDescription().equalsIgnoreCase("fic")) {
                        notificationService.sendEmail(m);
                    } else if (dueDiligence.getOm() && u.getRole().getDescription().equalsIgnoreCase("om")) {
                        notificationService.sendEmail(m);
                    }

                }
            }else{
                Users u=dueDiligence.getDemandeur();
                String test = "Bonjour "+ dueDiligence.getDemandeur().getNoms()+",\r\n" +
                        "\r\n" +
                        "Après analyse, la due diligence  N°"+dueDiligence.getMo()+" de +"+dueDiligence.getObjet()+"+ est conforme.\r\n" +
                        "\r" +
                        "Nous pouvons donc évoluer pour la suite.\r\n" +
                        "\r\n" +
                        "Cordialement due diligence\r\n";
                String html = "<p>"+"Bonjour "+ dueDiligence.getDemandeur().getNoms()+",\r\n" +
                        "<br/><br/>" +
                        "Après analyse, la due diligence N°"+dueDiligence.getMo()+" de "+dueDiligence.getObjet()+" est conforme.\r\n" +
                        "<br/>" +
                        "Nous pouvons donc évoluer pour la suite.\r\n" +
                        "<br/><br/>" +
                        "Cordialement Due diligence<br/>"+"</p>";
                MailUtils m = new MailUtils();
                m.setFrom("duediligence@orange.com");
                m.setText(test);
                m.setHtml(html);
                m.setSubject("Demande de due diligence ");
                m.setTo(Arrays.asList(u.getEmail()));
                notificationService.sendEmail(m);
            }
        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @PostMapping("/valide/fic")
    public HttpBodyResponse fic(@RequestBody TreatRequest request){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            log.info(" request fic {} ",request);
            DueDiligence dueDiligence= duediligenecDao.findById(request.getIdDue()).get();
            Users u=usersDao.findById(request.getIdUser()).get();
            dueDiligence.setDateCloture2(new Date());
            dueDiligence.setValidateur(u);
            dueDiligence.setCommentaire2(request.getCommentaire());
            dueDiligence.setStep(4);
            duediligenecDao.save(dueDiligence);
           u=dueDiligence.getDemandeur();
            String test = "Bonjour "+ dueDiligence.getDemandeur().getNoms()+",\r\n" +
                    "\r\n" +
                    "Après analyse, la due diligence  N°"+dueDiligence.getMo()+" de +"+dueDiligence.getObjet()+"+ est conforme.\r\n" +
                    "\r" +
                    "Nous pouvons donc évoluer pour la suite.\r\n" +
                    "\r\n" +
                    "Cordialement Due diligence\r\n";
            String html = "<p>"+"Bonjour "+ dueDiligence.getDemandeur().getNoms()+",\r\n" +
                    "<br/><br/>" +
                    "Après analyse, la due diligence N°"+dueDiligence.getMo()+" de "+dueDiligence.getObjet()+" est conforme.\r\n" +
                    "<br/>" +
                    "Nous pouvons donc évoluer pour la suite.\r\n" +
                    "<br/><br/>" +
                    "Cordialement Due diligence<br/>"+"</p>";
           MailUtils m = new MailUtils();
            m.setFrom("duediligence@orange.com");
            m.setText(test);
            m.setHtml(html);
            m.setSubject("Demande de due diligence ");
            m.setTo(Arrays.asList(u.getEmail()));
            notificationService.sendEmail(m);
        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @PostMapping("/idd/fics")
    public HttpBodyResponse idd(@RequestParam Integer idUser,@RequestParam Integer idDue,@RequestParam String commentaire,@RequestParam(value = "file1",required = false) MultipartFile file1,@RequestParam(value = "file2",required = false) MultipartFile file2){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{

            DueDiligence dueDiligence= duediligenecDao.findById(idDue).get();
            Users u=usersDao.findById(idUser).get();
            String links="";
            String parms="";
            if(file1!=null){
                parms+="IDD FIX";
                links="uploads/"+storageService.save(file1);
            }
            if(file2!=null){
                parms+=";Rapport d'escalade";
                links+=";uploads/"+storageService.save(file2);
            }
            dueDiligence.setParams2(parms);
            dueDiligence.setPaths2(links);
            dueDiligence.setDateCloture2(new Date());
            dueDiligence.setValidateur(u);
            dueDiligence.setCommentaire2(commentaire);
            dueDiligence.setStep(9);
            duediligenecDao.save(dueDiligence);
            for (Users users : usersDao.findAll()) {

                String test = "Bonjour " + u.getNoms() + ", La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire  est: " + dueDiligence.getObjet() + " est soumise ce "+ new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateCloture()) +" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance2()) + ". Merci due diligence app";
                String html = "<h4>Bonjour " + u.getNoms() + ", <br/> La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire est : " + dueDiligence.getObjet() + " est soumise ce "+new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateCloture())+" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour  un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance2()) + "<br/> .  Merci due diligence app</h4>";
                MailUtils m = new MailUtils();
                m.setFrom("duediligence@orange.com");
                m.setText(test);
                m.setHtml(html);
                m.setSubject("Demande de due diligence ");
                m.setTo(Arrays.asList(users.getEmail()));
                if (dueDiligence.getFic() && users.getRole().getDescription().equalsIgnoreCase("cco")) {
                    notificationService.sendEmail(m);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @PostMapping("/rejete")
    public HttpBodyResponse rejeter(@RequestBody TreatRequest request){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            log.info(" request {} ",request);
            DueDiligence dueDiligence= duediligenecDao.findById(request.getIdDue()).get();
            dueDiligence.setDateCloture(new Date());
            dueDiligence.setJuriste(usersDao.findById(request.getIdUser()).get());
            dueDiligence.setCommentaire(request.getCommentaire());
            dueDiligence.setStep(3);
            duediligenecDao.save(dueDiligence);
            Users u=dueDiligence.getDemandeur();
            String test = "Bonjour "+ dueDiligence.getDemandeur().getNoms()+",\r\n" +
                    "\r\n" +
                    "Après analyse, la due diligence  N°"+dueDiligence.getMo()+" de +"+dueDiligence.getObjet()+"+ est conforme.\r\n" +
                    "\r" +
                    "Nous ne pouvons pas donc évoluer suite à la raison : '"+dueDiligence.getCommentaire()+"'.\r\n" +
                    "\r\n" +
                    "Cordialement due diligence\r\n";
            String html = "<p>"+"Bonjour "+ dueDiligence.getDemandeur().getNoms()+",\r\n" +
                    "<br/><br/>" +
                    "Après analyse, la due diligence N°"+dueDiligence.getMo()+" de "+dueDiligence.getObjet()+" est rejeté.\r\n" +
                    "<br/>" +
                    "Nous ne pouvons pas donc évoluer suite à la raison : '"+dueDiligence.getCommentaire()+"'.\r\n" +
                    "<br/><br/>" +
                    "Cordialement Due diligence<br/>"+"</p>";

            MailUtils m = new MailUtils();
            m.setFrom("duediligence@orange.com");
            m.setText(test);
            m.setHtml(html);
            m.setSubject("Demande de due diligence ");
            m.setTo(Arrays.asList(u.getEmail()));
            notificationService.sendEmail(m);
        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @PostMapping("/rejete/fic")
    public HttpBodyResponse rejet(@RequestBody TreatRequest request){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            log.info(" request {} ",request);
            DueDiligence dueDiligence= duediligenecDao.findById(request.getIdDue()).get();
            dueDiligence.setDateCloture2(new Date());
            dueDiligence.setValidateur(usersDao.findById(request.getIdUser()).get());
            dueDiligence.setCommentaire2(request.getCommentaire());
            dueDiligence.setStep(5);
            duediligenecDao.save(dueDiligence);
            Users u=dueDiligence.getDemandeur();
            String s= dueDiligence.getCommentaire();
            try{
                s= dueDiligence.getCommentaire2();
            }catch (Exception e){

            }
                  String test = "Bonjour "+ dueDiligence.getDemandeur().getNoms()+",\r\n" +
                    "\n\r" +
                    "Après analyse, la due diligence de "+dueDiligence.getEntite()+" est rejeté.\r\n" +
                    "\n\r" +
                    "Nous ne pouvons pas donc évoluer  suite à la raison : '"+s+"'.\r\n" +
                    "\n\r" +
                    "Cordialement\r\n";
                  String html = "<p>"+"Bonjour "+ dueDiligence.getDemandeur().getNoms()+",\r\n" +
                    "<br/><br/>" +
                    "Après analyse, la due diligence de "+dueDiligence.getEntite()+" est rejeté.\r\n" +
                    "<br/>" +
                    "Nous ne pouvons pas donc évoluer  suite à la raison : '"+s+"'.\r\n" +
                    "<br/><br/>" +
                    "Cordialement\r\n"+"</p>";
            MailUtils m = new MailUtils();
            m.setFrom("duediligence@orange.com");
            m.setText(test);
            m.setHtml(html);
            m.setSubject("Demande de due diligence ");
            m.setTo(Arrays.asList(u.getEmail()));
            notificationService.sendEmail(m);
        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }

    @PostMapping("/rejete/dal")
    public HttpBodyResponse rejetDal(@RequestBody TreatRequest request){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            log.info(" request {} ",request);
            DueDiligence dueDiligence= duediligenecDao.findById(request.getIdDue()).get();
            dueDiligence.setDateCloture2(new Date());
            dueDiligence.setValidateur(usersDao.findById(request.getIdUser()).get());
            dueDiligence.setCommentaire2(request.getCommentaire());
            dueDiligence.setStep(5);
            duediligenecDao.save(dueDiligence);
            Users u=dueDiligence.getDemandeur();
            String s= dueDiligence.getCommentaire();
            try{
                s= dueDiligence.getCommentaire2();
            }catch (Exception e){

            }
            String test = "Bonjour "+ dueDiligence.getDemandeur().getNoms()+",\r\n" +
                    "\n\r" +
                    "Après analyse, la due diligence de "+dueDiligence.getEntite()+" est rejeté.\r\n" +
                    "\n\r" +
                    "Nous ne pouvons pas donc évoluer  suite à la raison : '"+s+"'.\r\n" +
                    "\n\r" +
                    "Cordialement\r\n";
            String html = "<p>"+"Bonjour "+ dueDiligence.getDemandeur().getNoms()+",\r\n" +
                    "<br/><br/>" +
                    "Après analyse, la due diligence de "+dueDiligence.getEntite()+" est rejeté.\r\n" +
                    "<br/>" +
                    "Nous ne pouvons pas donc évoluer  suite à la raison : '"+s+"'.\r\n" +
                    "<br/><br/>" +
                    "Cordialement\r\n"+"</p>";
            MailUtils m = new MailUtils();
            m.setFrom("duediligence@orange.com");
            m.setText(test);
            m.setHtml(html);
            m.setSubject("Demande de due diligence ");
            m.setTo(Arrays.asList(u.getEmail()));
            //
        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
    @PostMapping("/form1")
    public HttpBodyResponse save1(@RequestParam Boolean urgent,@RequestParam(required = false) Integer user,@RequestParam(required = false,defaultValue = "") String objet,@RequestParam(required = false) Double valeur,@RequestParam Integer partenariat_id,@RequestParam(required = false,defaultValue = "")  String origine,@RequestParam Integer service,@RequestParam(required = false,defaultValue = "")  String entite,@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3,@RequestParam(value = "file4",required = false) MultipartFile file4,@RequestParam("file5") MultipartFile file5,@RequestParam("file6") MultipartFile file6,@RequestParam("file7") MultipartFile file7,@RequestParam(value = "file8",required = false) MultipartFile file8,@RequestParam(value = "file9",required = false) MultipartFile file9,@RequestParam("file10") MultipartFile file10,@RequestParam("file11") MultipartFile file11,@RequestParam("file12") MultipartFile file12,@RequestParam("file13") MultipartFile file13 ,@RequestParam("file14") MultipartFile file14,@RequestParam("file15") MultipartFile file15,@RequestParam(required = false,value = "file16") MultipartFile file16){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            log.info("urgent {}",urgent);
            DueDiligence dueDiligence=new DueDiligence();
            dueDiligence.setMo(Utils.getMo());
            dueDiligence.setOrigine(origine);
            dueDiligence.setEntite(entite);
            dueDiligence.setObjet(objet);
            dueDiligence.setFic(false);
            dueDiligence.setOm(false);
            dueDiligence.setUrgent(urgent);
            dueDiligence.setPartenariat(partenariatDao.findById(partenariat_id).get());
            if(!entite.equals("")) {
                dueDiligence.setDemandeur(usersDao.findById(user).get());
            }

            dueDiligence.setType("société commerciale");
            dueDiligence.setDateDemande(new Date());
            dueDiligence.setEcheance(Utils.getDate(new Date(),appInfo.getDelay()));
            dueDiligence.setValeur(valeur);
            Integer delay1= appInfo.getDelay();
            Integer delay2=appInfo.getDelay();
            if(appInfo.getDelay()%2!=0){
                delay1+=1;
            }
            if(service==2){

                dueDiligence.setOm(true);
                dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
            }else{
                if(valeur!=null) {
                    dueDiligence.setFic(Utils.checkFic(partenariat_id, valeur));
                    if (dueDiligence.getFic()) {
                        dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                        dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
                    }
                }
            }


            String links="uploads/"+storageService.save(file1);
            String paths="Identification Nationale";
            links+=";uploads/"+storageService.save(file2);
            paths+=";RCCM";
            links+=";uploads/"+storageService.save(file3);
            paths+=";Attestation de situation fiscale valide (NIF)";
            if(file4!=null){
                links+=";uploads/"+storageService.save(file4);
                paths+=";Avis d'assujettissement à la TVA";
            }
            links+=";uploads/"+storageService.save(file5);
            paths+=";Statuts notariés";
            links+=";uploads/"+storageService.save(file6);
            paths+=";Acte notarié des statuts";
            links+=";uploads/"+storageService.save(file7);
            paths+=";Preuve de dépôt des statuts au greffe du Tribunal de Commerce";
            if(file8!=null){
                links+=";uploads/"+storageService.save(file8);
                paths+=";Agrément à l'Autorité de Régulation à la Sous-traitance (facultatif)";
            }
            if(file9!=null) {
                links += ";uploads/" + storageService.save(file9);
                paths += ";Agrément à l'Autorité du secteur";
            }
            links+=";uploads/"+storageService.save(file10);
            paths+=";Acte nomination valide du Directeur Général";
            links+=";uploads/"+storageService.save(file11);
            paths+=";Pièce d'identité du Gérant";
            links+=";uploads/"+storageService.save(file12);
            paths+=";Relevés d'Identité Bancaire (RIB) ou fiche des coordonnées bancaires";
            links+=";uploads/"+storageService.save(file13);
            paths+=";Accord de confidentialité";
            links+=";uploads/"+storageService.save(file14);
            paths+=";Questionnaire d'auto-déclaration";
            links+=";uploads/"+storageService.save(file15);
            paths+=";Charte éthique";
            if(file16!=null) {
                links+=";uploads/"+storageService.save(file16);
                paths+=";Listes des membres du conseil d'administration";
            }

            dueDiligence.setPaths(links);
            dueDiligence.setParams(paths);
           log.info("size of params : {}",dueDiligence.getParams().length());
            dueDiligence.setStep(1);
            dueDiligence.setDate(new Date());
            duediligenecDao.save(dueDiligence);
            if(!entite.equals("")){
                for(Users u: usersDao.findAll()){
                    if (u.getRole().getDescription().equalsIgnoreCase("djr")) {
                        String test = "Bonjour " + u.getNoms() + ", La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire  est: " + dueDiligence.getObjet() + " est soumise ce "+ new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande()) +" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + ". Merci due diligence app";
                        String html = "<h4>Bonjour " + u.getNoms() + ", <br/> La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire est : " + dueDiligence.getObjet() + " est soumise ce "+new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande())+" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour  un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + "<br/> .  Merci due diligence app</h4>";
                        MailUtils m=new MailUtils();
                        m.setFrom("duediligence@orange.com");
                        m.setText(test);
                        m.setHtml(html);
                        m.setSubject("Demande de due diligence ");
                        m.setTo(Arrays.asList(u.getEmail()));
                        notificationService.sendEmail(m);
                    }

                }
            }else {

            }


        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");

            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }

    @PostMapping("/form2")
    public HttpBodyResponse save2(@RequestParam Boolean urgent,@RequestParam Integer user,@RequestParam(required = false,defaultValue = "") String objet,@RequestParam(required = false) Double valeur,@RequestParam Integer partenariat_id,@RequestParam(required = false,defaultValue = "") String origine,@RequestParam Integer service,@RequestParam(required = false,defaultValue = "") String entite,@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3,@RequestParam("file4") MultipartFile file4,@RequestParam("file5") MultipartFile file5,@RequestParam("file6") MultipartFile file6,@RequestParam("file7") MultipartFile file7,@RequestParam("file8") MultipartFile file8,@RequestParam("file9") MultipartFile file9,@RequestParam(value = "file10",required = false) MultipartFile file10){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            log.info("urgent {}",urgent);
            DueDiligence dueDiligence=new DueDiligence();
            dueDiligence.setMo(Utils.getMo());
            dueDiligence.setOrigine(origine);
            dueDiligence.setEntite(entite);
            dueDiligence.setObjet(objet);
            dueDiligence.setFic(false);
            dueDiligence.setOm(false);
            dueDiligence.setUrgent(urgent);
            dueDiligence.setPartenariat(partenariatDao.findById(partenariat_id).get());
            if(!entite.equals("")) {
                dueDiligence.setDemandeur(usersDao.findById(user).get());
            }
            dueDiligence.setType("société étrangère sous l’espace OHADA");
            dueDiligence.setDateDemande(new Date());
            dueDiligence.setEcheance(Utils.getDate(new Date(),appInfo.getDelay()));
            dueDiligence.setValeur(valeur);
            Integer delay1= appInfo.getDelay();
            Integer delay2=appInfo.getDelay();
            if(appInfo.getDelay()%2!=0){
                delay1+=1;
            }
            if(service==2){

                dueDiligence.setOm(true);
                dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
            }else{
                if(valeur!=null) {
                    dueDiligence.setFic(Utils.checkFic(partenariat_id, valeur));
                    if (dueDiligence.getFic()) {
                        dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                        dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
                    }
                }
            }

            String links="uploads/"+storageService.save(file1);
            links+=";uploads/"+storageService.save(file2);
            links+=";uploads/"+storageService.save(file3);
            links+=";uploads/"+storageService.save(file4);
            links+=";uploads/"+storageService.save(file5);
            links+=";uploads/"+storageService.save(file6);
            links+=";uploads/"+storageService.save(file7);
            links+=";uploads/"+storageService.save(file8);
            links+=";uploads/"+storageService.save(file9);
            String params="";
            if(file10!=null) {
                links+=";uploads/"+storageService.save(file10);
                params+=";Listes des membres du conseil d'administration";
            }

            dueDiligence.setPaths(links);
            dueDiligence.setParams("Acte d'enregistrement au Registre de Commerce;Attestation de situation fiscale valide (NIF);Statuts notariés;Acte nomination valide du Directeur Général;Pièce d'identité du Gérant;Relevés d'Identité Bancaire (RIB) ou fiche des coordonnées bancaires;Accord de confidentialité;Questionnaire d'auto-déclaration;Charte éthique"+params);
            dueDiligence.setStep(1);
            dueDiligence.setDate(new Date());
            duediligenecDao.save(dueDiligence);
            if(!entite.equals("")){
                for(Users u: usersDao.findAll()){
                    if (u.getRole().getDescription().equalsIgnoreCase("djr")) {
                        String test = "Bonjour " + u.getNoms() + ", La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire  est: " + dueDiligence.getObjet() + " est soumise ce "+ new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande()) +" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + ". Merci due diligence app";
                        String html = "<h4>Bonjour " + u.getNoms() + ", <br/> La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire est : " + dueDiligence.getObjet() + " est soumise ce "+new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande())+" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour  un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + "<br/> .  Merci due diligence app</h4>";
                        MailUtils m=new MailUtils();
                        m.setFrom("duediligence@orange.com");
                        m.setText(test);
                        m.setHtml(html);
                        m.setSubject("Demande de due diligence ");
                        m.setTo(Arrays.asList(u.getEmail()));
                        notificationService.sendEmail(m);
                    }
                }
            }else {

            }

        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");

            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }

    @PostMapping("/form3")
    public HttpBodyResponse save3(@RequestParam Boolean urgent,@RequestParam Integer user,@RequestParam(required = false,defaultValue = "") String objet,@RequestParam(required = false) Double valeur,@RequestParam Integer partenariat_id,@RequestParam(required = false,defaultValue = "") String origine,@RequestParam Integer service,@RequestParam(required = false,defaultValue = "") String entite,@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3,@RequestParam("file4") MultipartFile file4,@RequestParam("file5") MultipartFile file5,@RequestParam("file6") MultipartFile file6,@RequestParam("file7") MultipartFile file7,@RequestParam("file8") MultipartFile file8,@RequestParam("file9") MultipartFile file9,@RequestParam(value = "file10",required = false) MultipartFile file10){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            DueDiligence dueDiligence=new DueDiligence();
            dueDiligence.setMo(Utils.getMo());
            dueDiligence.setOrigine(origine);
            dueDiligence.setEntite(entite);
            dueDiligence.setFic(false);
            dueDiligence.setOm(false);
            dueDiligence.setUrgent(urgent);
            dueDiligence.setObjet(objet);
            dueDiligence.setValeur(valeur);
            dueDiligence.setPartenariat(partenariatDao.findById(partenariat_id).get());
            if(!entite.equals("")) {
                dueDiligence.setDemandeur(usersDao.findById(user).get());
            }
            dueDiligence.setType("société étrangère en dehors de l’espace OHADA");
            dueDiligence.setDateDemande(new Date());
            dueDiligence.setEcheance(Utils.getDate(new Date(),appInfo.getDelay()));
            Integer delay1= appInfo.getDelay();
            Integer delay2=appInfo.getDelay();
            if(appInfo.getDelay()%2!=0){
                delay1+=1;
            }
            if(service==2){

                dueDiligence.setOm(true);
                dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
            }else{
                if(valeur!=null) {
                    dueDiligence.setFic(Utils.checkFic(partenariat_id, valeur));
                    if (dueDiligence.getFic()) {
                        dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                        dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
                    }
                }
            }
            String links="uploads/"+storageService.save(file1);
            links+=";uploads/"+storageService.save(file2);
            links+=";uploads/"+storageService.save(file3);
            links+=";uploads/"+storageService.save(file4);
            links+=";uploads/"+storageService.save(file5);
            links+=";uploads/"+storageService.save(file6);
            links+=";uploads/"+storageService.save(file7);
            links+=";uploads/"+storageService.save(file8);
            links+=";uploads/"+storageService.save(file9);
            String params="";
            if(file10!=null) {
                links+=";uploads/"+storageService.save(file10);
                params+=";Listes des membres du conseil d'administration";
            }

            dueDiligence.setPaths(links);
            dueDiligence.setParams("Acte d'enregistrement au Registre de Commerce;Attestation de situation fiscale valide (NIF);Statuts notariés;Acte nomination valide du Directeur Général;Pièce d'identité du Gérant;Relevés d'Identité Bancaire (RIB) ou fiche des coordonnées bancaires;Accord de confidentialité;Questionnaire d'auto-déclaration;Charte éthique"+params);
            dueDiligence.setStep(1);
            dueDiligence.setDate(new Date());
            duediligenecDao.save(dueDiligence);
            if(!entite.equals("")){
                for(Users u: usersDao.findAll()){
                    if (u.getRole().getDescription().equalsIgnoreCase("djr")) {
                        String test = "Bonjour " + u.getNoms() + ", La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire  est: " + dueDiligence.getObjet() + " est soumise ce "+ new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande()) +" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + ". Merci due diligence app";
                        String html = "<h4>Bonjour " + u.getNoms() + ", <br/> La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire est : " + dueDiligence.getObjet() + " est soumise ce "+new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande())+" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour  un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + "<br/> .  Merci due diligence app</h4>";
                        MailUtils m=new MailUtils();
                        m.setFrom("duediligence@orange.com");
                        m.setText(test);
                        m.setHtml(html);
                        m.setSubject("Demande de due diligence ");
                        m.setTo(Arrays.asList(u.getEmail()));
                        notificationService.sendEmail(m);
                    }

                }
            }else {

            }

        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");

            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }

    @PostMapping("/form4")
    public HttpBodyResponse save4(@RequestParam Boolean urgent,@RequestParam Integer user,@RequestParam(required = false,defaultValue = "") String objet,@RequestParam(required = false) Double valeur,@RequestParam Integer partenariat_id,@RequestParam(required = false,defaultValue = "") String origine,@RequestParam(required = false,defaultValue = "") String entite,@RequestParam Integer service,@RequestParam("file1") MultipartFile file1,@RequestParam(value = "file2",required = false) MultipartFile file2,@RequestParam("file3") MultipartFile file3,@RequestParam("file4") MultipartFile file4,@RequestParam("file5") MultipartFile file5,@RequestParam("file6") MultipartFile file6,@RequestParam("file7") MultipartFile file7,@RequestParam("file8") MultipartFile file8,@RequestParam("file9") MultipartFile file9,@RequestParam(value = "file10",required = false) MultipartFile file10){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            DueDiligence dueDiligence=new DueDiligence();
            dueDiligence.setMo(Utils.getMo());
            dueDiligence.setOrigine(origine);
            dueDiligence.setEntite(entite);
            dueDiligence.setFic(false);
            dueDiligence.setOm(false);
            dueDiligence.setUrgent(urgent);
            dueDiligence.setObjet(objet);
            dueDiligence.setValeur(valeur);
            dueDiligence.setPartenariat(partenariatDao.findById(partenariat_id).get());
            if(!entite.equals("")) {
                dueDiligence.setDemandeur(usersDao.findById(user).get());
            }
            dueDiligence.setType("association sans but lucratif");
            dueDiligence.setDateDemande(new Date());
            dueDiligence.setEcheance(Utils.getDate(new Date(),appInfo.getDelay()));
            Integer delay1= appInfo.getDelay();
            Integer delay2=appInfo.getDelay();
            if(appInfo.getDelay()%2!=0){
                delay1+=1;
            }
            if(service==2){

                dueDiligence.setOm(true);
                dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
            }else{
                if(valeur!=null) {
                    dueDiligence.setFic(Utils.checkFic(partenariat_id, valeur));
                    if (dueDiligence.getFic()) {
                        dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                        dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
                    }
                }
            }
            String links="uploads/"+storageService.save(file1);
            String params="Arrêté Ministériel octroyant la personnalité juridique ou le F92";
            if(file2!=null){
                links+=";uploads/"+storageService.save(file2);
                params+=";Preuve de publication des statuts au Journal Officiel (facultatif)";
            }

            links+=";uploads/"+storageService.save(file3);
            params+=";Statuts notariés";
            links+=";uploads/"+storageService.save(file4);
            params+=";Acte nomination valide du Président/Coordonnateur";
            links+=";uploads/"+storageService.save(file5);
            params+=";Pièce d'identité du Président/Coordonnateur";
            links+=";uploads/"+storageService.save(file6);
            params+=";Relevés d'Identité Bancaire (RIB) ou fiche des coordonnées bancaires";
            links+=";uploads/"+storageService.save(file7);
            params+=";Accord de confidentialité";
            links+=";uploads/"+storageService.save(file8);
            params+=";Questionnaire d'auto-déclaration";
            links+=";uploads/"+storageService.save(file9);
            params+=";Charte éthique";
            if(file10!=null) {
                links+=";uploads/"+storageService.save(file10);
                params+=";Listes des membres du conseil d'administration";
            }

            dueDiligence.setPaths(links);
            dueDiligence.setParams(params);
            dueDiligence.setStep(1);
            dueDiligence.setDate(new Date());
            duediligenecDao.save(dueDiligence);
            if(!entite.equals("")){
                for(Users u: usersDao.findAll()){
                    if (u.getRole().getDescription().equalsIgnoreCase("djr")) {
                        String test = "Bonjour " + u.getNoms() + ", La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire  est: " + dueDiligence.getObjet() + " est soumise ce "+ new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande()) +" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + ". Merci due diligence app";
                        String html = "<h4>Bonjour " + u.getNoms() + ", <br/> La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire est : " + dueDiligence.getObjet() + " est soumise ce "+new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande())+" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour  un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + "<br/> .  Merci due diligence app</h4>";
                        MailUtils m=new MailUtils();
                        m.setFrom("duediligence@orange.com");
                        m.setText(test);
                        m.setHtml(html);
                        m.setSubject("Demande de due diligence ");
                        m.setTo(Arrays.asList(u.getEmail()));
                        notificationService.sendEmail(m);
                    }

                }
            }else {

            }

        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");

            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }

    @PostMapping("/form5")
    public HttpBodyResponse save5(@RequestParam Boolean urgent,@RequestParam Integer user,@RequestParam(required = false,defaultValue = "") String objet,@RequestParam(required = false) Double valeur,@RequestParam Integer partenariat_id,@RequestParam(required = false,defaultValue = "") String origine,@RequestParam(required = false,defaultValue = "") String entite,@RequestParam Integer service,@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3,@RequestParam(value = "file4",required = false) MultipartFile file4,@RequestParam("file5") MultipartFile file5,@RequestParam("file6") MultipartFile file6,@RequestParam("file7") MultipartFile file7,@RequestParam("file8") MultipartFile file8,@RequestParam("file9") MultipartFile file9){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            DueDiligence dueDiligence=new DueDiligence();
            dueDiligence.setMo(Utils.getMo());
            dueDiligence.setOrigine(origine);
            dueDiligence.setEntite(entite);
            dueDiligence.setFic(false);
            dueDiligence.setOm(false);
            dueDiligence.setUrgent(urgent);
            dueDiligence.setObjet(objet);
            dueDiligence.setValeur(valeur);
            dueDiligence.setPartenariat(partenariatDao.findById(partenariat_id).get());
            if(!entite.equals("")) {
                dueDiligence.setDemandeur(usersDao.findById(user).get());
            }
            dueDiligence.setType("Etablissement");
            dueDiligence.setDateDemande(new Date());
            dueDiligence.setEcheance(Utils.getDate(new Date(),appInfo.getDelay()));
            dueDiligence.setValeur(valeur);
            Integer delay1= appInfo.getDelay();
            Integer delay2=appInfo.getDelay();
            if(appInfo.getDelay()%2!=0){
                delay1+=1;
            }
            if(service==2){

                dueDiligence.setOm(true);
                dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
            }else{
                if(valeur!=null) {
                    dueDiligence.setFic(Utils.checkFic(partenariat_id, valeur));
                    if (dueDiligence.getFic()) {
                        dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                        dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
                    }
                }
            }
            String links="uploads/"+storageService.save(file1);
            String params="Identification Nationale";
            links+=";uploads/"+storageService.save(file2);
            params+=";RCCM";
            links+=";uploads/"+storageService.save(file3);
            params+=";Attestation de situation fiscale valide (NIF)";
            if(file4!=null){
                links+=";uploads/"+storageService.save(file4);
                params+=";Agrément à l'Autorité du secteur d'activité (facultatif)";
            }
            links+=";uploads/"+storageService.save(file5);
            params+=";Relevés d'Identité Bancaire (RIB) ou fiche des coordonnées bancaires";
            links+=";uploads/"+storageService.save(file6);
            params+=";Pièce d'identité du propriétaire";
            links+=";uploads/"+storageService.save(file7);
            params+=";Accord de confidentialité";
            links+=";uploads/"+storageService.save(file8);
            params+=";Questionnaire d'auto-déclaration";
            links+=";uploads/"+storageService.save(file9);
            params+=";Charte éthique";

            dueDiligence.setPaths(links);
            dueDiligence.setParams(params);
            dueDiligence.setStep(1);
            dueDiligence.setDate(new Date());
            duediligenecDao.save(dueDiligence);
            if(!entite.equals("")){
                for(Users u: usersDao.findAll()){
                    if (u.getRole().getDescription().equalsIgnoreCase("djr")) {
                        String test = "Bonjour " + u.getNoms() + ", La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire  est: " + dueDiligence.getObjet() + " est soumise ce "+ new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande()) +" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + ". Merci due diligence app";
                        String html = "<h4>Bonjour " + u.getNoms() + ", <br/> La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire est : " + dueDiligence.getObjet() + " est soumise ce "+new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande())+" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour  un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + "<br/> .  Merci due diligence app</h4>";
                        MailUtils m=new MailUtils();
                        m.setFrom("duediligence@orange.com");
                        m.setText(test);
                        m.setHtml(html);
                        m.setSubject("Demande de due diligence ");
                        m.setTo(Arrays.asList(u.getEmail()));
                        notificationService.sendEmail(m);
                    }

                }
            }else {

            }

        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");

            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }

    @PostMapping("/form6")
    public HttpBodyResponse save6(@RequestParam Boolean urgent,@RequestParam Integer user,@RequestParam(required = false,defaultValue = "") String objet,@RequestParam(required = false) Double valeur,@RequestParam Integer partenariat_id,@RequestParam(required = false,defaultValue = "") String origine,@RequestParam(required = false,defaultValue = "") String entite,@RequestParam Integer service,@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3,@RequestParam(value = "file4",required = false) MultipartFile file4,@RequestParam("file5") MultipartFile file5,@RequestParam("file6") MultipartFile file6,@RequestParam("file7") MultipartFile file7,@RequestParam("file8") MultipartFile file8,@RequestParam(value = "file9",required = false) MultipartFile file9){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            DueDiligence dueDiligence=new DueDiligence();
            dueDiligence.setMo(Utils.getMo());
            dueDiligence.setOrigine(origine);
            dueDiligence.setEntite(entite);
            dueDiligence.setFic(false);
            dueDiligence.setOm(false);
            dueDiligence.setUrgent(urgent);
            dueDiligence.setObjet(objet);
            dueDiligence.setValeur(valeur);
            dueDiligence.setPartenariat(partenariatDao.findById(partenariat_id).get());
            if(!entite.equals("")) {
                dueDiligence.setDemandeur(usersDao.findById(user).get());
            }
            dueDiligence.setType("cabinet d’avocats");
            dueDiligence.setDateDemande(new Date());
            dueDiligence.setEcheance(Utils.getDate(new Date(),appInfo.getDelay()));
            dueDiligence.setValeur(valeur);
            Integer delay1= appInfo.getDelay();
            Integer delay2=appInfo.getDelay();
            if(appInfo.getDelay()%2!=0){
                delay1+=1;
            }
            if(service==2){

                dueDiligence.setOm(true);
                dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
            }else{
                if(valeur!=null) {
                    dueDiligence.setFic(Utils.checkFic(partenariat_id, valeur));
                    if (dueDiligence.getFic()) {
                        dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                        dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
                    }
                }
            }
            String links="uploads/"+storageService.save(file1);
            String parms="Carte d'avocat";
            links+=";uploads/"+storageService.save(file2);
            parms+=";La preuve d'inscription au bureau (numéro d'ordre)";
            links+=";uploads/"+storageService.save(file3);
            parms+=";Avis d'assujettissement à la TVA";
            if(file4!=null){
                links+=";uploads/"+storageService.save(file4);
                parms+=";Agrément à l'Autorité du secteur d'activité (facultatif);";
            }
            links+=";uploads/"+storageService.save(file5);
            parms+="Relevés d'Identité Bancaire (RIB) ou fiche des coordonnées bancaires";
            links+=";uploads/"+storageService.save(file6);
            parms+=";Accord de confidentialité";
            links+=";uploads/"+storageService.save(file7);
            parms+=";Questionnaire d'auto-déclaration";
            links+=";uploads/"+storageService.save(file8);
            parms+=";Charte éthique";
            if(file9!=null) {
                links+=";uploads/"+storageService.save(file9);
                parms+=";Listes des membres du conseil d'administration";
            }


            dueDiligence.setPaths(links);
            dueDiligence.setParams(parms);
            dueDiligence.setStep(1);
            dueDiligence.setDate(new Date());
            duediligenecDao.save(dueDiligence);
            if(!entite.equals("")){
                for(Users u: usersDao.findAll()){
                    if (u.getRole().getDescription().equalsIgnoreCase("djr")) {
                        String test = "Bonjour " + u.getNoms() + ", La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire  est: " + dueDiligence.getObjet() + " est soumise ce "+ new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande()) +" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + ". Merci due diligence app";
                        String html = "<h4>Bonjour " + u.getNoms() + ", <br/> La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire est : " + dueDiligence.getObjet() + " est soumise ce "+new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande())+" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour  un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + "<br/> .  Merci due diligence app</h4>";
                        MailUtils m=new MailUtils();
                        m.setFrom("duediligence@orange.com");
                        m.setText(test);
                        m.setHtml(html);
                        m.setSubject("Demande de due diligence ");
                        m.setTo(Arrays.asList(u.getEmail()));
                        notificationService.sendEmail(m);
                    }

                }
            }else {

            }

        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");

            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }

    @PostMapping("/form7")
    public HttpBodyResponse save7(@RequestParam Boolean urgent,@RequestParam Integer user,@RequestParam(required = false,defaultValue = "") String objet,@RequestParam(required = false) Double valeur,@RequestParam Integer partenariat_id,@RequestParam(required = false,defaultValue = "") String origine,@RequestParam(required = false,defaultValue = "") String entite,@RequestParam Integer service,@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3,@RequestParam("file4") MultipartFile file4,@RequestParam("file5") MultipartFile file5){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            DueDiligence dueDiligence=new DueDiligence();
            dueDiligence.setMo(Utils.getMo());
            dueDiligence.setOrigine(origine);
            dueDiligence.setEntite(entite);
            dueDiligence.setFic(false);
            dueDiligence.setOm(false);
            dueDiligence.setUrgent(urgent);
            dueDiligence.setObjet(objet);
            dueDiligence.setValeur(valeur);
            dueDiligence.setPartenariat(partenariatDao.findById(partenariat_id).get());
            if(!entite.equals("")) {
                dueDiligence.setDemandeur(usersDao.findById(user).get());
            }
            dueDiligence.setType("personne physique ");
            dueDiligence.setDateDemande(new Date());
            dueDiligence.setEcheance(Utils.getDate(new Date(),appInfo.getDelay()));
            Integer delay1= appInfo.getDelay();
            Integer delay2=appInfo.getDelay();
            if(appInfo.getDelay()%2!=0){
                delay1+=1;
            }
            if(service==2){

                dueDiligence.setOm(true);
                dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
            }else{
                if(valeur!=null) {
                    dueDiligence.setFic(Utils.checkFic(partenariat_id, valeur));
                    if (dueDiligence.getFic()) {
                        dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                        dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
                    }
                }
            }
            String links="uploads/"+storageService.save(file1);
            links+=";uploads/"+storageService.save(file2);
            links+=";uploads/"+storageService.save(file3);
            links+=";uploads/"+storageService.save(file4);
            links+=";uploads/"+storageService.save(file5);

            dueDiligence.setPaths(links);
            dueDiligence.setParams("Pièce d'identité valide;Relevés d'Identité Bancaire (RIB) ou fiche des coordonnées bancaires;Accord de confidentialité;Questionnaire d'auto-déclaration;Charte éthique");
            dueDiligence.setStep(1);
            dueDiligence.setDate(new Date());
            duediligenecDao.save(dueDiligence);
            if(!entite.equals("")){
                for(Users u: usersDao.findAll()){
                    if (u.getRole().getDescription().equalsIgnoreCase("djr")) {
                        String test = "Bonjour " + u.getNoms() + ", La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire  est: " + dueDiligence.getObjet() + " est soumise ce "+ new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande()) +" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + ". Merci due diligence app";
                        String html = "<h4>Bonjour " + u.getNoms() + ", <br/> La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire est : " + dueDiligence.getObjet() + " est soumise ce "+new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande())+" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour  un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + "<br/> .  Merci due diligence app</h4>";
                        MailUtils m=new MailUtils();
                        m.setFrom("duediligence@orange.com");
                        m.setText(test);
                        m.setHtml(html);
                        m.setSubject("Demande de due diligence ");
                        m.setTo(Arrays.asList(u.getEmail()));
                        notificationService.sendEmail(m);
                    }

                }
            }else {

            }

        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");

            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }

    @PostMapping("/form8")
    public HttpBodyResponse save8(@RequestParam Boolean urgent,@RequestParam Integer user,@RequestParam(required = false,defaultValue = "") String objet,@RequestParam(required = false) Double valeur,@RequestParam Integer partenariat_id,@RequestParam(required = false,defaultValue = "") String origine,@RequestParam(required = false,defaultValue = "") String entite,@RequestParam Integer service,@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3,@RequestParam("file4") MultipartFile file4,@RequestParam("file5") MultipartFile file5,@RequestParam("file6") MultipartFile file6){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            DueDiligence dueDiligence=new DueDiligence();
            dueDiligence.setMo(Utils.getMo());
            dueDiligence.setOrigine(origine);
            dueDiligence.setEntite(entite);
            dueDiligence.setFic(false);
            dueDiligence.setOm(false);
            dueDiligence.setUrgent(urgent);
            dueDiligence.setObjet(objet);
            dueDiligence.setValeur(valeur);
            dueDiligence.setPartenariat(partenariatDao.findById(partenariat_id).get());
            if(!entite.equals("")) {
                dueDiligence.setDemandeur(usersDao.findById(user).get());
            }
            dueDiligence.setType("bailleur");
            dueDiligence.setDateDemande(new Date());
            dueDiligence.setEcheance(Utils.getDate(new Date(),appInfo.getDelay()));
            Integer delay1= appInfo.getDelay();
            Integer delay2=appInfo.getDelay();
            if(appInfo.getDelay()%2!=0){
                delay1+=1;
            }
            if(service==2){

                dueDiligence.setOm(true);
                dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
            }else{
                if(valeur!=null) {
                    dueDiligence.setFic(Utils.checkFic(partenariat_id, valeur));
                    if (dueDiligence.getFic()) {
                        dueDiligence.setEcheance(Utils.getDate(new Date(),delay1));
                        dueDiligence.setEcheance2(Utils.getDate(new Date(),delay2));
                    }
                }
            }
            String links="uploads/"+storageService.save(file1);
            links+=";uploads/"+storageService.save(file2);
            links+=";uploads/"+storageService.save(file3);
            links+=";uploads/"+storageService.save(file4);
            links+=";uploads/"+storageService.save(file5);
            links+=";uploads/"+storageService.save(file6);
            dueDiligence.setPaths(links);
            dueDiligence.setParams("Pièce d'identité valide;Titre de propriété de la parcelle;Relevés d'Identité Bancaire (RIB) ou fiche des coordonnées bancaires;Accord de confidentialité;Questionnaire d'auto-déclaration;Charte éthique");
            dueDiligence.setStep(1);
            dueDiligence.setDate(new Date());
            duediligenecDao.save(dueDiligence);
            for(Users u: usersDao.findAll()){
                if (u.getRole().getDescription().equalsIgnoreCase("djr")) {
                    String test = "Bonjour " + u.getNoms() + ", La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire  est: " + dueDiligence.getObjet() + " est soumise ce "+ new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande()) +" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + ". Merci due diligence app";
                    String html = "<h4>Bonjour " + u.getNoms() + ", <br/> La demande de due diligence   N: " + dueDiligence.getMo() + " dont le partenaire est : " + dueDiligence.getObjet() + " est soumise ce "+new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getDateDemande())+" par "+dueDiligence.getDemandeur().getNoms()+" / "+dueDiligence.getOrigine()+" pour  un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(dueDiligence.getEcheance()) + "<br/> .  Merci due diligence app</h4>";
                    MailUtils m=new MailUtils();
                    m.setFrom("duediligence@orange.com");
                    m.setText(test);
                    m.setHtml(html);
                    m.setSubject("Demande de due diligence ");
                    m.setTo(Arrays.asList(u.getEmail()));
                    notificationService.sendEmail(m);

                }

            }

        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");

            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }

    @GetMapping("/show/{p}")
    public ResponseEntity<InputStreamResource> show(@PathVariable String p) {
        try {
             String fileName = "doc.pdf";

            File file = new File("uploads/"+p);
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-disposition", "inline;filename=" + fileName);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .body(resource);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return  null;
    }

    @GetMapping("/download/{p}")
    public ResponseEntity<InputStreamResource> dowload(@PathVariable String p) {
        try {

            String[] sp = p.split("\\.");
            String ext = sp[sp.length-1];
            String fileName = "doc.pdf";
            if(!ext.contains("pdf")){
                fileName="doc."+ext;
            }else {
                DueDiligence d=duediligenecDao.findByPath(p);
               String t=Utils.getName(d,p);
                if(t!=null){
                    fileName=t+".pdf";
                }
            }
            File file = new File("uploads/"+p);
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-disposition", "inline;filename=" + fileName);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return  null;
    }
    @DeleteMapping("/{id}")
    public HttpBodyResponse delete(@PathVariable Integer id){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{

            duediligenecDao.deleteById(id);
            bodyResponse.setResponse(true);

        }catch (Exception e){
            bodyResponse.setStatus("Failed");
            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }
}
