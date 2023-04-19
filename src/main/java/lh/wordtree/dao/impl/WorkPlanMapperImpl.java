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
            var workPlan = DbUtils.db().query("SELECT * FROM WorkPlace WHERE id BETWEEN date('now','start of year') AND date('now','start of year','+1 year','-1 day');");
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
        }
    }

    @Override
    public List<WorkPlan> selectOfDate(Long startTime, Long endTime) {
        return null;
    }

    @Override
    public int insert(WorkPlan workPlan) {
        try {
            return DbUtils.db().insert(new Entity().setTableName(tableName).parseBean(workPlan, false, true));
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

    /**
     * 查询从指定日期到指定日期查询的所有字段
     */
    public List<WorkPlan> selectBetweenDate(String startTime, String endTime) {
        try {
            List<Entity> query = DbUtils.db().query("SELECT * FROM WorkPlace WHERE id BETWEEN %s AND %s;".formatted(startTime, endTime));
            return DbUtils.paddingAll(query, WorkPlan.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询这个指定时间段作者所有的码字字数
     */
    public long selectDateNumber(String startTime, String endTime) {
        try {
            return DbUtils.db()
                    .queryOne("SELECT SUM(number) AS number FROM WorkPlace WHERE id BETWEEN %s AND %s".formatted(startTime, endTime))
                    .getLong("number");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
