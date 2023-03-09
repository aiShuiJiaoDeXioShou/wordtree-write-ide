package lh.wordtree.dao;

import lh.wordtree.entity.WorkPlan;

import java.util.List;

public interface WorkPlanMapper {

    /**
     * 根据时间顺序获取全部数据
     */
    List<WorkPlan> selectAllOfDate();

    /**
     * 更新数据
     */
    int update(WorkPlan workPlan);

    /**
     * 根据时间查询
     */
    List<WorkPlan> selectOfDate(Long startTime, Long endTime);

    /**
     * 插入一条数据
     */
    int insert(WorkPlan workPlan);

    /**
     * 获取最后一条数据
     */
    WorkPlan selectOfLast();
}
