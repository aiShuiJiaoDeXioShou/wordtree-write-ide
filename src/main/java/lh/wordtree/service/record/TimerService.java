package lh.wordtree.service.record;

import lh.wordtree.entity.WorkPlan;

public interface TimerService {
    TimerService INSTANCE = new TimerServiceImpl();
    void init();

    WorkPlan getNowWorkPlan();

    boolean updateNowWorkPlan(WorkPlan workPlan);
}
