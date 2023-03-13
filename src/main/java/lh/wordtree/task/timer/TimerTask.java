package lh.wordtree.task.timer;

import cn.hutool.core.thread.ThreadUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lh.wordtree.entity.WorkPlan;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.service.record.TimerService;
import lh.wordtree.task.Task;
import lh.wordtree.task.WTTask;

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

    public void write() {
        nowNumber++;
        auto.set(true);
    }

    public void init() {
        timerService.init();
        nowWork = timerService.getNowWorkPlan();
        nowSecond = nowWork.getTime();
        nowNumber = nowWork.getNumber();
        ThreadUtil.execAsync(() -> {
            while (true) {
                ThreadUtil.sleep(1000);
                if (auto.get()) {
                    nowSecond++;
                    auto.set(false);
                    Platform.runLater(() -> {
                        FactoryBeanService.time.set(getDate(nowSecond));
                        FactoryBeanService.number.set("今日已码" + nowNumber + "个字");
                    });
                }
            }
        });
    }

    // 读取任务管理器中的时间属性,判断该任务是否到时间了
    public void getTask() {

    }

    public void end() {
        var nowWorkPlan = timerService.getNowWorkPlan();
        nowWorkPlan.setNumber(nowNumber);
        nowWorkPlan.setTime(nowSecond);
        timerService.updateNowWorkPlan(nowWorkPlan);
    }
}
