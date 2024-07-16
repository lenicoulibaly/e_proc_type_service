package lenicorp.typeservices.controller.repositories;

import lenicorp.typeservices.model.dtos.ReadTypeDTO;
import lenicorp.typeservices.model.dtos.SelectOption;
import lenicorp.typeservices.model.entities.Type;
import lenicorp.typeservices.model.enums.TypeGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TypeRepo extends JpaRepository<Type, String>
{
    @Query("select t from Type t where t.active = true")
    List<Type> findActiveTypes();

    @Query("select t.typeGroup from Type t where t.uniqueCode = ?1")
    TypeGroup findTypeGroupByTypeCode(String typeCode);
    

    @Query("select new lenicorp.typeservices.model.dtos.ReadTypeDTO(t.uniqueCode, t.typeGroup, t.name, t.active, t.objectFolder) from Type t where t.typeGroup = ?1 and t.active = true")
    List<ReadTypeDTO> findByTypeGroup(TypeGroup typeGroup);

    @Query("select t.uniqueCode from Type t where t.typeGroup = ?1 and t.active=true order by t.name")
    List<String> findTypeCodesByTypeGroup(TypeGroup typeGroup);

    @Query("select new lenicorp.typeservices.model.dtos.ReadTypeDTO( t.uniqueCode, t.typeGroup, t.name, t.active) from Type t order by t.typeGroup, t.uniqueCode, t.objectFolder")
    List<ReadTypeDTO> findAllTypes();

    @Query("select new lenicorp.typeservices.model.dtos.ReadTypeDTO(s.child.uniqueCode, s.child.typeGroup, s.child.name, s.child.active) from TypeParam s where s.parent.uniqueCode = ?1 order by s.child.name")
    List<ReadTypeDTO> findSousTypeOf(String uniqueCode);

    @Query("select tp.child from TypeParam  tp where tp.parent.uniqueCode = ?1 and tp.active = true and tp.child.active = true")
    List<Type> findActiveSousTypes(String parentCode);

    @Query("select tp.child.uniqueCode from TypeParam  tp where tp.parent.uniqueCode = ?1 and tp.child.active = true and tp.active = true")
    List<String> findChildrenCodes(String parentCode);

    @Query("select (count (stp)>0) from TypeParam stp where stp.child.uniqueCode=?2 and stp.parent.uniqueCode=?1")
    boolean isSousTypeOf(String parentCode, String childCode);

    @Query("select (count(t) > 0) from Type t where upper(t.uniqueCode) = upper(?1)")
    boolean existsByUniqueCode(String uniqueCode);

    @Query("select (count(t) > 0) from Type t where t.typeGroup = ?1 and upper(t.uniqueCode) = upper(?2)")
    boolean existsByGroupAndUniqueCode(TypeGroup valueOf, String uniqueCode);

    @Query("select (count(stp) = 0) from TypeParam stp where stp.child.uniqueCode = ?1 or stp.parent.uniqueCode = ?1")
    boolean isDeletable(String uniqueCode);

    @Modifying
    @Query("delete from Type t where t.uniqueCode = ?1")
    long deleteByUniqueCode(String uniqueCode);

    @Modifying
    @Query("update Type t set t.typeGroup = :typeGroup, t.name = :name where t.uniqueCode =:uniqueCode")
    long updateType(@Param("uniqueCode") String uniqueCode, @Param("typeGroup") String typeGroup, @Param("name") String name);

    @Query("select (count(t)>0) from Type t where t.typeGroup = ?1 and t.uniqueCode = ?2 and t.active=true")
    boolean typeGroupHasChild(TypeGroup typeGroup, String uniqueCode);

    @Query("select t.objectFolder from Type t where t.uniqueCode = ?1")
    String getObjectFolderByUniqueCode(String uniqueCode);

    @Query("select t.uniqueCode from Type t where t.objectFolder = ?1")
    List<String> findUniqueCodesByObjectFolder(String objectFolder);

    @Query("select (count(t.typeParamId)>0) from TypeParam t where t.child.uniqueCode = ?1")
    boolean typeHasAnyParent(String uniqueCode);

    @Query("select (count(t.typeParamId)>0) from TypeParam t where t.child.uniqueCode = ?1 and t.child.typeGroup = ?2")
    boolean typeHasAnyParent(String uniqueCode, TypeGroup typeGroup);

    @Query("select t from Type t where t.typeGroup = ?1 and t.active=true and (select count(tp.typeParamId) from TypeParam tp where tp.child.uniqueCode = t.uniqueCode and tp.active = true) = 0")
    List<Type> findBaseTypes(TypeGroup typeGroup);

    @Query("select tp.child from TypeParam tp where tp.parent.uniqueCode = ?1 and tp.active = true")
    List<Type> getChildren(String uniqueCode);


    @Query("select (count(t.uniqueCode)>0) from Type t where t.name = ?1")
    boolean existsByName(String name);

    @Query("select (count(t.uniqueCode)>0) from Type t where t.name = ?1 and t.uniqueCode <> ?2")
    boolean existsByName(String name, String uniqueCode);

    @Query("select (count(t.uniqueCode)>0) from Type t where t.uniqueCode = ?1 and exists (select t1 from Type t1 where t1.uniqueCode = ?1 and t1.uniqueCode <> ?2)")
    boolean existsByUniqueCode(String uniqueCode, String oldUniqueCode);

    @Query("select new lenicorp.typeservices.model.dtos.SelectOption(t.uniqueCode, t.name) from Type t where t.typeGroup = ?1 and t.active=true")
    List<SelectOption> findOptionsByTypeGroup(TypeGroup typeGroup);

    @Query("""
        select new lenicorp.typeservices.model.dtos.ReadTypeDTO(t.uniqueCode, t.typeGroup, t.name, t.active) 
        from Type t 
        where (
        locate(upper(coalesce(?1, '') ), upper(cast(function('unaccent',  coalesce(t.uniqueCode, '') ) as string))) >0 or
        locate(upper(coalesce(?1, '') ), upper(cast(function('unaccent',  coalesce(t.name, '') ) as string))) >0 ) and
        t.active = true and t.typeGroup in ?2
""")
    Page<ReadTypeDTO> searchPageOfTypes(String key, List<TypeGroup> baseTypeGroups, PageRequest of);
}
