package cd.bnb.syprome.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorieRequest {
    private Integer id;
    private String description;
    private Integer menu;
}
