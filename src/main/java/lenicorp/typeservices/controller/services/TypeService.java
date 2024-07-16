package lenicorp.typeservices.controller.services;

import lenicorp.typeservices.controller.exceptions.AppException;
import lenicorp.typeservices.controller.repositories.TypeParamRepo;
import lenicorp.typeservices.controller.repositories.TypeRepo;
import lenicorp.typeservices.model.dtos.*;
import lenicorp.typeservices.model.entities.Type;
import lenicorp.typeservices.model.entities.TypeParam;
import lenicorp.typeservices.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service @Transactional
@RequiredArgsConstructor
public class TypeService implements ITypeService
{
    private final TypeRepo typeRepo;
    private final TypeMapper typeMapper;
    private final TypeParamRepo typeParamRepo;
    @Override
    public Type createType(CreateTypeDTO dto) throws UnknownHostException {
        Type type = typeMapper.mapToType(dto);
        type = typeRepo.save(type);
        return type;
    }

    @Override @Transactional
    public Type updateType(UpdateTypeDTO dto) throws UnknownHostException {
        Type loadedType = typeRepo.findById(dto.getOldUniqueCode()).orElseThrow(()->new AppException("Type introuvable : " + dto.getOldUniqueCode()));

        loadedType.setTypeGroup(TypeGroup.valueOf(dto.getTypeGroup()));
        loadedType.setName(dto.getName().toUpperCase(Locale.ROOT));
        loadedType.setUniqueCode(dto.getUniqueCode().toUpperCase(Locale.ROOT));

        return loadedType;
    }

    @Override
    public void deleteType(String uniqueCode) {
        Type loadedType = uniqueCode == null ? null : typeRepo.findById(uniqueCode).orElse(null);
        if(loadedType == null || !loadedType.isActive()) return;
    }

    @Override
    public List<SelectOption> getTypeGroupOptions() {
        List<SelectOption> options = Arrays.stream(TypeGroup.values()).map(tg->new SelectOption(tg.name(), tg.getGroupName())).collect(Collectors.toList());
        return options;
    }

    @Override
    public boolean existsByName(String name, String uniqueCode)
    {
        if(name == null || name.equals("")) return false;
        if(uniqueCode == null || uniqueCode.equals("")) return typeRepo.existsByName(name);
        return typeRepo.existsByName(name, uniqueCode);
    }

    @Override
    public boolean typeGroupIsValid(String typeGroup)
    {
        if(typeGroup == null || typeGroup.trim().equals("")) return false;
        return TypeGroup.hasValue(typeGroup);
    }

    @Override
    public boolean existsByUniqueCode(String uniqueCode, String oldUniqueCode)
    {
        if(uniqueCode == null || uniqueCode.trim().equals("")) return false;
        if(oldUniqueCode==null || oldUniqueCode.trim().equals("")) return typeRepo.existsByUniqueCode(uniqueCode);
        return typeRepo.existsByUniqueCode(uniqueCode, oldUniqueCode);
    }

    @Override
    public List<SelectOption> getOptions(TypeGroup typeGroup)
    {
        return typeRepo.findOptionsByTypeGroup(typeGroup);
    }

    @Override @Transactional
    public void addSousType(TypeParamDTO dto) throws UnknownHostException {
        if(this.parentHasDistantSousType(dto.getChildCode(), dto.getParentCode())) return;
        if(typeParamRepo.alreadyExistsAndActive(dto.getParentCode(), dto.getChildCode())) return;
        if(typeParamRepo.alreadyExistsAndNotActive(dto.getParentCode(), dto.getChildCode()))
        {
            TypeParam typeParam = typeParamRepo.findByParentAndChild(dto.getParentCode(), dto.getChildCode());
            typeParam.setActive(true);
            return;
        }
        TypeParam typeParam = typeMapper.mapToTypeParam(dto);
        typeParam.setActive(true);
        typeParam = typeParamRepo.save(typeParam);
    }


