package net.anet.workflow.airflow.service.mapper;

import net.anet.workflow.airflow.domain.WrkDataset;
import net.anet.workflow.airflow.service.dto.AfDatasetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface AfDatasetMapper extends EntityMapper<AfDatasetDTO, WrkDataset> {

//    @Mapping(target = "colNames", ignore = true)
    WrkDataset toEntity(AfDatasetDTO dto);

    default WrkDataset fromId(Long id){
        if (id == null) {
            return null;
        }
        WrkDataset myDs=new WrkDataset();
        myDs.setId(id);
        return myDs;
    }

}
