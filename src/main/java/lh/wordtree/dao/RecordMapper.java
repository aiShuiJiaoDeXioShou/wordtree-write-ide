package lh.wordtree.dao;

import lh.wordtree.entity.Record;

import java.util.List;

public interface RecordMapper {

    int insert(Record record);

    List<Record> selectAll();

    int delOne(int id);
}