    @Override @Transactional
    public void setSousTypes(TypeParamsDTO dto)
    {
        List<String> alreadyExistingSousTypeCodes = typeRepo.findChildrenCodes(dto.getParentCode());
        Set<String> newSousTypesToSetCodes = Arrays.stream(dto.getChildCodes()).filter(id0-> alreadyExistingSousTypeCodes.stream().noneMatch(id0::equals)).collect(Collectors.toSet());
        Set<String> sousTypesToRemoveCodes = alreadyExistingSousTypeCodes.stream().filter(id0-> Arrays.stream(dto.getChildCodes()).noneMatch(id0::equals)).collect(Collectors.toSet());

        newSousTypesToSetCodes.stream().map(code->new TypeParamDTO(code, dto.getParentCode())).forEach(o-> {
            try {
                this.addSousType(o);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        sousTypesToRemoveCodes.stream().map(id->new TypeParamDTO(id, dto.getParentCode())).forEach(o-> {
            try {
                this.removeSousType(o);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    @Override @Transactional
    public void removeSousType(TypeParamDTO dto) throws UnknownHostException {
        if(typeParamRepo.alreadyExistsAndNotActive(dto.getParentCode(), dto.getChildCode())) return;
        if(typeParamRepo.alreadyExistsAndActive(dto.getParentCode(), dto.getChildCode()))
        {
            TypeParam typeParam = typeParamRepo.findByParentAndChild(dto.getParentCode(), dto.getChildCode());
            typeParam.setActive(false);
            return;
        }
        TypeParam typeParam = typeMapper.mapToTypeParam(dto);
        typeParam.setActive(false);
        typeParam = typeParamRepo.save(typeParam);
    }

    @Override
    public boolean parentHasDirectSousType(String parentCode, String childCode)
    {
        return typeParamRepo.parentHasDirectSousType(parentCode, childCode);
    }

    @Override //Methode Récursive vérifie si le child est sous type du parent. puis pour chaque sous type parent, on rappelle la méthode ...
    public boolean parentHasDistantSousType(String parentCode, String childCode)
    {
        if(parentCode == null || childCode == null) return false;
        if(parentHasDirectSousType(parentCode, childCode)) return true;
        if(!typeRepo.existsById(parentCode) || !typeRepo.existsById(childCode)) return false;
        return typeRepo.findActiveSousTypes(parentCode).stream().anyMatch(st->parentHasDistantSousType(st.getUniqueCode(), childCode));
    }

    @Override //Tous les types du même typeGroupe dont le type du code passé en paramètre n'est pas un sous type distant
    public List<ReadTypeDTO> getPossibleSousTypes(String parentCode)
    {
        return typeRepo.findByTypeGroup(typeRepo.findTypeGroupByTypeCode(parentCode)).stream()
                .filter(t->!this.parentHasDistantSousType(t.getUniqueCode(), parentCode) && !t.getUniqueCode().equals(parentCode))
                .collect(Collectors.toList());
    }

    @Override
    public Type setSousTypesRecursively(String typeCode)
    {
        Type type = typeRepo.findById(typeCode).orElse(null);
        if(type == null) return null;
        List<Type> sousTypes = typeRepo.findActiveSousTypes(typeCode);
        type.setChildren(sousTypes);
        sousTypes.forEach(t->setSousTypesRecursively(t.getUniqueCode()));
        return type;
    }

    @Override
    public List<Type> getSousTypesRecursively(String uniqueCode)
    {
        Type type = typeRepo.findById(uniqueCode).orElse(null);
        if(type == null) return null;
        return typeRepo.findActiveSousTypes(uniqueCode).stream().flatMap(t-> Stream.concat(Stream.of(t), getSousTypesRecursively(t.getUniqueCode()).stream())).collect(Collectors.toList());
    }

    @Override
    public List<TypeGroup> getTypeGroups() {
        return Arrays.asList(TypeGroup.values());
    }

    @Override
    public Page<ReadTypeDTO> searchPageOfTypes(String key, List<String> typeGroups, int pageNum, int pageSize)
    {
        List<TypeGroup> baseTypeGroups = typeGroups == null || typeGroups.isEmpty() ?
                Arrays.stream(TypeGroup.values()).toList() :
                typeGroups.stream().filter(tg-> TypeGroup.hasValue(tg)).map(tg->TypeGroup.valueOf(tg)).collect(Collectors.toList());

        return typeRepo.searchPageOfTypes(key, baseTypeGroups, PageRequest.of(pageNum, pageSize));
    }
}