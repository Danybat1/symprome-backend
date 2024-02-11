package cd.bnb.syprome.daos;

import cd.bnb.syprome.entities.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenceDao extends JpaRepository<Agence,Integer> {
}
