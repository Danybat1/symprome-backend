package cd.orange.due_diligence.controllers;


import cd.orange.due_diligence.daos.MembreDao;
import cd.orange.due_diligence.daos.PaiementDao;
import cd.orange.due_diligence.daos.TypePaiementDao;
import cd.orange.due_diligence.daos.UsersDao;
import cd.orange.due_diligence.entities.Centre;
import cd.orange.due_diligence.entities.Paiement;
import cd.orange.due_diligence.requests.PaiementRequest;
import cd.orange.due_diligence.responses.HttpBodyResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/paiement")
public class PaiementController {

    private PaiementDao paiementDao;
    private MembreDao membreDao;
    private UsersDao usersDao;
    private TypePaiementDao typePaiementDao;
    @GetMapping
    public HttpBodyResponse getAll(){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(paiementDao.findAll());
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
    @GetMapping("/type/{id}")
    public HttpBodyResponse getAllByType(@PathVariable Integer id){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(paiementDao.findAllByTypePaiementId(id));
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
    @GetMapping("/date/{date1}/{date2}")
    public HttpBodyResponse getAllByType(@PathVariable String date1,@PathVariable String date2){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            Date d=new SimpleDateFormat("yyyy-MM-dd").parse(date1);
            Date d2=new SimpleDateFormat("yyyy-MM-dd").parse(date2);
            bodyResponse.setResponse(paiementDao.getAllBetweenDates(d,d2));
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }


    @GetMapping("/{id}")
    public HttpBodyResponse getOne(@PathVariable Integer id){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(paiementDao.findById(id).get());
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
    @PostMapping
    public HttpBodyResponse save(@RequestBody PaiementRequest request){
       /*/ Role role=roleDao.findById(Utils.users.getId()).get();
        if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            Paiement s=new Paiement();
            s.setNoms(request.getNoms());
            s.setNumero(paiementDao.count()+1+"");
            s.setMontant(request.getMontant());
            s.setTelephone(request.getTelephone());
            s.setDevise(request.getDevise());
            s.setPercepteur(usersDao.findById(request.getPercepteur()).get());
            s.setTypePaiement(typePaiementDao.findById(request.getTypePaiement()).get());
            s.setMembre(membreDao.findById(request.getMembre()).get());
            s.setDateCreat(new Date());
            bodyResponse.setResponse(paiementDao.save(s));
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }


    @PutMapping
    public HttpBodyResponse update(@RequestBody PaiementRequest request){
       /*/ Role role=roleDao.findById(Utils.users.getId()).get();
        if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            Paiement s=paiementDao.findById(request.getId()).get();
            s.setNoms(request.getNoms());
            s.setMontant(request.getMontant());
            s.setTelephone(request.getTelephone());
            s.setDevise(request.getDevise());
            s.setPercepteur(usersDao.findById(request.getPercepteur()).get());
            s.setTypePaiement(typePaiementDao.findById(request.getTypePaiement()).get());
            s.setMembre(membreDao.findById(request.getMembre()).get());
            s.setDateCreat(new Date());
            bodyResponse.setResponse(paiementDao.save(s));
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }

    @DeleteMapping("/{id}")
    public HttpBodyResponse deleteOne(@PathVariable Integer id){
       /* Role role=roleDao.findById(Utils.users.getId()).get();
        if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } */
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            paiementDao.deleteById(id);
            bodyResponse.setResponse(true);
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
}
