package net.anet.workflow.airflow.service.mapper;

import net.anet.workflow.airflow.domain.WrkDbColName;
import net.anet.workflow.airflow.domain.WrkDbTableName;
import net.anet.workflow.airflow.service.dto.AfDbColNameDTO;
import net.anet.workflow.airflow.service.dto.AfDbTableNameDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AfDbTableNameMapper extends EntityMapper<AfDbTableNameDTO, WrkDbTableName> {

    WrkDbTableName toEntity(AfDbTableNameDTO dto);

    default WrkDbTableName fromId(Long id){
        if (id == null) {
            return null;
        }
        WrkDbTableName myDs=new WrkDbTableName();
        myDs.setId(id);
        return myDs;
    }

}
