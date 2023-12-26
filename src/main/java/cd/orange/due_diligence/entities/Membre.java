package cd.orange.due_diligence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String dateNaissance;
    private String employeur;
    private String email;
    private String telephone;
    private String photo;
    private String status;
}
