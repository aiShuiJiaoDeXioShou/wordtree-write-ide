package lh.wordtree.plugin;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestWTPlugin implements WTPlugin {
    @Override
    public void init() {

    }

    @Override
    public void end() {

    }

    @Override
    public WTPluginConfig config() {
        return new WTPluginConfig() {

            public String name() {
                return "阅读器插件";
            }

            public String version() {
                return "1.4.1";
            }

            public String author() {
                return "林河";
            }

            public Image icon() {
                try {
                    return new Image(new FileInputStream("C:\\Users\\28322\\Pictures\\Saved Pictures\\lz.jpg"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public String introduce() {
                return """
                        《龙族》，作家江南创作的系列长篇幻想小说，
                        由《龙族Ⅰ：火之晨曦》、《龙族Ⅱ悼亡者之瞳》、《龙族Ⅲ黑月之潮》、《龙族Ⅳ奥丁之渊》，《龙族Ⅴ：悼亡者的归来》组成，
                        2009年10月1日开始在小说绘上连载，第一部于2010年04月首次出版，第二部于2011年05月出版，
                        第三部上篇于2012年12月出版，第三部中篇于2013年07月出版，第三部下篇于2013年12月出版
                        ，第四部则于2015年10月出版，第五部于2018年5月15日在QQ阅读平台开始连载。
                        """;
            }
        };
    }
}
