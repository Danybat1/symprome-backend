package cd.bnb.syprome.daos;


import cd.bnb.syprome.entities.TypePaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypePaiementDao extends JpaRepository<TypePaiement,Integer> {
}
