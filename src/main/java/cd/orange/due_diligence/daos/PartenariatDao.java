package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.Partenariat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartenariatDao extends JpaRepository<Partenariat,Integer> {
}
