package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.WrkDatabase;
import net.anet.workflow.airflow.domain.WrkDbColName;
import net.anet.workflow.airflow.domain.WrkDbTableName;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the WrkDbColName entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WrkDbColNameRepository extends JpaRepository<WrkDbColName, Long> {

    List<WrkDbColName> findByDbName(WrkDbTableName db);
    @Query(value="select col.col_type_id from WRK_DB_COL_NAME col where col.id=(:id)" , nativeQuery = true)
    Long getColTypeIdWhereId(@Param("id") Long id);
}
