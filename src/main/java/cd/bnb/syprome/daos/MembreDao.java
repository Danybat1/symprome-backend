package cd.bnb.syprome.daos;

import cd.bnb.syprome.entities.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembreDao extends JpaRepository<Membre,Integer> {
}
