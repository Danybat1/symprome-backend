package cd.orange.due_diligence.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePasswordRequest {
    private Integer userId;
    private String ancien;
    private String nouveau;
}
