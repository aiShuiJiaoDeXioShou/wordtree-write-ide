package lh.wordtree.model.task;

import cn.hutool.db.Entity;
import lh.wordtree.archive.entity.WorkPlan;
import lh.wordtree.comm.Db;

import java.sql.SQLException;
import java.util.List;

public class WorkPlanMapperImpl implements WorkPlanMapper {
    private final String tableName = "WorkPlace";

    @Override
    public List<WorkPlan> selectAllOfDate() {
        try {
            var workPlan = Db.db().query("SELECT * FROM WorkPlace WHERE id BETWEEN date('now','start of year') AND date('now','start of year','+1 year','-1 day');");
            return Db.paddingAll(workPlan, WorkPlan.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(WorkPlan workPlan) {
        try {
            return Db.db().update(
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
            return Db.db().insert(new Entity().setTableName(tableName).parseBean(workPlan, false, true));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WorkPlan selectOfLast() {
        try {
            return Db.padding(Db.db().queryOne("SELECT * FROM WorkPlace order by id desc limit 0,1;"), WorkPlan.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询从指定日期到指定日期查询的所有字段
     */
    public List<WorkPlan> selectBetweenDate(String startTime, String endTime) {
        try {
            List<Entity> query = Db.db().query("SELECT * FROM WorkPlace WHERE id BETWEEN %s AND %s;".formatted(startTime, endTime));
            return Db.paddingAll(query, WorkPlan.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询这个指定时间段作者所有的码字字数
     */
    public long selectDateNumber(String startTime, String endTime) {
        try {
            return Db.db()
                    .queryNumber("SELECT SUM(number) AS number FROM WorkPlace WHERE id BETWEEN '%s' AND '%s'".formatted(startTime, endTime))
                    .longValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
