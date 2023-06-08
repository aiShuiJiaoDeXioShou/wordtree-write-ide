package lh.wordtree.archive.mapper.impl;

import cn.hutool.db.Entity;
import lh.wordtree.archive.mapper.AuthorTaskMapper;
import lh.wordtree.archive.entity.AuthorTask;
import lh.wordtree.comm.Db;

import java.sql.SQLException;
import java.util.List;

public class AuthorTaskMapperImpl implements AuthorTaskMapper {
    private final String tableName = "AuthorTask";

    @Override
    public int insertAuthorTask(AuthorTask authorTask) {
        try {
            return Db.db().insert(new Entity().setTableName(tableName).parseBean(authorTask));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorTask> selectAll() {
        try {
            return Db.paddingSetAll(Db.db().findAll(Entity.create(tableName)), AuthorTask.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delOne(int id) {
        try {
            return Db.db().del(Entity.create(tableName).set("id", id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
