package lh.wordtree.views.root;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import lh.wordtree.comm.utils.ClassLoaderUtils;
import lh.wordtree.component.CpButtonItem;
import lh.wordtree.component.CpIcon;
import lh.wordtree.views.bookrack.BookRackView;
import lh.wordtree.views.core.NoteCoreView;
import lh.wordtree.views.plugin.PluginView;
import lh.wordtree.views.setting.SettingView;

public class NoteLeftButtonBarView extends HBox {
    public ListView<Label> listView;

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
            listView.getItems().addAll(fliesItem, writeItem, bookshelf, plugins, setting);
            listView.setPrefWidth(45);
            listView.setPadding(new Insets(10));
        }
        this.getChildren().addAll(listView);
        this.controller();
    }

    private void controller() {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.getId()) {
                case "书籍管理" -> {
                    NoteBookRootView.newInstance().setCenter(BookRackView.newInstance());
                }
                case "书架管理" -> {
                }
                case "写作" -> NoteBookRootView.newInstance().setCenter(NoteCoreView.newInstance());
                case "插件" -> NoteBookRootView.newInstance().setCenter(PluginView.newInstance());
                case "设置" -> NoteBookRootView.newInstance().setCenter(new SettingView());
            }
        });
        listView.getSelectionModel().select(2);
    }

    private static class NoteLeftButtonBarViewHolder {
        public static NoteLeftButtonBarView instance = new NoteLeftButtonBarView();
    }

}
