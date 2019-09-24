package net.anet.workflow.airflow.service.mapper;

import net.anet.workflow.airflow.domain.*;
import net.anet.workflow.airflow.service.dto.MyDummyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MyDummy} and its DTO {@link MyDummyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MyDummyMapper extends EntityMapper<MyDummyDTO, MyDummy> {


    @Mapping(target = "colNames", ignore = true)
    @Mapping(target = "removeColName", ignore = true)
    MyDummy toEntity(MyDummyDTO myDummyDTO);

    default MyDummy fromId(Long id) {
        if (id == null) {
            return null;
        }
        MyDummy myDummy = new MyDummy();
        myDummy.setId(id);
        return myDummy;
    }
}
