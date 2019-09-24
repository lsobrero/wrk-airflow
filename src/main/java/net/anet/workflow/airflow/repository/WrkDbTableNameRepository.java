package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.WrkDatabase;
import net.anet.workflow.airflow.domain.WrkDbTableName;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.BitSet;
import java.util.List;


/**
 * Spring Data  repository for the WrkDbTableName entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WrkDbTableNameRepository extends JpaRepository<WrkDbTableName, Long> {

    List<WrkDbTableName> findAllByDatabaseName(WrkDatabase db);
}
