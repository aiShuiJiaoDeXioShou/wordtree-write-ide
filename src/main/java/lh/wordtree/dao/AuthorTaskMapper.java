package lh.wordtree.dao;

import lh.wordtree.entity.AuthorTask;

import java.util.List;

public interface AuthorTaskMapper {
    int insertAuthorTask(AuthorTask authorTask);

    List<AuthorTask> selectAll();

    int delOne(int id);
}
