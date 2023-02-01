package lh.wordtree;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MyCoder extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        var textArea = new TextArea();
        primaryStage.setScene(new Scene(textArea));
        primaryStage.show();
    }
}
