package lh.wordtree.archive.mapper;

import lh.wordtree.archive.entity.Record;

import java.util.List;

public interface RecordMapper {

    int insert(Record record);

    List<Record> selectAll();

    int delOne(int id);
}
