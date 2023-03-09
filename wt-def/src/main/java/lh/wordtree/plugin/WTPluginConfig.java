package lh.wordtree.plugin;

import javafx.scene.image.Image;

public interface WTPluginConfig {
    String name();

    String version();

    String author();

    Image icon();

    /**
     * 有关插件的介绍
     *
     * @return
     */
    String introduce();

    WTPluginType type();

    /**
     * 解析的文件后缀名称
     */
    default String file() {
        return ".*";
    }
}
