package lh.wordtree.task;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lh.wordtree.archive.entity.WorkPlan;
import lh.wordtree.comm.BeanFactory;
import lh.wordtree.service.record.TimerService;
import lh.wordtree.task.Task;
import lh.wordtree.task.WTTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Task(name = "后台记录任务", value = -1)
public class TimerTask implements WTTask {
    public static int nowSecond;
    public static int nowNumber;
    private final TimerService timerService = TimerService.INSTANCE;
    private final BooleanProperty auto = new SimpleBooleanProperty(false);
    private WorkPlan nowWork;

    private static String getDate(Integer date) {
        if (date < 60) {
            return date + "秒";
        } else if (date > 60 && date < 3600) {
            int m = date / 60;
            int s = date % 60;
            return m + "分" + s + "秒";
        } else {
            int h = date / 3600;
            int m = (date % 3600) / 60;
            int s = (date % 3600) % 60;
            return h + "小时" + m + "分" + s + "秒";
        }

    }

    public void write(String source) {
        nowNumber++;
        auto.set(true);
    }

    public void init() {
        timerService.init();
        nowWork = timerService.getNowWorkPlan();
        nowSecond = nowWork.getTime();
        nowNumber = nowWork.getNumber();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (auto.get()) {
                nowSecond++;
                auto.set(false);
                Platform.runLater(() -> {
                    BeanFactory.time.set(getDate(nowSecond));
                    BeanFactory.number.set("今日已码" + nowNumber + "个字");
                });
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void end() {
        var nowWorkPlan = timerService.getNowWorkPlan();
        nowWorkPlan.setNumber(nowNumber);
        nowWorkPlan.setTime(nowSecond);
        timerService.updateNowWorkPlan(nowWorkPlan);
    }
}
