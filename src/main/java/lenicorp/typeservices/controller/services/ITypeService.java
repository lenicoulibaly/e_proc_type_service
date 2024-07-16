package lenicorp.typeservices.controller.services;


import lenicorp.typeservices.model.dtos.*;
import lenicorp.typeservices.model.entities.Type;
import lenicorp.typeservices.model.enums.TypeGroup;
import org.springframework.data.domain.Page;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public interface ITypeService
{
    Type createType(CreateTypeDTO dto) throws UnknownHostException;
    Type updateType(UpdateTypeDTO dto) throws UnknownHostException;
    void deleteType(String typeCode) throws UnknownHostException;
    void addSousType(TypeParamDTO dto) throws UnknownHostException;
    void removeSousType(TypeParamDTO dto) throws UnknownHostException;
    void setSousTypes(TypeParamsDTO dto) throws UnknownHostException;
    boolean parentHasDirectSousType(String parentCode, String childCode);
    boolean parentHasDistantSousType(String parentCode, String childCode);

    List<ReadTypeDTO> getPossibleSousTypes(String parentId);

    Type setSousTypesRecursively(String typeCode);
    List<Type> getSousTypesRecursively(String typeCode);
    List<TypeGroup> getTypeGroups();

    List<SelectOption> getTypeGroupOptions();

    boolean existsByName(String name, String uniqueCode);

    boolean typeGroupIsValid(String typeGroup);

    boolean existsByUniqueCode(String uniqueCode, String oldUniqueCode);

    List<SelectOption> getOptions(TypeGroup typeFrequence);

    Page<ReadTypeDTO> searchPageOfTypes(String key, List<String> typeGroups, int page, int size);
}
