package lh.wordtree.entity;

import java.beans.JavaBean;
import java.time.LocalDateTime;

@JavaBean
public class RecentFiles {
    private LocalDateTime time;
    private String filePath;
    private String userName;
    private String workspaceName;

    public RecentFiles(LocalDateTime time, String filePath, String userName, String workspaceName) {
        this.time = time;
        this.filePath = filePath;
        this.userName = userName;
        this.workspaceName = workspaceName;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }
}