package net.anet.workflow.airflow.service.mapper;

import net.anet.workflow.airflow.domain.*;
import net.anet.workflow.airflow.service.dto.MyTestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MyTest} and its DTO {@link MyTestDTO}.
 */
@Mapper(componentModel = "spring", uses = {MyDummyMapper.class})
public interface MyTestMapper extends EntityMapper<MyTestDTO, MyTest> {

    @Mapping(source = "colType.id", target = "colTypeId")
    @Mapping(source = "colType.name", target = "colTypeName")
    MyTestDTO toDto(MyTest myTest);

    @Mapping(source = "colTypeId", target = "colType")
    MyTest toEntity(MyTestDTO myTestDTO);

    default MyTest fromId(Long id) {
        if (id == null) {
            return null;
        }
        MyTest myTest = new MyTest();
        myTest.setId(id);
        return myTest;
    }
}
