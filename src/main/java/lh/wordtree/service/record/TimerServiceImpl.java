package lh.wordtree.service.record;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lh.wordtree.dao.WorkPlanMapper;
import lh.wordtree.entity.WorkPlan;
import lh.wordtree.service.InitializationService;
import lh.wordtree.utils.JDBCUtils;
import lh.wordtree.views.notebook.core.LeftNoteBookFileTreeView;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Objects;

public class TimerServiceImpl implements TimerService, InitializationService {

    private final WorkPlanMapper workPlanMapper = JDBCUtils.getSqlSessionFactory().getMapper(WorkPlanMapper.class);
    ;
    private final WorkPlan lastWorkPlan = workPlanMapper.selectOfLast();
    // 当前工作时间
    private final LocalDate nowTime = LocalDate.now();
    private Log log = LogFactory.get();

    public void init() {
        if (Objects.isNull(lastWorkPlan)) {
            this.insert();
            return;
        }
        // 判断数据库最后一条数据是不是跟当前日期处于同一个日期，如果不是则进行实例化
        var lastTime = lastWorkPlan.getIdToTime();
        if (nowTime.isEqual(ChronoLocalDate.from(lastTime))) return;
        this.insert();
    }

    private void insert() {
        log.info("正在插入用户使用历史计算任务当中...");
        var workPlan = new WorkPlan();
        workPlan.setId(nowTime.toString())
                .setTime(0)
                .setNumber(0)
                .setWorks(LeftNoteBookFileTreeView.INSTANCE.nowFile.getName());
        workPlanMapper.insert(workPlan);
        log.info("正在插入完毕！！！");
    }

    /**
     * 获取当前工作空间记录
     */
    public WorkPlan getNowWorkPlan() {
        return this.lastWorkPlan;
    }

    /**
     * 更新工作空间
     */
    public boolean updateNowWorkPlan(WorkPlan workPlan) {
        return workPlanMapper.update(workPlan) > 0;
    }

    public void apply() {
        this.init();
    }
}
