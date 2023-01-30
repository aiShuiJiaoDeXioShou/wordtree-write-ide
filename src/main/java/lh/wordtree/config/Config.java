package lh.wordtree.config;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import javafx.application.Platform;
import javafx.scene.Scene;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import lh.wordtree.component.WTMessage;
import lh.wordtree.entity.NovelProject;
import lh.wordtree.utils.ConfigUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public interface Config {

    // 初始化写作工作空间项目
    static void initWriteWorkSpace(File file, NovelProject novelProject) {
        file.mkdirs();

        var outline = new File(file.getPath() + "/大纲");
        var figure = new File(outline.getPath() + "/人物.json");
        var map = new File(outline.getPath() + "/地图.json");

        var originalText = new File(file.getPath() + "/原文");
        var material = new File(file.getPath() + "/素材");

        var wt = new File(file.getPath() + "/.wordtree");
        var wtInit = new File(wt.getPath() + "/.init.json");
        var wtProject = new File(wt.getPath() + "/project.json");
        // 初始化wtProject文件
        FileUtil.writeBytes(JSON.toJSONBytes(novelProject, JSONWriter.Feature.PrettyFormat), wtProject);
        // 批量创建文件夹
        for (File f : new File[]{
                wt, outline, originalText, material
        }) {
            f.mkdirs();
        }
        // 批量创建文件
        for (File f : new File[]{
                wtInit, figure, map, wtProject
        }) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                Platform.runLater(() -> WTMessage.sendError("初始化工作空间失败,在创建名为" + f.getName() + "文件的时候发生错误！"));
            }
        }
    }

    static String getStyle(String path) {
        return Objects.requireNonNull(Config.class.getClassLoader().getResource(path)).toExternalForm();
    }

    static void setStyle(Scene scene) {
        JMetro metro;
        if (ConfigUtils.getProperties("defThemeColor").equals("light")) {
            metro = new JMetro(Style.LIGHT);
        } else metro = new JMetro(Style.DARK);
        metro.setScene(scene);
        // 必须要在setScene下面,得先设置了场景,才能设置覆盖样式
        metro.getOverridingStylesheets().addAll(
                getStyle("static/style/light.css"),
                getStyle("static/style/base.css")
        );
    }

    enum THEME {
        THEME_LIGHT(0), THEME_DART(1);
        public Integer value = 0;

        THEME(Integer value) {
            this.value = value;
        }
    }

    // 用户主目录
    String APP_CONFIG_DIR = System.getProperty("user.home") + "/" + ".wordtree";
    String WT_CONFIG_DIR = ".wordtree";

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

    enum CodeMode {
        EDITE, WT
    }
}
