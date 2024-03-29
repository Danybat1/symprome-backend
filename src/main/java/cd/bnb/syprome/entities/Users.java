package cd.bnb.syprome.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String noms;
    private String username;
    private String email;
    private String password;
    private String telephone;
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date date;

}
