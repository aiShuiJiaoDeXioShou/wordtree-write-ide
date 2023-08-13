package lh.wordtree.model.bookrack;

import javafx.beans.property.SimpleObjectProperty;
import lh.wordtree.comm.entity.NovelProject;
import lh.wordtree.model.task.WorkPlanMapper;
import lh.wordtree.model.task.WorkPlanMapperImpl;
import lh.wordtree.archive.entity.Author;
import lh.wordtree.archive.entity.RecentFiles;
import lh.wordtree.archive.entity.WorkPlan;
import lh.wordtree.comm.BeanFactory;
import lh.wordtree.service.record.WorkSpaceService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoViewModel {
    // 获取当前的用户信息
    private final SimpleObjectProperty<Author> user = BeanFactory.user;
    private final List<RecentFiles> recentFiles = WorkSpaceService.get();
    private final WorkPlanMapper workPlanMapper = new WorkPlanMapperImpl();

    public Author user() {
        return user.get();
    }

    // 获取历史书籍信息
    public List<NovelProject> novelProjects() {
        return recentFiles.stream().map(recent -> {
            var novelProject = new NovelProject();
            novelProject.setName(recent.getWorkspaceName());
            novelProject.setAuthor(recent.getUserName());
            novelProject.setPath(recent.getFilePath());
            return novelProject;
        }).collect(Collectors.toList());
    }

    // 获取该用户工作的全部日期
    public HashMap<String, Integer> getWorkPlan() {
        var workPlan = workPlanMapper.selectAllOfDate();
        // 获取当前的年份
        var nowYear = LocalDateTime.now().getYear();
        var data = new HashMap<String, Integer>();
        List<WorkPlan> collect = workPlan.stream().filter(work -> {
            var time = work.getId();
            var year = time.split("-")[0];
            return Integer.parseInt(year) == nowYear;
        }).peek(work -> {
            data.put(work.getId(), work.getNumber());
        }).toList();
        return data;
    }
}
