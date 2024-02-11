package cd.bnb.syprome.daos;

import cd.bnb.syprome.entities.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormationDao extends JpaRepository<Formation,Integer> {
}
