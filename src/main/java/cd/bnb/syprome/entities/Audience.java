package cd.bnb.syprome.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Audience {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String mo;
    private String sujet;
    private Integer delay;
    private Integer reply;
    private String path;
    private Integer step;
    private Boolean status;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date dateAudience;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date dateDem;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date date;
}
