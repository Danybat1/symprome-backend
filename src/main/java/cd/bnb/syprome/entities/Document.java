package cd.bnb.syprome.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nom;
    private String prenom;
    private String postnom;
    private String profession;
    private String employeur;
    private String email;
    private String telephone;
    private String status;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date date;
}
