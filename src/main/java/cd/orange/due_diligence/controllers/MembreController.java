package cd.orange.due_diligence.controllers;


import cd.orange.due_diligence.daos.MembreDao;
import cd.orange.due_diligence.daos.RoleDao;
import cd.orange.due_diligence.daos.TypeMembreDao;
import cd.orange.due_diligence.entities.DueDiligence;
import cd.orange.due_diligence.entities.Membre;
import cd.orange.due_diligence.entities.Role;
import cd.orange.due_diligence.entities.Users;
import cd.orange.due_diligence.responses.HttpBodyResponse;
import cd.orange.due_diligence.services.FilesStorageService;
import cd.orange.due_diligence.utilities.MailUtils;
import cd.orange.due_diligence.utilities.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/role")
public class MembreController {

    private MembreDao membreDao;
    private TypeMembreDao typeMembreDao;
    private FilesStorageService storageService;
    @GetMapping
    public HttpBodyResponse getAll(){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            bodyResponse.setResponse(membreDao.findAll());
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
            bodyResponse.setResponse(membreDao.findById(id).get());
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
    @PostMapping()
    public HttpBodyResponse save(@RequestParam String  nom,@RequestParam String  postnom,@RequestParam String  prenom,@RequestParam  String profession,
                                 @RequestParam String genre,
                                 @RequestParam String lieu,
                                             @RequestParam String dateNaissance,
                                             @RequestParam String employeur,
                                             @RequestParam String email,@RequestParam String telephone, @RequestParam("photo") MultipartFile photo){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            Membre m=new Membre();
            m.setNom(nom);
            m.setPostnom(postnom);
            m.setPrenom(prenom);
            m.setProfession(profession);
            m.setGenre(genre);
            m.setLieu(lieu);
            m.setDateNaissance(dateNaissance);
            m.setEmployeur(employeur);
            m.setEmail(email);
            m.setTelephone(telephone);
            String links="uploads/"+storageService.save(photo);
            m.setPhoto(links);
            membreDao.save(m);

        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");

            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
        }
    }



    @PutMapping()
    public HttpBodyResponse update(@RequestParam Integer  id,@RequestParam String  nom,@RequestParam String  postnom,@RequestParam String  prenom,@RequestParam  String profession,
                                 @RequestParam String genre,
                                 @RequestParam String lieu,
                                 @RequestParam String dateNaissance,
                                 @RequestParam String employeur,
                                 @RequestParam String email,@RequestParam String telephone, @RequestParam("photo") MultipartFile photo){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            Membre m=membreDao.findById(id).get();
            m.setNom(nom);
            m.setPostnom(postnom);
            m.setPrenom(prenom);
            m.setProfession(profession);
            m.setGenre(genre);
            m.setLieu(lieu);
            m.setDateNaissance(dateNaissance);
            m.setEmployeur(employeur);
            m.setEmail(email);
            m.setTelephone(telephone);
            String links="uploads/"+storageService.save(photo);
            m.setPhoto(links);
            membreDao.save(m);

        }catch (Exception e){
            e.printStackTrace();
            bodyResponse.setStatus("Failed");

            bodyResponse.setError(e.getMessage());
        }finally {
            return  bodyResponse;
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
            membreDao.deleteById(id);
            bodyResponse.setResponse(true);
        }catch (Exception e){
            bodyResponse.setError(e.getMessage());
            bodyResponse.setStatus("Failed");

        }finally {
            return bodyResponse;
        }
    }
}
