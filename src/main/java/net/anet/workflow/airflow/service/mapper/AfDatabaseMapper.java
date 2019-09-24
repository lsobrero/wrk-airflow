package net.anet.workflow.airflow.service.mapper;

import net.anet.workflow.airflow.domain.WrkDatabase;
import net.anet.workflow.airflow.domain.WrkDataset;
import net.anet.workflow.airflow.service.dto.AfDatabaseDTO;
import net.anet.workflow.airflow.service.dto.AfDatasetDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AfDatabaseMapper extends EntityMapper<AfDatabaseDTO, WrkDatabase> {

    WrkDatabase toEntity(AfDatabaseDTO dto);

    default WrkDatabase fromId(Long id){
        if (id == null) {
            return null;
        }
        WrkDatabase myDs=new WrkDatabase();
        myDs.setId(id);
        return myDs;
    }

}
