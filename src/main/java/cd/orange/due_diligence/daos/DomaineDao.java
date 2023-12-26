package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.Domaine;
import cd.orange.due_diligence.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomaineDao extends JpaRepository<Domaine,Integer> {
    Domaine findByDescription(String s);

}
