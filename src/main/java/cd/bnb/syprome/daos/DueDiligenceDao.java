package cd.bnb.syprome.daos;


import cd.bnb.syprome.entities.DueDiligence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DueDiligenceDao extends JpaRepository<DueDiligence,Integer> {
    List findAllByFicOrderByIdDesc(Boolean b);
    List findAllByOmOrderByIdDesc(Boolean b);
    List findAllByStep(Integer i);
    List findAllByDemandeurIdOrderByIdDesc(Integer id);
    List findAllByOrigineOrderByIdDesc(String s);
    @Query("select u from DueDiligence u where lower(u.paths) like lower(concat('%', ?1,'%'))")
    DueDiligence findByPath(String path);
}
