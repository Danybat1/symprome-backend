package cd.bnb.syprome.responses;

import lombok.Data;

@Data
public class HttpBodyResponse {
    private String status;
    private String error;
    private Object response;

    public HttpBodyResponse(){
        status="Success";
    }

}
