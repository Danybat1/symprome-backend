package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembreDao extends JpaRepository<Membre,Integer> {
}
