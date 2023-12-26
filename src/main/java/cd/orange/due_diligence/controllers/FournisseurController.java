package cd.orange.due_diligence.controllers;

import cd.orange.due_diligence.daos.FournisseurDao;
import cd.orange.due_diligence.entities.Fournisseur;
import cd.orange.due_diligence.entities.Role;
import cd.orange.due_diligence.requests.UpdatePasswordRequest;
import cd.orange.due_diligence.responses.HttpBodyResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/fournisseur")
public class FournisseurController {
    private FournisseurDao fournisseurDao;
    @GetMapping
    public HttpBodyResponse getAll(){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(fournisseurDao.findAll());
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
            bodyResponse.setResponse(fournisseurDao.findById(id).get());
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
    @PostMapping
    public HttpBodyResponse save(@RequestBody Fournisseur s){

        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(fournisseurDao.save(s));
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }


    @PutMapping
    public HttpBodyResponse update(@RequestBody Fournisseur s){

        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            Fournisseur f=fournisseurDao.findById(s.getId()).get();
            s.setPassword(f.getPassword());
            bodyResponse.setResponse(fournisseurDao.save(s));
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
    @PutMapping("/password")
    public HttpBodyResponse updatePassword(@RequestBody UpdatePasswordRequest s){

        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            Fournisseur f=fournisseurDao.findById(s.getUserId()).get();
            if(f.getPassword().equalsIgnoreCase(s.getAncien())){
                f.setPassword(s.getNouveau());
                bodyResponse.setResponse(fournisseurDao.save(f));
            }else {
                throw  new Exception("Password Invalid");
            }

        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }

    @DeleteMapping("/{id}")
    public HttpBodyResponse deleteOne(@PathVariable Integer id){

        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            fournisseurDao.deleteById(id);
            bodyResponse.setResponse(true);
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
}
