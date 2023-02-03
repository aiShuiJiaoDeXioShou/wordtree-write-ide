package lh.wordtree.views.notebook.root;

import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import lh.wordtree.component.WTNoteLeftButtonItem;
import lh.wordtree.views.notebook.bookrack.BookRackView;
import lh.wordtree.views.notebook.core.NoteCoreView;
import lh.wordtree.views.notebook.plugin.PluginView;
import lh.wordtree.views.notebook.setting.SettingView;
import lh.wordtree.views.toolbox.home.HomeScene;

import java.util.Objects;

public class NoteLeftButtonBarView extends HBox {

    public static final NoteLeftButtonBarView INSTANCE = new NoteLeftButtonBarView();
    public ListView<WTNoteLeftButtonItem> listView;

    public NoteLeftButtonBarView() {
//        this.setBorder(new Border(new BorderStroke(Paint.valueOf("#f8f9fa"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 1, 0, 0))));
        WTNoteLeftButtonItem fliesItem, writeItem, setting, plugins;
        fliesItem = new WTNoteLeftButtonItem("\uE7F4", "书籍管理");
        writeItem = new WTNoteLeftButtonItem("\uE70F", "写作");
        plugins = new WTNoteLeftButtonItem("\uE74C", "插件");
        setting = new WTNoteLeftButtonItem("\uE713", "设置");
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


    private String getStyle(String path) {
        return Objects.requireNonNull(HomeScene.class.getClassLoader().getResource(path)).toExternalForm();
    }

    private void controller() {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.getId()) {
                case "书籍管理" -> {
                    NoteBookRootView.INSTANCE.setCenter(BookRackView.INSTANCE);
                }
                case "写作" -> NoteBookRootView.INSTANCE.setCenter(NoteCoreView.INSTANCE);
                case "插件" -> NoteBookRootView.INSTANCE.setCenter(PluginView.INSTANCE);
                case "设置" -> NoteBookRootView.INSTANCE.setCenter(new SettingView());
            }
        });
    }

}
