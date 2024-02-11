package cd.bnb.syprome.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Membre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nom;
    private String prenom;
    private String postnom;
    private Integer step;
    private String profession;
    private String genre;
    private String lieu;
    private String etat;
    private String dateNaissance;
    private String telEmployeur;
    private  String adrEmployeur;
    private  Double salaire;
    private String adresse;
    private String soins;
    private String frais;
    private String adresseEmployeur;
    private String employeur;
    private String email;
    private String telephone;
    private String photo;
    private String status;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date dateCreat;
}
