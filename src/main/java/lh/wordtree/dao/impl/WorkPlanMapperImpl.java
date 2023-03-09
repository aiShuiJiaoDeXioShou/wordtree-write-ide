package lh.wordtree.dao.impl;

import cn.hutool.db.Entity;
import lh.wordtree.dao.WorkPlanMapper;
import lh.wordtree.entity.WorkPlan;
import lh.wordtree.utils.DbUtils;

import java.sql.SQLException;
import java.util.List;

public class WorkPlanMapperImpl implements WorkPlanMapper {
    private final String tableName = "WorkPlace";

    @Override
    public List<WorkPlan> selectAllOfDate() {
        try {
            var workPlan = DbUtils.db().findAll(tableName);
            return DbUtils.paddingAll(workPlan, WorkPlan.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(WorkPlan workPlan) {
        try {
            return DbUtils.db().update(
                    //修改的数据
                    Entity.create().set("number", workPlan.getNumber())
                            .set("works", workPlan.getWorks())
                            .set("time", workPlan.getTime()),
                    Entity.create(tableName).set("id", workPlan.getId()) //where条件
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
        }
    }

    @Override
    public List<WorkPlan> selectOfDate(Long startTime, Long endTime) {
        return null;
    }

    @Override
    public int insert(WorkPlan workPlan) {
        try {
            return DbUtils.db().insert(new Entity().setTableName(tableName).parseBean(workPlan));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WorkPlan selectOfLast() {
        try {
            return DbUtils.padding(DbUtils.db().queryOne("SELECT * FROM WorkPlace order by id desc limit 0,1;"), WorkPlan.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
