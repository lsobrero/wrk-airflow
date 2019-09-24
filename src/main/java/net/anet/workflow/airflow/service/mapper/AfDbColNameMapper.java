package net.anet.workflow.airflow.service.mapper;

import net.anet.workflow.airflow.domain.WrkDbColName;
import net.anet.workflow.airflow.service.dto.AfDbColNameDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AfDbColNameMapper extends EntityMapper<AfDbColNameDTO, WrkDbColName> {

    WrkDbColName toEntity(AfDbColNameDTO dto);

    default WrkDbColName fromId(Long id){
        if (id == null) {
            return null;
        }
        WrkDbColName myDs=new WrkDbColName();
        myDs.setId(id);
        return myDs;
    }

}
