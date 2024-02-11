package cd.bnb.syprome.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashRequest {
    private Integer id;
    private String titre;
    private String contenu;
    private String debut;
    private String fin;
}
