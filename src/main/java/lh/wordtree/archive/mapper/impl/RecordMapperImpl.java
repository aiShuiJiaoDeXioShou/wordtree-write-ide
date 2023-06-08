package lh.wordtree.archive.mapper.impl;

import cn.hutool.db.Entity;
import lh.wordtree.archive.mapper.RecordMapper;
import lh.wordtree.archive.entity.Record;
import lh.wordtree.comm.Db;

import java.sql.SQLException;
import java.util.List;

public class RecordMapperImpl implements RecordMapper {
    private static String tableName = "Record";

    @Override
    public int insert(Record record) {
        try {
            return Db.db().insert(new Entity().setTableName(tableName).parseBean(record, false, true));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Record> selectAll() {
        try {
            return Db.paddingAll(Db.db().findAll(Entity.create(tableName)), Record.class);
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
