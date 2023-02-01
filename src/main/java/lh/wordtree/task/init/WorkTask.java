package lh.wordtree.task.init;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSON;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lh.wordtree.config.Config;
import lh.wordtree.entity.NovelProject;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.service.record.WorkSpaceService;
import lh.wordtree.task.Task;
import lh.wordtree.task.WTTask;
import lh.wordtree.views.notebook.core.FileTreeView;
import lh.wordtree.views.notebook.core.MenuView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;

@Task(value = 1, name = "工作空间初始化任务")
public class WorkTask implements WTTask {


    public void init() {
        // 对工作空间进行初始化操作
        fileInit();
        // 初始化成功之后读取WT任务
        parseWT();
        // 对根源目录进行监听，如果路径发生改变，则重新刷新
        FactoryBeanService.nowRootFile.addListener((observable, oldValue, newValue) -> {
            FileTreeView.INSTANCE.toggleFile(newValue);
            parseWT();
        });
    }

    private void fileInit() {
        var recentFiles = WorkSpaceService.get();
        var size = recentFiles.size();
        if (size > 0) FactoryBeanService.nowRootFile.set(new File(recentFiles.get(0).getFilePath()));
        else FactoryBeanService.nowRootFile.set(new File(Config.BASE_WORKSPACE));
        File nowRootFile = FactoryBeanService.nowRootFile.get();
        FileTreeView.INSTANCE.toggleFile(nowRootFile);
    }

    private void parseWT() {
        File nowRootFile = FactoryBeanService.nowRootFile.get();
        var contains = Arrays
                .stream(Objects.requireNonNull(nowRootFile.list()))
                .toList()
                .contains(Config.WT_CONFIG_DIR);
        FactoryBeanService.isWt.set(contains);
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
                            Platform.runLater(() -> FactoryBeanService.novelProject.set(novelProject));
                            if (FileUtil.exist(novelProject.getImg())) {
                                ImageView imageView;
                                try {
                                    imageView = new ImageView(new Image(new FileInputStream(novelProject.getImg())));
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                imageView.setFitHeight(20);
                                imageView.setFitWidth(20);
                                Platform.runLater(() -> MenuView.INSTANCE.toggleWorkSpace.setGraphic(imageView));
                            }
                        });
                    }
                }
            });
        }
    }
}
