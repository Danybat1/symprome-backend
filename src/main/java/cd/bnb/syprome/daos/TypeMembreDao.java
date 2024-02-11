package cd.bnb.syprome.daos;

import cd.bnb.syprome.entities.TypeMembre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeMembreDao extends JpaRepository<TypeMembre,Integer> {
}
