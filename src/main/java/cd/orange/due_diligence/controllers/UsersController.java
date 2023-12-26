package cd.orange.due_diligence.controllers;

import cd.orange.due_diligence.daos.RoleDao;
import cd.orange.due_diligence.daos.UsersDao;
import cd.orange.due_diligence.entities.Users;
import cd.orange.due_diligence.requests.UserRequest;
import cd.orange.due_diligence.responses.HttpBodyResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user")
public class UsersController {
    private RoleDao roleDao;
    private UsersDao userDao;
    @GetMapping
    public HttpBodyResponse getAll(){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(userDao.findAll());
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
            bodyResponse.setResponse(userDao.findById(id).get());
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
    @PostMapping
    public HttpBodyResponse save(@RequestBody UserRequest request){
        //Role role=Utils.users.getRole();
        log.info("request body : {}",request);
        Users s=userDao.findByEmail(request.getEmail());
        if(s==null) {
            s = new Users();
            s.setNoms(request.getNoms());
            s.setUsername(request.getCuid());
            s.setEmail(request.getEmail());
            s.setDate(new Date());
            s.setTelephone(request.getTelephone());
            s.setRole(roleDao.findById(request.getRole()).get());
        }
       /* if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            s.setStatus(false);
            bodyResponse.setResponse(userDao.save(s));
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }


    @PutMapping
    public HttpBodyResponse update(@RequestBody UserRequest request){
        log.info("request body : {}",request);
       // Role role=Utils.users.getRole();
        log.info("{}",request);
        Users s=userDao.findById(request.getId()).get();
        if(s!=null){
            s.setNoms(request.getNoms());
            s.setUsername(request.getCuid());
            s.setEmail(request.getEmail());
            s.setTelephone(request.getTelephone());
            s.setRole(roleDao.findById(request.getRole()).get());
        }
      /*  if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(userDao.save(s));
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }

    @DeleteMapping("/{id}")
    public HttpBodyResponse deleteOne(@PathVariable Integer id){
       // Role role=Utils.users.getRole();
       /* if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } */
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            userDao.deleteById(id);
            bodyResponse.setResponse(true);
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
}
