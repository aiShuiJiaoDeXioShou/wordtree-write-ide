package lh.wordtree.archive.mapper;

import lh.wordtree.archive.entity.AuthorTask;

import java.util.List;

public interface AuthorTaskMapper {
    int insertAuthorTask(AuthorTask authorTask);

    List<AuthorTask> selectAll();

    int delOne(int id);
}
