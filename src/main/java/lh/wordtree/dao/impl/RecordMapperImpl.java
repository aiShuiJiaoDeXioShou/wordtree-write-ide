package lh.wordtree.dao.impl;

import cn.hutool.db.Entity;
import lh.wordtree.dao.RecordMapper;
import lh.wordtree.entity.Record;
import lh.wordtree.utils.DbUtils;

import java.sql.SQLException;
import java.util.List;

public class RecordMapperImpl implements RecordMapper {
    private static String tableName = "Record";

    @Override
    public int insert(Record record) {
        try {
            return DbUtils.db().insert(new Entity().setTableName(tableName).parseBean(record, false, true));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Record> selectAll() {
        try {
            return DbUtils.paddingAll(DbUtils.db().findAll(Entity.create(tableName)), Record.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delOne(int id) {
        try {
            return DbUtils.db().del(Entity.create(tableName).set("id", id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
