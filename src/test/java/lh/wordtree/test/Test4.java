package lh.wordtree.test;

import impl.jfxtras.styles.jmetro.FluentButtonSkin;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.FlatDialog;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class Test4 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        JMetro metro = new JMetro(Style.LIGHT);

        var flatDialog = new FlatDialog();
        var dialogPane = new DialogPane();
        var hello = new Button("hello");
        var buttonSkin = new FluentButtonSkin(hello);
        dialogPane.setContent(hello);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        dialogPane.getButtonTypes().add(ButtonType.APPLY);
        flatDialog.setDialogPane(dialogPane);

        var box = new VBox();
        box.setPrefSize(800, 800);
        var button = new Button("打开");
        hello.setOnMouseClicked(e -> flatDialog.show());
        box.getChildren().add(hello);

        var scene = new Scene(box);
        metro.setScene(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
