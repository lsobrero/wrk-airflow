package net.anet.workflow.airflow.service.mapper;

import net.anet.workflow.airflow.domain.WrkAnonType;
import net.anet.workflow.airflow.domain.WrkDbColType;
import net.anet.workflow.airflow.service.dto.AfAnonTypeDTO;
import net.anet.workflow.airflow.service.dto.AfDbColTypeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AfAnonTypeMapper extends EntityMapper<AfAnonTypeDTO, WrkAnonType> {

    WrkAnonType toEntity(AfAnonTypeDTO dto);

    default WrkAnonType fromId(Long id){
        if (id == null) {
            return null;
        }
        WrkAnonType myDs=new WrkAnonType();
        myDs.setId(id);
        return myDs;
    }

}
