package lh.wordtree.views.notebook.plugin;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import lh.wordtree.component.WTMessage;

public class PluginView extends VBox {
    public PluginView() {
        var button = new Button("loading");
        button.setPadding(new Insets(4, 10, 4, 10));
        this.getChildren().addAll(button);
        button.setOnMouseClicked(e->{
            WTMessage.sendError("无法使用该模块，因为没有开发！");
        });
    }

}
