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
public class DueDiligence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String mo;
    private String origine;
    private String entite;
    private String objet;
    private Double valeur;
    private Boolean urgent=false;
    @Column(columnDefinition = "text")
    private String params;
    @Column(columnDefinition = "text")
    private String paths;
    @Column(columnDefinition = "text")
    private String params2;
    @Column(columnDefinition = "text")
    private String paths2;
    private Integer step;
    private String type;
    private String commentaire;
    private String commentaire2;
    private Boolean fic;
    private Boolean om;
    @ManyToOne
    @JoinColumn(name = "partenariat_id")
    private Partenariat partenariat;
    @ManyToOne
    @JoinColumn(name = "juriste_id")
    private Users juriste;
    @ManyToOne
    @JoinColumn(name = "validateur_id")
    private Users validateur;
    @ManyToOne
    @JoinColumn(name = "demandeur_id")
    private Users demandeur;
    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date dateDemande;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date echeance;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date echeance2;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date dateCloture;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date dateCloture2;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date date;
}
