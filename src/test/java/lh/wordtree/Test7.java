package lh.wordtree;

import cn.hutool.core.date.LocalDateTimeUtil;
import lh.wordtree.dao.impl.AuthorTaskMapperImpl;
import lh.wordtree.entity.AuthorTask;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test7 {
    @Test
    public void dao1() throws SQLException {
        var authorTaskMapper = new AuthorTaskMapperImpl();
        authorTaskMapper.insertAuthorTask(
                new AuthorTask(LocalDateTime.now(), LocalDateTime.now(), "linghe", "hello", "true", 100, "false")
        );
    }

    @Test
    public void dao2() {
        var parse = LocalDateTimeUtil.parse("2023-3-14 20:40:12", DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        System.out.println(parse);
    }
}
