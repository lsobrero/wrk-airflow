package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.Anonimation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Anonimation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnonimationRepository extends JpaRepository<Anonimation, Long> {

}
