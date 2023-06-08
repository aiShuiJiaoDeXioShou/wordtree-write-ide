package lh.wordtree.test;

import cn.hutool.core.date.LocalDateTimeUtil;
import javafx.application.Application;
import javafx.stage.Stage;
import lh.wordtree.archive.mapper.impl.AuthorTaskMapperImpl;
import lh.wordtree.archive.mapper.impl.UserAutoTaskMapperImpl;
import lh.wordtree.archive.entity.AuthorTask;
import lh.wordtree.archive.entity.UserAutoTask;
import lh.wordtree.plugin.bookshelf.WTReader;
import lh.wordtree.comm.Db;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Test7 extends Application {
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
    public void auto() throws SQLException {
        Number query = Db.db().queryNumber("SELECT SUM(number) FROM WorkPlace WHERE id BETWEEN '2023-04-09' AND '2023-04-13'");
        System.out.println(query.longValue());
    }

    @Override
    public void start(Stage stage) throws Exception {
        WTReader reader = new WTReader(new File("C:\\Users\\28322\\Documents\\Book\\诡秘之主.txt"));
        reader.show();
    }
}
