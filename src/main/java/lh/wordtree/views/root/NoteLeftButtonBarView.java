package lh.wordtree.views.root;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import lh.wordtree.comm.utils.ClassLoaderUtils;
import lh.wordtree.component.CpButtonItem;
import lh.wordtree.component.CpIcon;
import lh.wordtree.plugin.WTPluginExtended;
import lh.wordtree.service.plugin.WTPluginService;
import lh.wordtree.views.bookrack.BookRackView;
import lh.wordtree.views.core.NoteCoreView;
import lh.wordtree.views.plugin.PluginView;
import lh.wordtree.views.setting.SettingView;

import java.util.Map;

public class NoteLeftButtonBarView extends HBox {
    public ListView<Node> listView;
    private WTPluginService ps = WTPluginService.pluginService;
    // 加载插件
    private Map<String, WTPluginExtended> wtPlugins = ps.extendedPlugin();

    public static NoteLeftButtonBarView newInstance() {
        return NoteLeftButtonBarViewHolder.instance;
    }

    private NoteLeftButtonBarView() {
        CpButtonItem fliesItem, writeItem, setting, plugins;
        fliesItem = new CpButtonItem("\uE7F4", "书籍管理");
        writeItem = new CpButtonItem("\uE70F", "写作");
        plugins = new CpButtonItem("\uE74C", "插件");
        setting = new CpButtonItem("\uE713", "设置");
        var bookshelf = new CpIcon(ClassLoaderUtils.url("static/icon/书架管理.png"), "书架管理");
        listView = new ListView<>();
        {
            listView.getStyleClass().add("node-left");
            listView.getItems().addAll(fliesItem, writeItem, plugins);
            listView.setPrefWidth(45);
            listView.setPadding(new Insets(10));
        }

        wtPlugins.forEach((s, extended) -> {
            var icon = new CpIcon(extended.config().icon(), s);
            listView.getItems().add(icon);
        });
        listView.getItems().add(setting);
        this.getChildren().addAll(listView);
        this.controller();
    }

    private void controller() {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.getId()) {
                case "书籍管理" -> {
                    NoteBookRootView.newInstance().setCenter(BookRackView.newInstance());
                }
                case "写作" -> NoteBookRootView.newInstance().setCenter(NoteCoreView.newInstance());
                case "插件" -> NoteBookRootView.newInstance().setCenter(PluginView.newInstance());
                case "设置" -> NoteBookRootView.newInstance().setCenter(new SettingView());
            }
            if (wtPlugins.size() > 0) {
                wtPlugins.forEach((s, extended) -> {
                    if (newValue.getId().equals(s)) {
                        NoteBookRootView.newInstance().setCenter(extended.view());
                    }
                });
            }
        });
    }

    private static class NoteLeftButtonBarViewHolder {
        public static NoteLeftButtonBarView instance = new NoteLeftButtonBarView();
    }

}
