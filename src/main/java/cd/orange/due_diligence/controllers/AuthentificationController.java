package cd.orange.due_diligence.controllers;

import cd.orange.due_diligence.daos.FournisseurDao;
import cd.orange.due_diligence.entities.Fournisseur;
import cd.orange.due_diligence.requests.LoginRequest;
import cd.orange.due_diligence.requests.UserRequest;
import cd.orange.due_diligence.responses.HttpBodyResponse;
import cd.orange.due_diligence.services.AuthentificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
@RestController
public class AuthentificationController {
    private AuthentificationService service;
    private FournisseurDao fournisseurDao;



    @PostMapping("/register")
    public HttpBodyResponse register(@RequestBody UserRequest request){

        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            log.info("la request {}",request);
            bodyResponse.setResponse(service.register(request));
        }catch (Exception e){
            log.error(e.getMessage());
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }

    @PostMapping("/fournisseur/register")
    public HttpBodyResponse register(@RequestBody Fournisseur s){

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
    @PostMapping
    public HttpBodyResponse login(@RequestBody LoginRequest request){
         log.info("{}",request);
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            if(request.getCuid().contains("@")) {
                Fournisseur f = fournisseurDao.findByEmailAndPassword(request.getCuid(), request.getPassword());
                f.setPassword("*****************");
                bodyResponse.setResponse(f);
            }else
                bodyResponse.setResponse( service.login(request.getCuid(), request.getPassword()));
        }catch (Exception e){
            log.error(e.getMessage());
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }


}
