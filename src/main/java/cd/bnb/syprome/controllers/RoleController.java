package cd.bnb.syprome.controllers;


import cd.bnb.syprome.daos.RoleDao;
import cd.bnb.syprome.entities.Role;
import cd.bnb.syprome.responses.HttpBodyResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    private RoleDao roleDao;
    @GetMapping
    public HttpBodyResponse getAll(){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(roleDao.findAll());
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
            bodyResponse.setResponse(roleDao.findById(id).get());
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
    @PostMapping
    public HttpBodyResponse save(@RequestBody Role s){
       /*/ Role role=roleDao.findById(Utils.users.getId()).get();
        if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(roleDao.save(s));
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }


    @PutMapping
    public HttpBodyResponse update(@RequestBody Role s){
      /*  Role role=roleDao.findById(Utils.users.getId()).get();
        if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(roleDao.save(s));
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
            roleDao.deleteById(id);
            bodyResponse.setResponse(true);
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
}
