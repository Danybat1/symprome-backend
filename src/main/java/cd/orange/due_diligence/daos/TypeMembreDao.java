package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.TypeMembre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeMembreDao extends JpaRepository<TypeMembre,Integer> {
}
