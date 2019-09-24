package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.WrkDbColType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WrkDbColType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WrkDbColTypeRepository extends JpaRepository<WrkDbColType, Long> {
    @Query(value="select col.anon_type_id from WRK_DB_COL_TYPE col where col.id=(:id)" , nativeQuery = true)
    Long getAnonTypeIdWhereId(@Param("id") Long id);
}
