package net.anet.workflow.airflow.repository;
import net.anet.workflow.airflow.domain.MyTest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MyTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyTestRepository extends JpaRepository<MyTest, Long> {

}
