package lh.wordtree.comm.entity;

import java.time.LocalDateTime;

public class NovelProject {
    private String name;
    private String path;
    private String targetWeb;
    private String img;
    private String theme;
    private String author;
    private Integer targetNumber;
    private Integer nowNumber;
    private LocalDateTime startDateTime;
    private LocalDateTime lastDateTime;
    private String briefIntroduction;

    public NovelProject(String name, String path, String targetWeb, String img, String theme, String author, Integer targetNumber, Integer nowNumber, LocalDateTime startDateTime, LocalDateTime lastDateTime) {
        this.name = name;
        this.path = path;
        this.targetWeb = targetWeb;
        this.img = img;
        this.theme = theme;
        this.author = author;
        this.targetNumber = targetNumber;
        this.nowNumber = nowNumber;
        this.startDateTime = startDateTime;
        this.lastDateTime = lastDateTime;
    }

    public NovelProject(String name, String path, String targetWeb, String img, String theme, String author, Integer targetNumber, Integer nowNumber, LocalDateTime startDateTime, LocalDateTime lastDateTime, String briefIntroduction) {
        this.name = name;
        this.path = path;
        this.targetWeb = targetWeb;
        this.img = img;
        this.theme = theme;
        this.author = author;
        this.targetNumber = targetNumber;
        this.nowNumber = nowNumber;
        this.startDateTime = startDateTime;
        this.lastDateTime = lastDateTime;
        this.briefIntroduction = briefIntroduction;
    }

    public String getBriefIntroduction() {
        return briefIntroduction;
    }

    public void setBriefIntroduction(String briefIntroduction) {
        this.briefIntroduction = briefIntroduction;
    }

    public NovelProject() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTargetWeb() {
        return targetWeb;
    }

    public void setTargetWeb(String targetWeb) {
        this.targetWeb = targetWeb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(Integer targetNumber) {
        this.targetNumber = targetNumber;
    }

    public Integer getNowNumber() {
        return nowNumber;
    }

    public void setNowNumber(Integer nowNumber) {
        this.nowNumber = nowNumber;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getLastDateTime() {
        return lastDateTime;
    }

    public void setLastDateTime(LocalDateTime lastDateTime) {
        this.lastDateTime = lastDateTime;
    }

    @Override
    public String toString() {
        return "NovelProject{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", targetWeb='" + targetWeb + '\'' +
                ", img='" + img + '\'' +
                ", theme='" + theme + '\'' +
                ", author='" + author + '\'' +
                ", targetNumber=" + targetNumber +
                ", nowNumber=" + nowNumber +
                ", startDateTime=" + startDateTime +
                ", lastDateTime=" + lastDateTime +
                '}';
    }
}
