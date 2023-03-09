package lh.wordtree.ui;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.thread.ThreadUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class WTCalendar extends BorderPane {
    private GridPane root = new GridPane();
    private String dateFormat = "yyyy-MM-dd";
    private Map<String, Integer> frequency;

    public WTCalendar(Map<String, Integer> frequency) {
        this.frequency = frequency;
        this.getStyleClass().add("wt-calendar");
        ThreadUtil.execAsync(() -> {
            // 创建星期，月份
            for (int i = 1; i <= 12; i++) {
                var moth = new Label();
                if (i % 2 == 0) {
                    moth.setText(i + "");
                }
                int finalI = i;
                Platform.runLater(() -> root.add(moth, 1, finalI + 1));
            }
            for (int i = 1; i < 31; i++) {
                var day = new Label();
                if (i % 2 != 0) {
                    day.setText(i + "");
                }
                int finalI = i;
                Platform.runLater(() -> root.add(day, finalI + 1, 1));
            }
        });
        //  获取本年度所有的时间,把获取的时间放到构造工厂获取对应的label
        root.setHgap(5);
        root.setVgap(6);
        this.timeFactory();
        this.setCenter(root);
        var label = new Label("工作列表：");
        label.getStyleClass().add("title");
        this.setTop(label);
        this.setPadding(new Insets(10, 0, 0, 0));
    }

    private void itemFactory(LocalDateTime dateTime) {
        var id = LocalDateTimeUtil.format(dateTime, dateFormat);
        var moth = dateTime.get(ChronoField.MONTH_OF_YEAR);
        var day = dateTime.get(ChronoField.DAY_OF_MONTH);
        int fre = frequency.get(id) == null ? 0 : frequency.get(id);
        var item = new CalendarLabel(fre, dateTime);
        Platform.runLater(() -> root.add(item, day + 1, moth + 1));
    }

    // 获取本年度所有的日期
    private void timeFactory() {
        //日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            // 获取现在的年份
            var year = LocalDateTime.now().getYear();
            // 起始日期
            Date d1 = sdf.parse(year + "-1-1");
            // 结束日期
            Date d2 = sdf.parse(year + "-12-31");
            final Date[] tmp = {d1};
            Calendar dd = Calendar.getInstance();
            dd.setTime(d1);
            ThreadUtil.execAsync(() -> {
                // 打印2001年10月1日到2001年11月4日的日期
                while (tmp[0].getTime() <= d2.getTime()) {
                    tmp[0] = dd.getTime();
                    var dateTime = LocalDateTimeUtil.of(tmp[0]);
                    itemFactory(dateTime);
                    //天数加上1
                    dd.add(Calendar.DAY_OF_MONTH, 1);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static class CalendarLabel extends Label {
        public CalendarLabel(int fre, LocalDateTime time) {
            var item = this;
            item.getStyleClass().add("time-item");
            // 设置对应的颜色频率
            if (fre >= NumberFrequency.LESS1.value) {
                var style = NumberFrequency.LESS1.color;
                item.setStyle("-fx-background-color: " + style + ";-fx-border-color: " + style);
            }
            if (fre >= NumberFrequency.MORE0.value) {
                var style = NumberFrequency.MORE0.color;
                item.setStyle("-fx-background-color: " + style + ";-fx-border-color: " + style);
            }
            if (fre >= NumberFrequency.MORE1.value) {
                var style = NumberFrequency.MORE1.color;
                item.setStyle("-fx-background-color: " + style + ";-fx-border-color: " + style);
            }
            if (fre >= NumberFrequency.MORE2.value) {
                var style = NumberFrequency.MORE2.color;
                item.setStyle("-fx-background-color: " + style + ";-fx-border-color: " + style);
            }
            var area = new VBox();
            area.setSpacing(10);
            var timeLabel = new Label(LocalDateTimeUtil.format(time, "yyyy年MM月dd日"));
            timeLabel.setStyle("-fx-text-fill: #74c0fc;-fx-font-size: 15;-fx-font-family: monaco");
            var numberLabel = new Label("字数：" + fre);
            numberLabel.setStyle("-fx-font-size: 10");
            area.getChildren().addAll(timeLabel, numberLabel);
            area.setStyle("""
                        -fx-background-color: -def-backgroud-color;
                        -fx-border-color: -def-border-color;
                        -fx-border-radius: 5;
                        -fx-padding: 10;
                    """);
            new WTPopup(this, area);
        }
    }

    public enum NumberFrequency {
        LESS0(0, "#e9ecef"),
        LESS1(100, "#0e4429"),
        MORE0(1000, "#006d32"),
        MORE1(3000, "#31ab4b"),
        MORE2(6000, "#39d353");
        public int value;
        public String color;

        NumberFrequency(int value, String color) {
            this.value = value;
            this.color = color;
        }
    }

}
