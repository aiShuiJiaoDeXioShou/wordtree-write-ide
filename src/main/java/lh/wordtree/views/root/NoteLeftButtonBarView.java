package lh.wordtree.views.root;

import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import lh.wordtree.App;
import lh.wordtree.component.CpButtonItem;
import lh.wordtree.views.bookrack.BookRackView;
import lh.wordtree.views.core.NoteCoreView;
import lh.wordtree.views.plugin.PluginView;
import lh.wordtree.views.setting.SettingView;

import java.util.Objects;

public class NoteLeftButtonBarView extends HBox {
    public ListView<CpButtonItem> listView;

    public static NoteLeftButtonBarView newInstance() {
        return NoteLeftButtonBarViewHolder.instance;
    }

    private NoteLeftButtonBarView() {
//        this.setBorder(new Border(new BorderStroke(Paint.valueOf("#f8f9fa"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 1, 0, 0))));
        CpButtonItem fliesItem, writeItem, setting, plugins;
        fliesItem = new CpButtonItem("\uE7F4", "书籍管理");
        writeItem = new CpButtonItem("\uE70F", "写作");
        plugins = new CpButtonItem("\uE74C", "插件");
        setting = new CpButtonItem("\uE713", "设置");
        listView = new ListView<>();
        {
            listView.getStyleClass().add("node-left");
            listView.getItems().addAll(fliesItem, writeItem, plugins, setting);
            listView.getSelectionModel().select(1);
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
                case "写作" -> NoteBookRootView.newInstance().setCenter(NoteCoreView.newInstance());
                case "插件" -> NoteBookRootView.newInstance().setCenter(PluginView.newInstance());
                case "设置" -> NoteBookRootView.newInstance().setCenter(new SettingView());
            }
        });
    }

    private String getStyle(String path) {
        return Objects.requireNonNull(App.class.getClassLoader().getResource(path)).toExternalForm();
    }

    private static class NoteLeftButtonBarViewHolder {
        public static NoteLeftButtonBarView instance = new NoteLeftButtonBarView();
    }

}
