package cd.bnb.syprome.controllers;


import cd.bnb.syprome.daos.AgenceDao;
import cd.bnb.syprome.entities.Agence;
import cd.bnb.syprome.responses.HttpBodyResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/agence")
public class AgenceController {

    private AgenceDao agenceDao;
    @GetMapping
    public HttpBodyResponse getAll(){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(agenceDao.findAll());
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
            bodyResponse.setResponse(agenceDao.findById(id).get());
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
    @PostMapping
    public HttpBodyResponse save(@RequestBody Agence s){
       /*/ Role role=roleDao.findById(Utils.users.getId()).get();
        if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(agenceDao.save(s));
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }


    @PutMapping
    public HttpBodyResponse update(@RequestBody Agence s){
      /*  Role role=roleDao.findById(Utils.users.getId()).get();
        if(!role.getDescription().equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(agenceDao.save(s));
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
            agenceDao.deleteById(id);
            bodyResponse.setResponse(true);
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
}
