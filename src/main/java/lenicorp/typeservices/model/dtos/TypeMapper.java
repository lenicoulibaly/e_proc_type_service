package lenicorp.typeservices.model.dtos;

import lenicorp.typeservices.model.entities.Type;
import lenicorp.typeservices.model.entities.TypeParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TypeMapper
{
    @Mapping(target = "active", expression = "java(true)")
    @Mapping(target = "typeGroup", expression = "java(lenicorp.typeservices.model.enums.TypeGroup.valueOf(dto.getTypeGroup()))")
    @Mapping(target = "uniqueCode", expression = "java(dto.getUniqueCode().toUpperCase())")
    @Mapping(target = "name", expression = "java(dto.getName().toUpperCase())")
    Type mapToType(CreateTypeDTO dto);

    @Mapping(target = "typeGroup", expression = "java(type.getTypeGroup().getGroupName())")
    ReadTypeDTO mapToReadTypeDTO(Type type);
    @Mapping(target = "parent.uniqueCode", source = "dto.parentCode")
    @Mapping(target = "child.uniqueCode", source = "dto.childCode")
    TypeParam mapToTypeParam(TypeParamDTO dto);
}