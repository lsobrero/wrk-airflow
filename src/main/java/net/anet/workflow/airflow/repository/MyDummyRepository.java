package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.MyDummy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MyDummy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyDummyRepository extends JpaRepository<MyDummy, Long> {

}
