package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.Audience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudienceDao extends JpaRepository<Audience,Integer> {
}
