package lh.wordtree.task.edit;

import lh.wordtree.config.Config;
import lh.wordtree.config.LauncherConfig;
import lh.wordtree.task.ITask;
import lh.wordtree.task.Task;
import lh.wordtree.task.WTTask;
import lh.wordtree.utils.ClassLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Task(iTask = ITask.INIT)
public class InitTask implements WTTask {

    public void apply() {
        initConfig();
    }

    // 初始化配置对象
    public void initConfig() {
        try {
            // 判断这个路径下面是否有.wordtree目录，没有进行创建操作
            var appConfigDirFile = new File(Config.APP_CONFIG_DIR);
            if (appConfigDirFile.exists()) return;
            var sqliteFile = new File(Config.SQLITE_PATH);
            var workspace = new File(Config.WORKSPACE_PATH);
            var languageCodePath = new File(Config.LANGUAGE_CODE_PATH);
            var init = new File(Config.INIT_PATH);
            var baseWorkspace = new File(Config.BASE_WORKSPACE);
            if (!appConfigDirFile.exists()) {
                appConfigDirFile.mkdirs();
            }
            if (!sqliteFile.exists()) {

                Files.copy(ClassLoaderUtils.load("static/library.db"), Path.of(Config.SQLITE_PATH));

            }
            if (!baseWorkspace.exists()) {
                baseWorkspace.mkdirs();
            }
            if (!workspace.exists()) {
                workspace.createNewFile();
                Files.writeString(Path.of(Config.WORKSPACE_PATH), Config.WORKSPACE_DATA);
            }
            if (!languageCodePath.exists()) {
                languageCodePath.mkdirs();
                LauncherConfig.LANGUAGE_CODE_DATA.forEach(lang -> {
                    lang.forEach((k, v) -> {
                        try {
                            var path = Config.LANGUAGE_CODE_PATH + "/" + k + "-code.json";
                            if (new File(path).createNewFile()) Files.writeString(Path.of(path), v);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                });
            }
            if (!init.exists()) {
                init.createNewFile();
                Files.writeString(Path.of(Config.INIT_PATH), Config.INIT_DATA);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
