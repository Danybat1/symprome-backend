package cd.bnb.syprome.daos;

import cd.bnb.syprome.entities.Centre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentreDao extends JpaRepository<Centre,Integer> {
}
