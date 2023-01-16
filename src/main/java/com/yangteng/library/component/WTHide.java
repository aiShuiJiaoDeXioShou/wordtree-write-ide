package com.yangteng.library.component;

import com.yangteng.library.function.OrdinaryFunction;
import com.yangteng.library.views.notebook.main.core.NoteCoreView;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import jfxtras.styles.jmetro.MDL2IconFont;

public class WTHide extends VBox {
    private OrdinaryFunction func;
    private Button show;

    public WTHide() {
        var borderHover = this;
        {
            borderHover.setPrefSize(10, NoteCoreView.HEIGHT);
        }

        show = new Button();
        {
            show.setVisible(false);
            show.setGraphic(new MDL2IconFont("\uE761"));
        }
        borderHover.getChildren().add(show);
        borderHover.hoverProperty().addListener((observable, oldValue, newValue) -> show.setVisible(observable.getValue()));

    }

    public void setFunc(OrdinaryFunction func) {
        this.func = func;
        if (func != null) {
            show.setOnMouseClicked(e -> func.apply());
        }
    }
}
