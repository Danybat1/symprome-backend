package cd.orange.due_diligence.daos;

import cd.orange.due_diligence.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaiementDao extends JpaRepository<Paiement,Integer> {
    List<Paiement>findAllByTypePaiementId(Integer id);
    @Query(value = "from Paiement t where dateCreat BETWEEN :startDate AND :endDate")
    List<Paiement> getAllBetweenDates(@Param("startDate") Date startDate, @Param("endDate")Date endDate);
    long count();
}
