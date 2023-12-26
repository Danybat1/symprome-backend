package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role,Integer> {
    Role findByDescription(String s);

}
