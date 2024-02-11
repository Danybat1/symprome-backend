package cd.bnb.syprome.daos;

import cd.bnb.syprome.entities.Partenariat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartenariatDao extends JpaRepository<Partenariat,Integer> {
}
