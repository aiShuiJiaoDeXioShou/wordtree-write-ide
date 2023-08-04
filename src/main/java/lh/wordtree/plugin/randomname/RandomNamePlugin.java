package lh.wordtree.plugin.randomname;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import lh.wordtree.plugin.WTPluginConfig;
import lh.wordtree.plugin.WTPluginExtended;
import lh.wordtree.plugin.WTPluginType;

import java.io.IOException;

public class RandomNamePlugin implements WTPluginExtended {

    @Override
    public Node view() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RandomNameFXML.fxml"));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WTPluginConfig config() {
        return new WTPluginConfig() {
            @Override
            public String name() {
                return "起名工具";
            }

            @Override
            public String version() {
                return "1.0";
            }

            @Override
            public String author() {
                return "linghe";
            }

            @Override
            public Image icon() {
                return null;
            }

            @Override
            public Image cover() {
                return null;
            }

            @Override
            public String introduce() {
                return """
                        官方起名扩展。
                        """;
            }

            @Override
            public WTPluginType type() {
                return WTPluginType.toolBar;
            }
        };
    }
}
