package lenicorp.typeservices.controller.repositories;

import lenicorp.typeservices.model.entities.TypeParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TypeParamRepo extends JpaRepository<TypeParam, Long>
{
    @Query("select s from TypeParam s where upper(s.parent.uniqueCode) = upper(?1)")
    List<TypeParam> findByParent(String parentUniqueCode);

    @Query("select t from TypeParam t where t.parent.uniqueCode = ?1 and t.child.uniqueCode = ?2")
    TypeParam findByParentAndChild(String parentCode, String childCode);

    @Query("select (count(s) > 0) from TypeParam s where s.parent.uniqueCode = :parentCode and s.child.uniqueCode = :childCode")
    boolean alreadyExists(@Param("parentCode") String parentCode, @Param("childCode") String childCode);

    @Query("select (count(t) > 0) from TypeParam t where t.parent.uniqueCode = ?1 and t.child.uniqueCode = ?2 and t.active = true")
    boolean alreadyExistsAndActive(String parentCode, String childCode);

    @Query("select (count(t) > 0) from TypeParam t where t.parent.uniqueCode = ?1 and t.child.uniqueCode = ?2 and t.active = true")
    boolean alreadyExistsAndNotActive(String parentCode, String childCode);

    @Query("select (count(tp)> 0 ) from TypeParam tp where tp.parent.uniqueCode = ?1 and tp.child.uniqueCode = ?2 and tp.active = true")
    boolean parentHasDirectSousType(String parentCode, String childCode);

    @Modifying
    @Query("delete from TypeParam s where s.parent.uniqueCode = :parentCode and s.child.uniqueCode = :childCode")
    int removeSousType(@Param("parentCode") String parentCode, @Param("childCode") String childCode);
}