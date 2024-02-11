package cd.bnb.syprome.daos;

import cd.bnb.syprome.entities.Audience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudienceDao extends JpaRepository<Audience,Integer> {
}
