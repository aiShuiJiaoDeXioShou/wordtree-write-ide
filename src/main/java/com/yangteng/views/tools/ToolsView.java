package com.yangteng.views.tools;

import com.yangteng.component.ToolsLabel;
import javafx.scene.layout.VBox;

public class ToolsView extends VBox {

    public final static ToolsView INTER = new ToolsView();

    public ToolsView() {
        this.getChildren().add(new ToolsLabel("记事本！", "icon/note.png"));
    }

}
