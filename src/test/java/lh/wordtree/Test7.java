package lh.wordtree;

import lh.wordtree.dao.impl.AuthorTaskMapperImpl;
import lh.wordtree.entity.AuthorTask;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Test7 {
    @Test
    public void dao1() throws SQLException {
        var authorTaskMapper = new AuthorTaskMapperImpl();
        authorTaskMapper.insertAuthorTask(
                new AuthorTask(LocalDateTime.now(), LocalDateTime.now(), "linghe", "hello", "true", 100, "false")
        );
    }
}
