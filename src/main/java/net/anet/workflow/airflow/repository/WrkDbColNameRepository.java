package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.WrkDbColName;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WrkDbColName entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WrkDbColNameRepository extends JpaRepository<WrkDbColName, Long> {

}
