package lh.wordtree.service.record;

import lh.wordtree.entity.WorkPlan;

public interface TimerService {
    void init();

    WorkPlan getNowWorkPlan();

    boolean updateNowWorkPlan(WorkPlan workPlan);
}
