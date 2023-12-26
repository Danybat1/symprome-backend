package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormationDao extends JpaRepository<Formation,Integer> {
}
