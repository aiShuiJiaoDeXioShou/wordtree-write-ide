package lh.wordtree.task;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import javafx.application.Platform;
import lh.wordtree.comm.config.CodeLauncherConfig;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.utils.ClassLoaderUtils;
import lh.wordtree.archive.entity.Author;
import lh.wordtree.comm.BeanFactory;
import lh.wordtree.model.user.EditUserView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Task(name = "配置初始化任务", value = 3)
public class InitTask implements WTTask {
    // 在任务注册到系统的时候初始化文件目录，这样就不会造成与其他的任务相互冲突的场景
    public InitTask() {
        this.initDir();
    }

    public void init() {
        this.initUserConfig();
    }

    // 初始化配置对象
    public void initUserConfig() {
        var user = new File(Config.USER_CONFIG_PATH);
        // 如果不存在则打开初始化用户界面，要求填入用户信息
        if (!user.exists()) {
            Platform.runLater(() -> new EditUserView().show());
        } else {
            BeanFactory.user.set(JSON.parseObject(FileUtil.readBytes(user), Author.class));
        }
    }

    // 初始化目录结构
    public void initDir() {
        try {
            // 判断这个路径下面是否有.wordtree目录，没有进行创建操作
            var appConfigDirFile = new File(Config.APP_CONFIG_DIR);
            if (appConfigDirFile.exists()) return;
            var countryLanguage = new File(Config.COUNTRY_LANGUAGE);
            var sqliteFile = new File(Config.SQLITE_PATH);
            var workspace = new File(Config.WORKSPACE_PATH);
            var languageCodePath = new File(Config.LANGUAGE_CODE_PATH);
            var init = new File(Config.INIT_PATH);
            var baseWorkspace = new File(Config.BASE_WORKSPACE);
            if (!countryLanguage.exists()) {
                countryLanguage.mkdirs();
                Files.copy(ClassLoaderUtils.load(
                                "static/country-language/chinese.properties"),
                        Path.of(Config.COUNTRY_LANGUAGE + "/chinese.properties"));
                Files.copy(ClassLoaderUtils.load(
                                "static/country-language/english.properties"),
                        Path.of(Config.COUNTRY_LANGUAGE + "/english.properties"));
            }
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
                CodeLauncherConfig.LANGUAGE_CODE_DATA.forEach(lang -> {
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
