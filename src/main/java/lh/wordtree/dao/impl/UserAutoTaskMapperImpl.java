package lh.wordtree.dao.impl;

import cn.hutool.db.Entity;
import lh.wordtree.dao.UserAutoTaskMapper;
import lh.wordtree.entity.UserAutoTask;
import lh.wordtree.utils.DbUtils;

import java.sql.SQLException;
import java.util.List;

public class UserAutoTaskMapperImpl implements UserAutoTaskMapper {
    private final String tableName = "UserAutoTask";

    public UserAutoTaskMapperImpl() {
        // 判断sqlite里面是否存在该表，如果不存在则创建该表格
        try {
            DbUtils.db().query("SELECT count(*) FROM sqlite_master WHERE name='%s';".formatted(tableName));
        } catch (SQLException e) {
            // 创建该表格
            try {
                DbUtils.db().execute("""
                            CREATE TABLE "%s" (
                              id INTEGER PRIMARY KEY AUTOINCREMENT,
                              title TEXT,
                              executionTime TEXT,
                              description TEXT,
                              url TEXT,
                              isStart INTEGER
                            );
                        """.formatted(tableName));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public boolean insert(UserAutoTask userAutoTask) {
        try {
            return DbUtils.db()
                    .insert(new Entity().setTableName(tableName).parseBean(userAutoTask, false, true)) != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean closeOrStart(int id, int isStart) {
        try {
            return DbUtils.db().update(
                    new Entity().set("isStart", isStart),
                    new Entity().setTableName(tableName).set("id", id)) != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserAutoTask> tasks() {
        try {
            return DbUtils.paddingSetAll(DbUtils.db().findAll(Entity.create(tableName)), UserAutoTask.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int delOne(int id) {
        try {
            return DbUtils.db().del(Entity.create(tableName).set("id", id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
