package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentDao  extends JpaRepository<Document,Integer> {
}
