package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.Agence;
import cd.orange.due_diligence.entities.Centre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenceDao extends JpaRepository<Agence,Integer> {
}
