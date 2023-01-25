package lh.wordtree.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import jfxtras.styles.jmetro.MDL2IconFont;
import lh.wordtree.fun.OrdinaryFunction;
import lh.wordtree.views.notebook.core.NoteCoreView;

public class WTSideBarComponent extends VBox {
    private String name;
    private Label title = new Label(name);

    public WTSideBarComponent(Region node, OrdinaryFunction action) {
        title.getStyleClass().add("task-top-title");
        node.prefHeightProperty().bind(this.prefHeightProperty());
        var left = this;
        left.setPrefSize(NoteCoreView.WIDTH * 0.17, NoteCoreView.HEIGHT);
        var leftTop = new HBox();
        {
            leftTop.setPrefHeight(25);
            leftTop.prefWidthProperty().bind(left.prefWidthProperty());
            leftTop.getStyleClass().add("task-top");
        }
        leftTop.setAlignment(Pos.CENTER_RIGHT);
        leftTop.setPadding(new Insets(5, 5, 5, 0));
        var min = new Button();
        {
            var icon = new MDL2IconFont("\uE949");
            min.setGraphic(icon);
        }
        // 组件标题
        leftTop.getChildren().addAll(title, min);
        min.setOnMouseClicked(e -> action.apply());
        left.getChildren().addAll(leftTop, node);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        title.setText(name);
        title.setPadding(new Insets(0, 55, 0, 0));
    }
}
