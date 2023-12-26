package cd.orange.due_diligence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "nom")
    public String noms;
    public String email;
    public String telephone;
    public String  domaine;
    public String experience;
    public String password;
    private Boolean status=true;
    private Date date=new Date();
}
