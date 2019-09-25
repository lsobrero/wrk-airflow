package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.WrkDataset;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the WrkDataset entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WrkDatasetRepository extends JpaRepository<WrkDataset, Long> {
    Optional<WrkDataset> findByName(String name);

}
