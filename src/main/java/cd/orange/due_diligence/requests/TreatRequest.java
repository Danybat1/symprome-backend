package cd.orange.due_diligence.requests;

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
