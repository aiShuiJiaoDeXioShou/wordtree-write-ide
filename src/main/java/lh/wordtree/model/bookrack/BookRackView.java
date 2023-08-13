package lh.wordtree.model.bookrack;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BookRackView extends StackPane {

    private BookRackView() {
        VBox oprings = new VBox();
        Label info = new Label("个人信息");
        Label task = new Label("任务管理");
        info.setStyle("-fx-background-color: #74c0fc;-fx-background-radius: 0 30 30 0;-fx-text-fill: #ffff;");
        info.setPadding(new Insets(10, 20, 10, 20));
        task.setStyle("-fx-background-color: #ff8787;-fx-background-radius: 0 30 30 0;-fx-text-fill: #ffff;-fx-padding: 10;");
        oprings.getChildren().addAll(info, task);
        oprings.setSpacing(5);
        oprings.setMaxSize(88, 76);
        this.setAlignment(Pos.TOP_LEFT);
        this.getChildren().addAll(UserInfoView.newInstance(), oprings);

        info.setOnMouseClicked(e -> {
            this.getChildren().remove(0);
            this.getChildren().add(0, UserInfoView.newInstance());
            info.setPadding(new Insets(10, 20, 10, 20));
            task.setPadding(new Insets(10, 10, 10, 10));
        });

        task.setOnMouseClicked(e -> {
            this.getChildren().remove(0);
            this.getChildren().add(0, new Label("该功能还在开发当中..."));
            info.setPadding(new Insets(10, 10, 10, 10));
            task.setPadding(new Insets(10, 20, 10, 20));
        });
    }

    public static BookRackView newInstance() {
        return BookRackViewHolder.instance;
    }

    private static class BookRackViewHolder {
        public static BookRackView instance = new BookRackView();
    }

}
