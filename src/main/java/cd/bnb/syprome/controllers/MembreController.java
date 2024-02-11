package cd.bnb.syprome.controllers;


import cd.bnb.syprome.daos.MembreDao;
import cd.bnb.syprome.daos.TypeMembreDao;
import cd.bnb.syprome.entities.Membre;
import cd.bnb.syprome.responses.HttpBodyResponse;
import cd.bnb.syprome.services.FilesStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/membre")
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
                                             @RequestParam String dateNaissance,@RequestParam String etat,@RequestParam String adresse,@RequestParam String telEmployeur,@RequestParam String AdrEmployeur,
                                             @RequestParam String employeur,@RequestParam Double salaire,@RequestParam String soins,@RequestParam String frais,
                                             @RequestParam String telephone, @RequestParam("photo") MultipartFile photo){
        HttpBodyResponse bodyResponse=new HttpBodyResponse();
        try{
            Membre m=new Membre();
            m.setNom(nom);
            m.setPostnom(postnom);
            m.setPrenom(prenom);
            m.setEtat(etat);
            m.setSalaire(salaire);
            m.setSoins(soins);
            m.setFrais(frais);
            m.setAdresseEmployeur(AdrEmployeur);
            m.setTelEmployeur(telEmployeur);
            m.setProfession(profession);
            m.setGenre(genre);
            m.setLieu(lieu);
            m.setAdresse(adresse);
            m.setDateNaissance(dateNaissance);
            m.setEmployeur(employeur);
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

    @GetMapping("/image/{name}")
    public ResponseEntity<byte[]> fromDatabaseAsResEntity(@PathVariable String name)
            throws Exception {
        byte[] imageBytes = Files.readAllBytes(Paths.get("uploads/"+name));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
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
