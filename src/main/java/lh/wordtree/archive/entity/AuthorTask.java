package lh.wordtree.archive.entity;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AuthorTask implements AutoTask {
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
    private String mandatory;
    // 目标字数
    private Integer number;
    // 是否完成了该任务
    private String complete;

    public AuthorTask() {
    }

    public AuthorTask(LocalDateTime startDateTime, LocalDateTime endDateTime, String authorName, String describe, String mandatory, Integer number, String complete) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.authorName = authorName;
        this.describe = describe;
        this.mandatory = mandatory;
        this.number = number;
        this.complete = complete;
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
        try {
            this.startDateTime = LocalDateTimeUtil.parse(startDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        } catch (DateTimeParseException e) {
            this.startDateTime = LocalDateTimeUtil.parse(startDateTime);
        }
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        try {
            this.endDateTime = LocalDateTimeUtil.parse(endDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        } catch (DateTimeParseException e) {
            this.endDateTime = LocalDateTimeUtil.parse(endDateTime);
        }
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
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        mandatory = mandatory;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        complete = complete;
    }

    @Override
    public String toString() {
        return "AuthorTask{" +
                "id=" + id +
                ", startDateTime='" + startDateTime + '\'' +
                ", endDateTime='" + endDateTime + '\'' +
                ", authorName='" + authorName + '\'' +
                ", describe='" + describe + '\'' +
                ", isMandatory=" + mandatory +
                ", number=" + number +
                '}';
    }
}
