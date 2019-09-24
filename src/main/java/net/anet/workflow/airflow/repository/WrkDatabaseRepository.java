package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.WrkDatabase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WrkDatabase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WrkDatabaseRepository extends JpaRepository<WrkDatabase, Long> {

}