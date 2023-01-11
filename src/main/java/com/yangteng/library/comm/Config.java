package com.yangteng.library.comm;

import com.yangteng.library.utils.ClassLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Config {

    // 用户主目录
    String APP_CONFIG_DIR = System.getProperty("user.home") + "/" + ".wordtree";

    // 嵌入式数据库路径，如果不存在则创建该数据库
    String SQLITE_PATH = APP_CONFIG_DIR + "/wordtree.db";
    // 嵌入式数据库jdbc配置路径
    String SQLITE_JDBC_CONFIG_PATH = "jdbc:sqlite:" + SQLITE_PATH;
    // 初始化配置文件路径
    String INIT_PATH = APP_CONFIG_DIR + "/init.properties";
    // 历史空间配置文件
    String WORKSPACE_PATH = APP_CONFIG_DIR + "/workspace.json";
    // 语言资源包文件路径
    String LANGUAGE_CODE_PATH = APP_CONFIG_DIR + "/language-code";
    // base工作空间
    String BASE_WORKSPACE = APP_CONFIG_DIR + "/WorkSpace";
    // 初始化配置文件
    String INIT_DATA = """
            #-- 常规设置 --#
            #默认用户名称
            username=YangTeng
            #设置默认的主题色--[light,dark,system]
            defThemeColor=light
            #-- 编辑器设置 --#
            #默认文本编辑器字体大小
            codeFont=15
            #默认删除到回收站
            isDelRecycling=true
            """;
    // 初始化WORKSPACE数据
    String WORKSPACE_DATA = """
            [
                {
                    "filePath": "C:/Users/28322/.wordtree/WorkSpace",
                    "time": "2023-01-12 00:16:33.1509436",
                    "userName": "YangTeng"
                }
            ]
            """;

    // 初始化配置对象
    static void initConfig() throws Exception {
        // 判断这个路径下面是否有.wordtree目录，没有进行创建操作
        var appConfigDirFile = new File(Config.APP_CONFIG_DIR);
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
    }

    enum THEME {
        THEME_LIGHT(0), THEME_DART(1);
        public Integer value = 0;

        THEME(Integer value) {
            this.value = value;
        }
    }
}
