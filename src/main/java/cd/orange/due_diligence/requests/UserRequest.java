package cd.orange.due_diligence.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class UserRequest {
    private Integer id;
    private String email;
    private String noms;
    private String cuid;
    private String telephone;
    private Integer role;
}
