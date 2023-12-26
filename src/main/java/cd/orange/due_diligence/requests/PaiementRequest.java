package cd.orange.due_diligence.requests;

import lombok.Data;
import java.util.Date;


@Data
public class PaiementRequest {
    private Integer id;
    private String noms;
    private String numero;
    private Double montant;
    private String telephone;
    private String devise;
    private Integer typePaiement;
    private Integer percepteur;
    private Integer membre;
    private Date dateCreat;
}
