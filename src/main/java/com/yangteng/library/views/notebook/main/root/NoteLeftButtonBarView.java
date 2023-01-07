package com.yangteng.library.views.notebook.main.root;

import com.yangteng.library.views.notebook.component.NoteLeftButtonItem;
import com.yangteng.library.views.notebook.main.bookrack.BookRackView;
import com.yangteng.library.views.notebook.main.core.NoteCoreView;
import com.yangteng.library.views.notebook.main.setting.SettingView;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class NoteLeftButtonBarView extends VBox {

    public static final NoteLeftButtonBarView INSTANCE = new NoteLeftButtonBarView();
    private  NoteLeftButtonItem fliesItem,writeItem,setting;

    public NoteLeftButtonBarView() {
        this.setPrefWidth(30);
        this.setPadding(new Insets(10));
        fliesItem = new NoteLeftButtonItem("static/icon/flies.png", "书籍管理");
        writeItem = new NoteLeftButtonItem("static/icon/write.png", "写作");
        setting = new NoteLeftButtonItem("static/icon/setting.png", "设置");
        this.setSpacing(10);
        this.getChildren().addAll(fliesItem, writeItem, setting);
        this.controller();
    }

    private void controller() {
        fliesItem.setOnMouseClicked(e-> NoteBookRootView.INSTANCE.setCenter(BookRackView.INSTANCE));
        writeItem.setOnMouseClicked(e-> NoteBookRootView.INSTANCE.setCenter(NoteCoreView.INSTANCE));
        setting.setOnMouseClicked(e-> NoteBookRootView.INSTANCE.setCenter(SettingView.INSTANCE));
    }

}
