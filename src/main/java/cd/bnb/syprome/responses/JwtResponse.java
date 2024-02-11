package cd.bnb.syprome.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse
{
    private Integer expire_in;
    private  String access_token;
    private String role;
}
