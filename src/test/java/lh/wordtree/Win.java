package lh.wordtree;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Win extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        var box = new VBox();
        var oneWindow = new Button("OneWindow");
        oneWindow.setOnMouseClicked(e->{
            var window = new OneWindow();
            window.setMyTitle("你好世界");
            window.show();
        });
        box.setPrefSize(600, 700);
        box.getChildren().add(oneWindow);
        var scene = new Scene(box);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
