package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FournisseurDao extends JpaRepository<Fournisseur,Integer> {
 Fournisseur findByEmailAndPassword(String s, String o);
 Fournisseur findByEmail(String s);
}
