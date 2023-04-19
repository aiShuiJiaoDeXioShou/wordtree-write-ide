package lh.wordtree.test;

import cn.hutool.core.date.LocalDateTimeUtil;
import lh.wordtree.dao.impl.AuthorTaskMapperImpl;
import lh.wordtree.dao.impl.UserAutoTaskMapperImpl;
import lh.wordtree.entity.AuthorTask;
import lh.wordtree.entity.UserAutoTask;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    public void wordTree() throws Exception {
        List<String> stringsToSort = new ArrayList<>();
        stringsToSort.add("大地");
        stringsToSort.add("大帝");
        stringsToSort.add("萧炎大");
        stringsToSort.add("你好");
        String pattern = ".*大.*";
        List<String> sortedStrings = stringsToSort.stream()
                .filter(s -> s.matches(pattern))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        System.out.println(sortedStrings);
    }

    @Test
    public void find() {
        UserAutoTaskMapperImpl userAutoTaskMapper = new UserAutoTaskMapperImpl();
        UserAutoTask userAutoTask = new UserAutoTask();
        userAutoTask.setExecutionTime("1000s");
        userAutoTask.setDescription("test");
        userAutoTask.setUrl("./");
        userAutoTask.setIsStart(1);
        userAutoTask.setTitle("test");
        boolean insert = userAutoTaskMapper.insert(userAutoTask);
        System.out.println(insert);
        List<UserAutoTask> tasks = userAutoTaskMapper.tasks();
        System.out.println(tasks);
        userAutoTaskMapper.closeOrStart(0, 0);
    }

    @Test
    public void auto() {

    }

}