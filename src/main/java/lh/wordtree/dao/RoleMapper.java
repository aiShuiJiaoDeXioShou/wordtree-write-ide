package lh.wordtree.dao;

import lh.wordtree.entity.Role;
import org.apache.ibatis.annotations.Select;

public interface RoleMapper {
    @Select("select name,age from role where name = #{name}")
    Role selectOne(String name);
}
