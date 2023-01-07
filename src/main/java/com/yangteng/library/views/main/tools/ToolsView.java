package com.yangteng.library.views.main.tools;

import com.yangteng.library.Index;
import com.yangteng.library.views.main.component.ToolsLabel;
import com.yangteng.library.views.notebook.main.root.NoteBookScene;
import javafx.scene.layout.VBox;

public class ToolsView extends VBox {

    public final static ToolsView INSTANCE = new ToolsView();

    public ToolsView() {
        this.setSpacing(20);
        var notebook = new ToolsLabel("记事本！", "static/icon/note.png");
        notebook.setOnMouseClicked(e-> Index.primaryStage.setScene(NoteBookScene.INSTANCE));
        var fictionAnalysis = new ToolsLabel("小说数据分析", "static/icon/note.png");
        this.getChildren().addAll(notebook, fictionAnalysis);
    }

}
