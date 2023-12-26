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
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String noms;
    private String numero;
    private Double montant;
    private String telephone;
    private String devise;
    @ManyToOne
    @JoinColumn(name = "type_paiement_id")
    private TypePaiement typePaiement;
    @ManyToOne
    @JoinColumn(name = "percepteur_id")
    private Users percepteur;
    @ManyToOne
    @JoinColumn(name = "membre_id")
    private Membre membre;
    private Date dateCreat;
}
