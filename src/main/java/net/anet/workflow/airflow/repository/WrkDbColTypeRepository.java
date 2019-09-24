package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.WrkDbColType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WrkDbColType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WrkDbColTypeRepository extends JpaRepository<WrkDbColType, Long> {

}
