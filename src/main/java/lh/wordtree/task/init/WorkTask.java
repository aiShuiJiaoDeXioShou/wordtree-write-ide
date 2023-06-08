package lh.wordtree.task.init;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSON;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.entity.NovelProject;
import lh.wordtree.plugin.wtlang.WtLangView;
import lh.wordtree.comm.BeanFactory;
import lh.wordtree.service.record.WorkSpaceService;
import lh.wordtree.task.ITask;
import lh.wordtree.task.Task;
import lh.wordtree.task.WTTask;
import lh.wordtree.views.core.FileTreeView;
import lh.wordtree.views.core.MenuView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;

@Task(value = 1, name = "工作空间初始化任务", iTask = ITask.INIT)
public class WorkTask implements WTTask {


    public void apply() {
        // 对工作空间进行初始化操作
        fileInit();
        // 初始化成功之后读取WT任务
        parseWT();
        // 对根源目录进行监听，如果路径发生改变，则重新刷新
        BeanFactory.nowRootFile.addListener((observable, oldValue, newValue) -> {
            FileTreeView.newInstance().toggleFile(newValue);
            parseWT();
        });
    }

    private void fileInit() {
        var recentFiles = WorkSpaceService.get();
        var size = recentFiles.size();
        if (size > 0) BeanFactory.nowRootFile.set(new File(recentFiles.get(0).getFilePath()));
        else BeanFactory.nowRootFile.set(new File(Config.BASE_WORKSPACE));
        File nowRootFile = BeanFactory.nowRootFile.get();
        FileTreeView.newInstance().toggleFile(nowRootFile);
    }

    private void parseWT() {
        File nowRootFile = BeanFactory.nowRootFile.get();
        if (nowRootFile == null || nowRootFile.list() == null) return;
        var contains = Arrays.stream(nowRootFile.list())
                .toList()
                .contains(Config.WT_CONFIG_DIR);
        BeanFactory.isWt.set(contains);
        if (contains) {
            // 读取相关文件并且转化为全局对象
            var configDir = new File(nowRootFile.getPath() + "/" + Config.WT_CONFIG_DIR);
            var configs = Arrays
                    .stream(Objects.requireNonNull(configDir.listFiles()))
                    .toList();
            configs.forEach(file -> {
                switch (file.getName()) {
                    case "project.json" -> {
                        ThreadUtil.execAsync(() -> {
                            var bytes = FileUtil.readBytes(file);
                            var novelProject = JSON.parseObject(bytes, NovelProject.class);
                            Platform.runLater(() -> BeanFactory.novelProject.set(novelProject));
                            if (FileUtil.exist(novelProject.getImg())) {
                                ImageView imageView;
                                try {
                                    imageView = new ImageView(new Image(new FileInputStream(novelProject.getImg())));
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                imageView.setFitHeight(20);
                                imageView.setFitWidth(20);
                                Platform.runLater(() -> MenuView.newInstance().toggleWorkSpace.setGraphic(imageView));
                            }
                        });
                    }
                }
            });
        }
        if (contains) {
            // 加载人物关系插件，解析人物对象关系
            ThreadUtil.execAsync(() -> {
                String path = BeanFactory.nowRootFile.get().getPath() + "/大纲/人物.json";
                File file = new File(path);
                if (file.exists()) {
                    new WtLangView(file);
                }
            });
        }
    }
}
