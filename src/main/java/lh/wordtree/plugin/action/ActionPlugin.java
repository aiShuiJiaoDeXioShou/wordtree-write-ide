package lh.wordtree.plugin.action;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lh.wordtree.comm.utils.ClassLoaderUtils;
import lh.wordtree.plugin.WTPluginConfig;
import lh.wordtree.plugin.WTPluginExtended;
import lh.wordtree.plugin.WTPluginType;

public class ActionPlugin implements WTPluginExtended {

    private WebView webView = new WebView();

    @Override
    public Node view() {
        WebEngine engine = webView.getEngine();
        engine.load("https://linghe.site");
        return webView;
    }

    @Override
    public WTPluginConfig config() {
        return new WTPluginConfig() {
            @Override
            public String name() {
                return "活动";
            }

            @Override
            public String version() {
                return "1.0.0";
            }

            @Override
            public String author() {
                return "林河";
            }

            @Override
            public Image icon() {
                return new Image(ClassLoaderUtils.url("static/icon/社区活动.png"));
            }

            @Override
            public Image cover() {
                return new Image(ClassLoaderUtils.url("static/icon/回流区活动.png"));
            }

            @Override
            public String introduce() {
                return """
                        活动系统设计插件。
                        """;
            }

            @Override
            public WTPluginType type() {
                return WTPluginType.menu;
            }
        };
    }
}
