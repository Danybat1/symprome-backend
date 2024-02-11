package cd.bnb.syprome.task;


import cd.bnb.syprome.daos.AudienceDao;
import cd.bnb.syprome.daos.DocumentDao;
import cd.bnb.syprome.daos.DueDiligenceDao;
import cd.bnb.syprome.daos.UsersDao;
import cd.bnb.syprome.entities.DueDiligence;
import cd.bnb.syprome.entities.Users;
import cd.bnb.syprome.services.NotificationService;
import cd.bnb.syprome.utilities.MailUtils;
import cd.bnb.syprome.utilities.Utils;
import org.springframework.scheduling.annotation.Scheduled;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
@Component
@Configuration
@EnableScheduling
@Slf4j
@AllArgsConstructor*/
public class NotificationTask {
    private DocumentDao documentDao;
    private AudienceDao audienceDao;
    private DueDiligenceDao dueDiligenceDao;
    private UsersDao usersDao;
    private NotificationService notificationService;
    @Scheduled(fixedDelay = 43200000)
    public  void init(){
        //log.info("Job starting ");
        ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.submit(() -> {
               // task();
            });

        executorService.shutdown();

       // log.info("Job finished ");
    }
    public  void task(){

        List<DueDiligence>duediligences=dueDiligenceDao.findAllByStep(1);
         for(DueDiligence d:duediligences){

             int diff= Utils.getDateDiff(d.getEcheance());
             if (diff<=2){

       for(Users u: usersDao.findAll()){
            if (u.getRole().getDescription().equalsIgnoreCase("djr")) {
                String test = "Bonjour " + u.getNoms() + ", La demande de due diligence   N: " + d.getMo() + " dont le partenaire  est: " + d.getObjet() + " est soumise ce "+ new SimpleDateFormat("dd/MM/yy").format(d.getDateDemande()) +" par "+d.getDemandeur().getNoms()+" / "+d.getOrigine()+" pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(d.getEcheance()) + ". Merci due diligence app";
                String html = "<h4>Bonjour " + u.getNoms() + ", <br/> La demande de due diligence   N: " + d.getMo() + " dont le partenaire est : " + d.getObjet() + " est soumise ce "+new SimpleDateFormat("dd/MM/yy").format(d.getDateDemande())+" par "+d.getDemandeur().getNoms()+" / "+d.getOrigine()+" pour  un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(d.getEcheance()) + "<br/> .  Merci due diligence app</h4>";
                MailUtils m = new MailUtils();
                m.setFrom("duediligence@orange.com");
                m.setText(test);
                m.setHtml(html);
                m.setSubject("Demande de due diligence ");
                m.setTo(Arrays.asList(u.getEmail()));
                notificationService.sendEmail(m);

            }
                }
             }


         }

        duediligences=dueDiligenceDao.findAllByStep(2);
        for(DueDiligence d:duediligences){

                int diff= Utils.getDateDiff(d.getEcheance2());
                if (diff<=2){
                    for(Users u: usersDao.findAll()){
                            String test = "Bonjour " + u.getNoms() + ", une demande de duediligence  N: " + d.getMo() + " dont l'objet est : " + d.getObjet() + " est en attente   pour un traitement avant le " + new SimpleDateFormat("dd/MM/yy").format(d.getEcheance2()) + ". Merci due diligence app";
                            String html = "<h4>Bonjour " + u.getNoms() + ",<br/>  une demande de duediligence  N: " + d.getMo() + " dont l'objet est : " + d.getObjet() + " est en attente pour un traitement avant le" + new SimpleDateFormat("dd/MM/yy").format(d.getEcheance2()) + "<br/> . Merci due diligence app</h4>";
                            MailUtils m = new MailUtils();
                            m.setFrom("duediligence@orange.com");
                            m.setText(test);
                            m.setHtml(html);
                            m.setSubject("Demande de due diligence ");
                            m.setTo(Arrays.asList(u.getEmail()));
                            if (d.getFic()&&u.getRole().getDescription().equalsIgnoreCase("fic")) {
                                notificationService.sendEmail(m);
                            } else if (d.getOm()&&u.getRole().getDescription().equalsIgnoreCase("om")) {
                                notificationService.sendEmail(m);
                            }

                    }
                }


        }
    }


}
