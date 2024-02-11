package cd.bnb.syprome.daos;

import cd.bnb.syprome.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDao  extends JpaRepository<Users,Integer> {
    Users findByUsername(String cuid);
    Users findByEmail(String email);


}
