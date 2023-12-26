package cd.orange.due_diligence.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DalValidRequest {
    private Integer idDue;
    private Integer idUser;
    private Integer urgent;
}
