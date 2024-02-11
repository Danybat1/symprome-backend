package cd.bnb.syprome.daos;

import cd.bnb.syprome.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role,Integer> {
    Role findByDescription(String s);

}
