package lh.wordtree.archive.entity;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDate;

/**
 * 当软件被打开的时候创建该对象，写入到sqllite数据库当中
 * ID 属性为工作目录打开的时间
 */
public class WorkPlan {

    /**
     * 唯一ID根据当前日期创建
     */
    private String id;

    /**
     * 码字字数
     */
    private Integer number;

    /**
     * 码字作品
     */
    private String works;

    /**
     * 用时多少秒
     */
    private int time;

    /**
     * 时速
     */
    public Integer getSpeedPerHour() {
        Integer speedPerHour = Math.toIntExact(getNumber() / (getTime() / 3600));
        return speedPerHour;
    }

    public String getId() {
        return id;
    }

    public WorkPlan setId(String time) {
        this.id = time;
        System.out.println(this.id);
        return this;
    }

    public LocalDate getIdToTime() {
        return LocalDateTimeUtil
                .parse(id, "yyyy-MM-dd").toLocalDate();
    }

    public int getTime() {
        return time;
    }

    public WorkPlan setTime(int time) {
        this.time = time;
        return this;
    }

    public Integer getNumber() {
        return number;
    }

    public WorkPlan setNumber(Integer number) {
        this.number = number;
        return this;
    }

    public String getWorks() {
        return works;
    }

    public WorkPlan setWorks(String works) {
        this.works = works;
        return this;
    }

    @Override
    public String toString() {
        return "WorkPlan{" +
                "id=" + id +
                ", number=" + number +
                ", works='" + works + '\'' +
                ", time=" + time +
                '}';
    }
}
