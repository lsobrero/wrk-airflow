package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.WrkAnonType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WrkAnonType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WrkAnonTypeRepository extends JpaRepository<WrkAnonType, Long> {

}
