package lh.wordtree.model.router;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import lh.wordtree.RootView;
import lh.wordtree.component.ButtonItemComponent;
import lh.wordtree.component.IconComponent;
import lh.wordtree.model.core.NoteCoreView;
import lh.wordtree.plugin.WTPluginExtended;
import lh.wordtree.plugin.WTPluginType;
import lh.wordtree.service.plugin.WTPluginService;
import lh.wordtree.model.bookrack.BookRackView;
import lh.wordtree.model.plugin.PluginView;
import lh.wordtree.model.setting.SettingView;

import java.util.Map;

public class NavigationView extends HBox {
    public ListView<Node> listView;
    private WTPluginService ps = WTPluginService.pluginService;
    // 加载插件
    private Map<String, WTPluginExtended> wtPlugins = ps.extendedPlugin();
    public  ButtonItemComponent fliesItem, writeItem, setting, plugins;
    private RootView rootView;

    public static NavigationView newInstance() {
        return NoteLeftButtonBarViewHolder.instance;
    }

    private NavigationView() {
        fliesItem = new ButtonItemComponent("\uE7F4", "书籍管理");
        writeItem = new ButtonItemComponent("\uE70F", "写作");
        plugins = new ButtonItemComponent("\uE74C", "插件");
        setting = new ButtonItemComponent("\uE713", "设置");
        listView = new ListView<>();
        {
            listView.getStyleClass().add("node-left");
            listView.getItems().addAll(fliesItem, writeItem, plugins);
            listView.setPrefWidth(45);
            listView.setPadding(new Insets(10));
        }

        wtPlugins.forEach((s, extended) -> {
            if (extended.config().type().equals(WTPluginType.menu)) {
                var icon = new IconComponent(extended.config().icon(), s);
                listView.getItems().add(icon);
            }
        });
        listView.getItems().add(setting);
        this.getChildren().addAll(listView);
        this.controller();
    }

    private void controller() {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.getId()) {
                case "书籍管理" -> {
                    RootView.newInstance().setCenter(BookRackView.newInstance());
                }
                case "写作" -> RootView.newInstance().setCenter(NoteCoreView.newInstance());
                case "插件" -> RootView.newInstance().setCenter(new PluginView());
                case "设置" -> RootView.newInstance().setCenter(new SettingView());
            }
            if (wtPlugins.size() > 0) {
                wtPlugins.forEach((s, extended) -> {
                    if (newValue.getId().equals(s)) {
                        RootView.newInstance().setCenter(extended.view());
                    }
                });
            }
        });
    }

    private static class NoteLeftButtonBarViewHolder {
        public static NavigationView instance = new NavigationView();
    }

}
