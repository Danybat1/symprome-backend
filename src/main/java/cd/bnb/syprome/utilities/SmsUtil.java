package cd.bnb.syprome.utilities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsUtil {

    private String from;
    private List<String> to;
    private String message;
}
