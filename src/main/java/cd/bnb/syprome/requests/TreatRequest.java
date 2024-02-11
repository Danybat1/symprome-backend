package cd.bnb.syprome.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TreatRequest {
    private Integer idUser;
    private Integer idDue;
    private Integer service;
    private String commentaire;

}
