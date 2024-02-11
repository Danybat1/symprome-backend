package cd.bnb.syprome.services;


import cd.bnb.syprome.daos.FournisseurDao;
import cd.bnb.syprome.daos.RoleDao;
import cd.bnb.syprome.daos.UsersDao;
import cd.bnb.syprome.responses.UserResponse;
import cd.bnb.syprome.utilities.JwtUtility;
import cd.bnb.syprome.entities.Users;
import cd.bnb.syprome.requests.UserRequest;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class AuthentificationService {

  AuthenticationManager authenticationManager;

  UserJwtService userService;

  JwtUtility jwtUtility;
  RoleDao roleDao;
  private UsersDao usersDao;
  private FournisseurDao fournisseurDao;

    public Users login(String cuid , String password) throws Exception {

      String xmlString = "<COMMAND>\n" +
              "    <TYPE>AUTH_SVC</TYPE>\n" +
              "    <APPLINAME>Due-diligence</APPLINAME>\n" +
              "    <CUID>"+cuid+"</CUID>\n" +
              "    <PASSWORD>"+password+"</PASSWORD>\n" +
              "    <DATE>"+new Date()+"</DATE>\n" +
              "</COMMAND>\"\n";

      RestTemplate restTemplate =  new RestTemplate();
      //Create a list for the message converters
      List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
      //Add the String Message converter
      messageConverters.add(new StringHttpMessageConverter());
      //Add the message converters to the restTemplate
      restTemplate.setMessageConverters(messageConverters);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_XML);
      HttpEntity<String> request = new HttpEntity<String>(xmlString, headers);
      final ResponseEntity<String> response = restTemplate.postForEntity("http://10.25.2.25:8080/ldap/", request, String.class);
      System.out.println(response.getBody());
      JSONObject jsonObject = XML.toJSONObject((response.getBody()));
      System.out.println(jsonObject);
      Gson gson=new Gson();
      UserResponse userResponse= gson.fromJson(jsonObject.getJSONObject("COMMAND").toString(), UserResponse.class);
      if(userResponse.getEMAIL()==null||userResponse.getEMAIL().equals(""))
      {
        throw  new Exception("User Not found");
      }
      Users u=usersDao.findByEmail(userResponse.getEMAIL());
      if(u==null){
        throw  new Exception("User Not found");
     }
      return  u;
    }

    public Users register(UserRequest request){
      Users s=usersDao.findByEmail(request.getEmail());
      if(s==null) {
        s = new Users();
        s.setNoms(request.getNoms());
        s.setUsername(request.getUsername());
        s.setEmail(request.getEmail());
        s.setDate(new Date());
        s.setTelephone(request.getTelephone());
        s.setRole(roleDao.findByDescription("user"));
        s.setStatus(false);
        usersDao.save(s);
        return s;
      }else
        return s;
    }

}
