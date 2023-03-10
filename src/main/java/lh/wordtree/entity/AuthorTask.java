package lh.wordtree.entity;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuthorTask {
    private Integer id;
    // 开始时间
    private LocalDateTime startDateTime;

    // 结束时间
    private LocalDateTime endDateTime;
    // 作者名称
    private String authorName;
    // 任务描述
    private String describe;
    // 是否采用强制手段
    private String isMandatory;
    // 目标字数
    private Integer number;
    // 是否完成了该任务
    private String isComplete;

    public AuthorTask() {
    }

    public AuthorTask(LocalDateTime startDateTime, LocalDateTime endDateTime, String authorName, String describe, String isMandatory, Integer number, String isComplete) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.authorName = authorName;
        this.describe = describe;
        this.isMandatory = isMandatory;
        this.number = number;
        this.isComplete = isComplete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = LocalDateTimeUtil.parse(startDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = LocalDateTimeUtil.parse(endDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getMandatory() {
        return isMandatory;
    }

    public void setMandatory(String mandatory) {
        isMandatory = mandatory;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getComplete() {
        return isComplete;
    }

    public void setComplete(String complete) {
        isComplete = complete;
    }

    @Override
    public String toString() {
        return "AuthorTask{" +
                "id=" + id +
                ", startDateTime='" + startDateTime + '\'' +
                ", endDateTime='" + endDateTime + '\'' +
                ", authorName='" + authorName + '\'' +
                ", describe='" + describe + '\'' +
                ", isMandatory=" + isMandatory +
                ", number=" + number +
                '}';
    }
}
