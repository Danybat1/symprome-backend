package cd.orange.due_diligence.daos;


import cd.orange.due_diligence.entities.TypePaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypePaiementDao extends JpaRepository<TypePaiement,Integer> {
}
