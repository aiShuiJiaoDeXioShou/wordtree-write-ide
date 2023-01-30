package lh.wordtree.dao;

import lh.wordtree.entity.Record;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RecordMapper {

    @Insert("insert into Record(context,title) values (#{context}, #{title})")
    int insert(Record record);

    @Select("select * from Record")
    List<Record> selectAll();

    @Delete("delete from Record where id = #{id};")
    int delOne(int id);
}
