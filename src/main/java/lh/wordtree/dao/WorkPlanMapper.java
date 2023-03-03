package lh.wordtree.dao;

import lh.wordtree.entity.WorkPlan;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface WorkPlanMapper {

    /**
     * 根据时间顺序获取全部数据
     */
    @Select("select * from WorkPlace")
    List<WorkPlan> selectAllOfDate();

    /**
     * 更新数据
     */
    @Update("update WorkPlace set number = #{number},time = #{time}, works = #{works} where id = #{id}")
    int update(WorkPlan workPlan);

    /**
     * 根据时间查询
     */
    @Select("select * from WorkPlace where id > #{startTime} and id < #{endTime}")
    List<WorkPlan> selectOfDate(Long startTime, Long endTime);

    /**
     * 插入一条数据
     */
    @Insert("insert into WorkPlace values (#{id},#{number}, #{time}, #{works})")
    int insert(WorkPlan workPlan);

    /**
     * 获取最后一条数据
     */
    @Select("SELECT * FROM WorkPlace order by id desc limit 0,1;")
    WorkPlan selectOfLast();
}
