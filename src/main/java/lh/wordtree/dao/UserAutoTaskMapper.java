package lh.wordtree.dao;

import lh.wordtree.entity.UserAutoTask;

import java.util.List;

public interface UserAutoTaskMapper {
    boolean insert(UserAutoTask userAutoTask);

    boolean closeOrStart(int id, int isStart);

    List<UserAutoTask> tasks();

    int delOne(int id);
}
