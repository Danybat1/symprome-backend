package cd.bnb.syprome.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String TYPE;
    private  String APPLINAME;
    private String REQSTATUS;
    private String CUID;
    private String FULLNAME;
    private String NAME;
    private String FIRSTNAME;
    private String DESCRIPTION;
    private String PHONENUMBER;
    private String EMAIL;
    private String MESSAGE;
}
