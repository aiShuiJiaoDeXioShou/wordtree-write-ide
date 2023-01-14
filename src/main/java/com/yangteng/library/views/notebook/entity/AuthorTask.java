package com.yangteng.library.views.notebook.entity;

public class AuthorTask {
    private Integer id;
    // 开始时间
    private String startDateTime;
    // 结束时间
    private String endDateTime;
    // 作者名称
    private String authorName;
    // 任务描述
    private String describe;
    // 是否采用强制手段
    private Boolean isMandatory;
    // 目标字数
    private Integer number;
    // 是否完成了该任务
    private Boolean isComplete;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
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

    public Boolean getMandatory() {
        return isMandatory;
    }

    public void setMandatory(Boolean mandatory) {
        isMandatory = mandatory;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
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
