package com.yangteng.library.views.notebook.dao;

import com.yangteng.library.views.notebook.entity.Role;
import org.apache.ibatis.annotations.Select;

public interface RoleDao {
    @Select("select name,age from role where name = #{name}")
    Role selectOne(String name);
}
