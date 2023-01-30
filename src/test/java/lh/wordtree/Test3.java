package lh.wordtree;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import javafx.scene.input.Clipboard;
import lh.wordtree.dao.WorkPlanMapper;
import lh.wordtree.entity.WorkPlan;
import lh.wordtree.service.task.TaskService;
import lh.wordtree.service.task.TaskServiceImpl;
import lh.wordtree.task.ITask;
import lh.wordtree.utils.JDBCUtils;
import lh.wordtree.utils.WTFileUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test3 {

    @Test
    public void test1() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        var file = clipboard.getString();
        System.out.println(file);
    }

    @Test
    public void test2() {
        var userHomePath = FileUtil.getUserHomePath();
        System.out.println(userHomePath);
    }

    public static void main(String[] args) {
        var parse = LocalDateTimeUtil.parse("2023-01-14 04:38:58", DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        System.out.println(parse);
    }

    @Test
    public void test3() {
        var parse = LocalDateTime.parse("2023-01-14 04:38:58");
        System.out.println(parse);
    }

    @Test
    public void test4() {
        System.out.println("编程语言为：" + WTFileUtils.firstName("c-code.json"));
    }

    @Test
    public void test5() {
        var sqlSessionFactory = JDBCUtils.getSqlSessionFactory();
        WorkPlanMapper workPlanMapper = sqlSessionFactory.getMapper(WorkPlanMapper.class);
        ;
        var i = workPlanMapper.insert(
                new WorkPlan().setWorks("奥特曼")
                        .setId(LocalDate.now().toString())
                        .setNumber(0)
                        .setTime(0));
    }

    @Test
    public void test6() {
        TaskService taskService = new TaskServiceImpl();
        taskService.start(ITask.WRITE);
    }
}
