package lh.wordtree.dao;

import lh.wordtree.entity.AuthorTask;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AuthorTaskMapper {
    int insertAuthorTask(AuthorTask authorTask);

    @Select("select * from AuthorTask")
    List<AuthorTask> selectAll();
}
