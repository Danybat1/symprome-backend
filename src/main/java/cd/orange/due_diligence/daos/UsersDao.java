package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDao  extends JpaRepository<Users,Integer> {
    Users findByCuid(String cuid);
    Users findByEmail(String email);


}
