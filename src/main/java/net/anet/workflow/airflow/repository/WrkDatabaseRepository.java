package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.WrkDatabase;
import net.anet.workflow.airflow.domain.WrkDataset;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the WrkDatabase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WrkDatabaseRepository extends JpaRepository<WrkDatabase, Long> {
    List<WrkDatabase> findByWrkDataSet(WrkDataset dataset);

}
