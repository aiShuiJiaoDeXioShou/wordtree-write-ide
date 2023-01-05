package com.yangteng.views.notebook.main.core;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class NoteCoreView {
    public static final BorderPane ROOT = new BorderPane();
    static {
        ROOT.setTop(NoteBookMenuView.INTER);
        ListView<Label> menus = new ListView<>();{
            Label none = new Label("第一章：三年之约");
            Label two = new Label("第二章：二年之约");
            Label three = new Label("第三章： 一年之约");
            Label four = new Label("第四章： 大闹云岗宗");
            menus.getItems().addAll(none, two, three, four);
            ROOT.setLeft(menus);
        }
        TextArea textArea = new TextArea();{
            textArea.setPrefRowCount(40);
            textArea.setPrefColumnCount(80);
            textArea.setPadding(new Insets(5, 5, 10, 10));
            ROOT.setCenter(textArea);
        }
    }
}
