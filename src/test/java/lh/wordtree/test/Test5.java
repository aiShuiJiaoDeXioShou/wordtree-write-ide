package lh.wordtree.test;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import lh.wordtree.component.CpMessage;
import lh.wordtree.plugin.bookshelf.ReaderTextBuilder;
import lh.wordtree.service.plugin.WTPluginServiceImpl;
import lh.wordtree.ui.controls.WTFxInputAlert;
import lh.wordtree.ui.controls.WTOneWindow;
import lh.wordtree.views.record.RecordView;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test5 {

    @Test
    public void test1() {
        //日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            var date = new Date();
            var year = LocalDateTime.now().getYear();
            //起始日期
            Date d1 = sdf.parse(year + "-1-1");
            //结束日期
            Date d2 = sdf.parse(year + "-12-31");
            Date tmp = d1;
            Calendar dd = Calendar.getInstance();
            dd.setTime(d1);
            //打印2001年10月1日到2001年11月4日的日期
            while (tmp.getTime() <= d2.getTime()) {
                tmp = dd.getTime();
                System.out.println(sdf.format(tmp));
                //天数加上1
                dd.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        var now = LocalDateTime.now();
        var i = now.get(ChronoField.DAY_OF_WEEK);
        System.out.println(i);
    }

    @Test
    public void test3() {
        String src = "D:\\ytjava\\ideayt\\u1\\build\\libs";
        var service = new WTPluginServiceImpl(src);
        service.sendJar();
    }

    private void test() {
        var test = new VBox();
        var button = new Button("loading");
        var alert = new Button("alert");
        button.setPadding(new Insets(4, 10, 4, 10));
        var record = new Button("记录");
        test.getChildren().addAll(button, alert, record);
        record.setOnMouseClicked(event -> {
            var stage = new WTOneWindow("工具箱");
            stage.getRoot().getChildren().addAll(new RecordView());
            stage.getLabel().setStyle("-fx-text-fill: #ffff");
            stage.getTop().setStyle("-fx-background-color: #495057");
            stage.show();
        });
        button.setOnMouseClicked(e -> {
            CpMessage.sendError("无法使用该模块，因为没有开发！");
        });
        List<WTFxInputAlert.AlertTask> alertTasks = new ArrayList<>();
        alertTasks.add(new WTFxInputAlert.AlertTask("章节", "static/icon/default_file.svg", alert1 -> {
            System.out.println("章节");
        }));
        alertTasks.add(new WTFxInputAlert.AlertTask("Java", "static/icon/java.svg", alert1 -> {
            System.out.println("Java文件");
        }));
        alertTasks.add(new WTFxInputAlert.AlertTask("Python", "static/icon/py.svg", alert1 -> {
            System.out.println("Python文件");
        }));
        var alert1 = new WTFxInputAlert("新建文件", alertTasks);
        alert.setOnMouseClicked(e -> {
            alert1.show();
        });
    }

    @Test
    public void test4() {
        // 创建 Pattern 对象
        Pattern p = Pattern.compile("(^第\\d+章|^第[一二三四五六七八九十]|^\\d+).+[^\\r\\n]+");
        String[] split = "2、诡秘自主\n你是主...\n3、诡秘自主2\n你是主2...".split("\n");
        for (String s : split) {
            // 创建 Matcher 对象
            Matcher m = p.matcher(s);
            // 查找匹配的标题
            while (m.find()) {
                System.out.println("找到标题: " + m.group());
            }
        }
    }

    @Test
    public void test5() {
        String s = "C:\\Users\\28322\\Documents\\Book\\诡秘之主.txt";
        ReaderTextBuilder readerTextBuilder = ReaderTextBuilder.builder(new File(s));
        List<ReaderTextBuilder.C> cs = readerTextBuilder.find();
        String 第二章_情况 = readerTextBuilder.findTitleByContent("第二章 情况");
        System.out.println(第二章_情况);
    }


}
