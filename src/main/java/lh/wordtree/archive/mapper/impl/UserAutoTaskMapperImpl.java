package lh.wordtree.archive.mapper.impl;

import cn.hutool.db.Entity;
import lh.wordtree.archive.mapper.UserAutoTaskMapper;
import lh.wordtree.archive.entity.UserAutoTask;
import lh.wordtree.comm.Db;

import java.sql.SQLException;
import java.util.List;

public class UserAutoTaskMapperImpl implements UserAutoTaskMapper {
    private final String tableName = "UserAutoTask";

    public UserAutoTaskMapperImpl() {
        // 判断sqlite里面是否存在该表，如果不存在则创建该表格
        try {
            Db.db().query("SELECT count(*) FROM sqlite_master WHERE name='%s';".formatted(tableName));
        } catch (SQLException e) {
            // 创建该表格
            try {
                Db.db().execute("""
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
            return Db.db()
                    .insert(new Entity().setTableName(tableName).parseBean(userAutoTask, false, true)) != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean closeOrStart(int id, int isStart) {
        try {
            return Db.db().update(
                    new Entity().set("isStart", isStart),
                    new Entity().setTableName(tableName).set("id", id)) != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserAutoTask> tasks() {
        try {
            return Db.paddingSetAll(Db.db().findAll(Entity.create(tableName)), UserAutoTask.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int delOne(int id) {
        try {
            return Db.db().del(Entity.create(tableName).set("id", id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
