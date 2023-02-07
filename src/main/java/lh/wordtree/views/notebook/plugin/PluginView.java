package lh.wordtree.views.notebook.plugin;

import cn.hutool.core.thread.ThreadUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lh.wordtree.plugin.WTPlugin;
import lh.wordtree.service.language.CountryService;
import lh.wordtree.service.plugin.WTPluginService;

import java.util.Map;

public class PluginView extends BorderPane {
    public final static PluginView INSTANCE = new PluginView();
    private final double tabW = 130;
    private final TabPane tabPane = new TabPane();
    private Map<String, String> language = CountryService.language;
    private final Tab install = new Tab(language.get("已安装"));
    private final Tab pluginMarket = new Tab(language.get("插件市场"));

    public PluginView() {
        init();
    }

    private void install() {
        ThreadUtil.execAsync(() -> {
            var marketPlugin = new ListView<MarketPlugin>();
            marketPlugin.prefWidth(tabW);
            Platform.runLater(() -> install.setContent(marketPlugin));
            showInfo(marketPlugin);
        });
    }

    private void market() {
        ThreadUtil.execAsync(() -> {
            var pluginMarket = new ListView<MarketPlugin>();
            Platform.runLater(() -> install.setContent(pluginMarket));
            var wtPlugins = WTPluginService.pluginService.sendJar();
            for (WTPlugin wtPlugin : wtPlugins) {
                var marketPlugin = new MarketPlugin(wtPlugin, true);
                Platform.runLater(() -> pluginMarket.getItems().add(marketPlugin));
            }
            showInfo(pluginMarket);
        });
    }

    private void showInfo(ListView<MarketPlugin> mp) {
        mp.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    this.setCenter(new PluginInfo(newValue.wtPlugin));
                });
    }

    private void init() {
        install();
        market();
        tabPane.getTabs().addAll(install, pluginMarket);
        tabPane.setTabMinWidth(tabW);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        this.setLeft(tabPane);
    }

    class PluginInfo extends VBox {
        public PluginInfo(WTPlugin wtPlugin) {
            var config = wtPlugin.config();
            var box = new HBox();
            var img = new ImageView(config.icon());
            img.setFitHeight(150);
            img.setFitWidth(150);

            var in = new VBox();
            var title = new Label();
            title.setStyle("-fx-font-size: 20;-fx-text-fill: skyblue;");
            title.setText(config.name());
            var a = new Label();
            a.setText("作者：" + config.author());
            var v = new Label();
            v.setText(config.version());
            v.setStyle("""
                        -fx-background-color: #38d9a9;
                        -fx-background-radius: 15;
                        -fx-padding: 2 8;
                        -fx-pref-height: 15;
                        -fx-text-fill: #ffff;
                    """);
            in.setSpacing(10);
            in.getChildren().addAll(title, a, v);
            in.setAlignment(Pos.BOTTOM_LEFT);

            box.setSpacing(10);
            box.getChildren().addAll(img, in);

            var xq = new Label(language.get("相关详情") + "：");
            xq.setStyle("-fx-font-size: 15;-fx-text-fill: #4dabf7;");
            var introduce = new Text();
            introduce.setText(config.introduce());
            introduce.setWrappingWidth(600);
            introduce.setLineSpacing(10);
            this.getChildren().addAll(box, xq, introduce);

            this.setSpacing(20);
            this.setPadding(new Insets(15, 0, 0, 30));
            this.setStyle("""
                        -fx-border-color: -def-border-color;
                        -fx-background-color: -def-backgroud-color;
                        -fx-border-width: 0 0 0 1;
                    """);
        }
    }

    class MarketPlugin extends BorderPane {
        private final WTPlugin wtPlugin;

        public MarketPlugin(WTPlugin wtPlugin, boolean download) {
            this.getStyleClass().add("wt-plugin");
            this.wtPlugin = wtPlugin;
            var config = wtPlugin.config();
            var imageView = new ImageView(config.icon());
            imageView.setFitWidth(85);
            imageView.setFitHeight(85);

            var leftBox = new VBox();
            var author = new Label(language.get("作者") + "：" + config.author());
            var name = new Label(config.name());
            leftBox.getChildren().addAll(imageView, author);
            leftBox.setSpacing(5);
            this.setLeft(leftBox);

            var introduce = new Text();
            {
                introduce.setWrappingWidth(150);
                if (config.introduce().length() > 64) {
                    var substring = config.introduce().substring(0, 64);
                    introduce.setText(substring + "......");
                } else introduce.setText(config.introduce());
            }
            var introduceBox = new VBox();
            introduceBox.setPadding(new Insets(0, 0, 0, 10));
            introduceBox.getChildren().addAll(name, introduce);
            introduceBox.setSpacing(5);
            this.setCenter(introduceBox);

            var down = new Button(language.get("下载"));
            down.setDisable(download);
            var unDown = new Button(language.get("卸载"));
            unDown.setDisable(!download);
            var box = new VBox();
            box.getChildren().addAll(down, unDown);
            box.setSpacing(5);
            this.setRight(box);
        }

        public WTPlugin getWtPlugin() {
            return wtPlugin;
        }
    }

}
