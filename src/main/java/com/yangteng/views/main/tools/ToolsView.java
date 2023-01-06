package com.yangteng.views.main.tools;

import com.yangteng.Index;
import com.yangteng.views.main.component.ToolsLabel;
import com.yangteng.views.notebook.main.root.NoteBookScene;
import javafx.scene.layout.VBox;

public class ToolsView extends VBox {

    public final static ToolsView INSTANCE = new ToolsView();

    public ToolsView() {
        this.setSpacing(20);
        var notebook = new ToolsLabel("记事本！", "icon/note.png");
        notebook.setOnMouseClicked(e-> Index.primaryStage.setScene(NoteBookScene.INSTANCE));
        var fictionAnalysis = new ToolsLabel("小说数据分析", "icon/note.png");
        this.getChildren().addAll(notebook, fictionAnalysis);
    }

}
