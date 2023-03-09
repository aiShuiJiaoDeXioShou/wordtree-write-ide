package lh.wordtree.dao.impl;

import cn.hutool.db.Entity;
import lh.wordtree.dao.AuthorTaskMapper;
import lh.wordtree.entity.AuthorTask;
import lh.wordtree.utils.DbUtils;

import java.sql.SQLException;
import java.util.List;

public class AuthorTaskMapperImpl implements AuthorTaskMapper {
    private final String tableName = "AuthorTask";

    @Override
    public int insertAuthorTask(AuthorTask authorTask) {
        try {
            return DbUtils.db().insert(new Entity().setTableName(tableName).parseBean(authorTask));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorTask> selectAll() {
        try {
            return DbUtils.paddingSetAll(DbUtils.db().findAll(Entity.create(tableName)), AuthorTask.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
