package net.anet.workflow.airflow.service.mapper;

import net.anet.workflow.airflow.domain.WrkDbColType;
import net.anet.workflow.airflow.service.dto.AfDbColTypeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AfDbColTypeMapper extends EntityMapper<AfDbColTypeDTO, WrkDbColType> {

    WrkDbColType toEntity(AfDbColTypeDTO dto);

    default WrkDbColType fromId(Long id){
        if (id == null) {
            return null;
        }
        WrkDbColType myDs=new WrkDbColType();
        myDs.setId(id);
        return myDs;
    }

}
