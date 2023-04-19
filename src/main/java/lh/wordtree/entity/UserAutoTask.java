package lh.wordtree.entity;

/**
 * 每隔一段时间执行该任务
 */
public class UserAutoTask implements AutoTask {

    /**
     * 编号
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 执行时间表达式
     */
    private String executionTime;

    /**
     * 描述
     */
    private String description;

    /**
     * 执行脚本所在路径,支持Python脚本、java or kotlin脚本
     */
    private String url;

    /**
     * 是否开启,1表示开启,0表示未开启
     */
    private int isStart;

    public UserAutoTask(Integer id, String title, String executionTime, String description, String url, int isStart) {
        this.id = id;
        this.title = title;
        this.executionTime = executionTime;
        this.description = description;
        this.url = url;
        this.isStart = isStart;
    }

    public UserAutoTask() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsStart() {
        return isStart;
    }

    public void setIsStart(int isStart) {
        this.isStart = isStart;
    }

    @Override
    public String toString() {
        return "UserAutoTask{" +
                "title='" + title + '\'' +
                ", executionTime='" + executionTime + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", isStart=" + isStart +
                '}';
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
