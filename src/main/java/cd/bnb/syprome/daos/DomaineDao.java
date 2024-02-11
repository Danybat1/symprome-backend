package cd.bnb.syprome.daos;

import cd.bnb.syprome.entities.Domaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomaineDao extends JpaRepository<Domaine,Integer> {
    Domaine findByDescription(String s);

}
