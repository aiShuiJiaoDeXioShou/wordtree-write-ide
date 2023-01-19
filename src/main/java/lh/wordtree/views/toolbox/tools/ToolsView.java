package lh.wordtree.views.toolbox.tools;

import javafx.scene.layout.VBox;
import lh.wordtree.App;
import lh.wordtree.component.WTToolsLabel;
import lh.wordtree.views.notebook.root.NoteBookScene;

public class ToolsView extends VBox {

    public final static ToolsView INSTANCE = new ToolsView();

    public ToolsView() {
        this.setSpacing(20);
        var notebook = new WTToolsLabel("记事本！", "static/icon/note.png");
        notebook.setOnMouseClicked(e -> App.primaryStage.setScene(NoteBookScene.INSTANCE));
        var fictionAnalysis = new WTToolsLabel("小说数据分析", "static/icon/note.png");
        this.getChildren().addAll(notebook, fictionAnalysis);
    }

}
