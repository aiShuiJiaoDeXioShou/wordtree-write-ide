package lh.wordtree.uitest;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

public class Coder extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        var box = new BorderPane();
        var codeArea = new CodeArea();
        var scene = new Scene(box, 800, 600);
        box.prefWidth(800);
        box.prefHeight(600);
        box.setCenter(codeArea);
        codeArea.setStyle("-fx-font-size: 20;");
        var popup = new Popup();
        var listView = new ListView<Label>();
        for (int i = 0; i < 10; i++) {
            var text = new Label("Hello World!"+i);
            text.setCursor(Cursor.HAND);
            listView.getItems().add(text);
        }
        popup.getContent().add(listView);
        popup.setAutoHide(true);
        codeArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.contains("萧")) return;
            // 获取现在光标的所在位置
            var bounds = codeArea.getCaretBounds().get();
            popup.setX(bounds.getMaxX());
            popup.setY(bounds.getMaxY());
            popup.show(stage);
        });
        // 快捷键 Enter
        listView.setOnKeyPressed(event -> {
           switch (event.getCode()) {
               case ENTER -> {
                   if (popup.isShowing()) {
                       var label = listView.getSelectionModel().getSelectedItems().get(0);
                       // 插入所执行时间的文本,获取当前光标所在位置
                       var index = codeArea.getCaretPosition();
                       codeArea.insertText(index, label.getText());
                       popup.hide();
                   }
               }
               case LEFT,RIGHT,ESCAPE -> {
                   popup.hide();
               }
           }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
