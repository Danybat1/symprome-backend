package cd.bnb.syprome.controllers;


import cd.bnb.syprome.daos.FormationDao;
import cd.bnb.syprome.entities.Formation;
import cd.bnb.syprome.responses.HttpBodyResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/formation")
public class FormationController {

    private FormationDao formationDao;
    @GetMapping
    public HttpBodyResponse getAll(){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(formationDao.findAll());
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
            bodyResponse.setResponse(formationDao.findById(id).get());
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
    @PostMapping
    public HttpBodyResponse save(@RequestBody Formation s){
       /*/ Role role=roleDao.findById(Utils.users.getId()).get();
        if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(formationDao.save(s));
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }


    @PutMapping
    public HttpBodyResponse update(@RequestBody Formation s){
      /*  Role role=roleDao.findById(Utils.users.getId()).get();
        if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(formationDao.save(s));
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
            formationDao.deleteById(id);
            bodyResponse.setResponse(true);
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
}
