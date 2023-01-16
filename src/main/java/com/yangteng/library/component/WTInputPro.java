package com.yangteng.library.component;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class WTInputPro extends HBox {
    private boolean isValidation = false;
    private String labelName = "";
    private TextField textField;

    public WTInputPro(boolean isValidation, Node... node) {
        this.isValidation = isValidation;
        this.Init(node);
    }
    public WTInputPro(String labelName, Node... node) {
        this.labelName = labelName;
        this.Init(node);
    }

    public WTInputPro(String labelName, boolean isValidation, Node... node) {
        this.labelName = labelName;
        this.isValidation = isValidation;
        this.Init(node);
    }

    public WTInputPro(Node... node) {
        this.Init(node);
    }

    public boolean isValidation() {
        return isValidation;
    }

    public void setValidation(boolean validation) {
        isValidation = validation;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public TextField getTextField() {
        return textField;
    }

    private void Init(Node... nodes) {
        var number = new Label(this.labelName);
        number.setPrefWidth(100);
        if (this.isValidation) {
            var text = new Text("*");
            text.setStyle("-fx-fill: red");
            number.setGraphic(text);
        }
        textField = new TextField();
        this.getChildren().addAll(number, textField);
        for (Node node : nodes) {
            this.getChildren().add(node);
        }
        this.setSpacing(10);
    }
}
