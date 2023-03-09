package lh.wordtree;

import lh.wordtree.dao.impl.AuthorTaskMapperImpl;
import lh.wordtree.entity.AuthorTask;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class Test7 {
    @Test
    public void dao1() throws SQLException {
        var authorTaskMapper = new AuthorTaskMapperImpl();
        var authorTasks = authorTaskMapper.selectAll();
        for (AuthorTask authorTask : authorTasks) {
            System.out.println(authorTask);
        }
    }
}
