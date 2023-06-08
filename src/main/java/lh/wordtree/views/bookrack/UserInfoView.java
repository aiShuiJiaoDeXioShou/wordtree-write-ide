package lh.wordtree.views.bookrack;

import cn.hutool.core.thread.ThreadUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import lh.wordtree.component.BookComponent;
import lh.wordtree.component.SystemMessage;
import lh.wordtree.ui.controls.WTCalendar;
import lh.wordtree.ui.controls.WTLabel;
import lh.wordtree.ui.controls.WTNoteList;
import lh.wordtree.views.login.OneLoginView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UserInfoView extends BorderPane {
    private final UserInfoViewModel bm = new UserInfoViewModel();

    private UserInfoView() {
        // 设置样式
        this.myLayout();
    }

    public static UserInfoView newInstance() {
        return UserInfoViewHolder.instance;
    }

    private void myLayout() {
        this.getStyleClass().add("book-rack");
        this.setPrefSize(700, 600);
        this.setPadding(new Insets(0, 0, 10, 0));

        // 展示作者信息
        var leftbox = new HBox();
        leftbox.setPadding(new Insets(30, 0, 30, 30));
        var authorBox = new VBox();
        var view = new ImageView();
        view.getStyleClass().add("head");
        HBox box = new HBox();

        box.getChildren().addAll(view);
        var size = 240.0;
        ThreadUtil.execAsync(() -> {
            try {
                var image = new Image(
                        new FileInputStream(bm.user().imgPath()),
                        size, size, true, true);
                Platform.runLater(() -> {
                    view.setImage(image);
                    var circle = new Circle();
                    circle.setFill(Paint.valueOf("aqua"));
                    circle.setRadius(size / 2);
                    circle.setCenterX(size / 2);
                    circle.setCenterY(size / 2);
                    view.setClip(circle);
                });
            } catch (FileNotFoundException e) {
                Platform.runLater(() -> SystemMessage.sendError("加载头像失败，请确认头像图片所在位置！"));
            }
        });
        var box2 = new VBox();
        var labelName = new Label();
        {
            labelName.textProperty().set(bm.user().name());
            labelName.getStyleClass().add("name");
        }
        var maximName = new Label();
        {
            maximName.textProperty().set(bm.user().signature());
            maximName.getStyleClass().add("maxim");
        }
        box2.getChildren().addAll(labelName, maximName);
        box2.setAlignment(Pos.BOTTOM_LEFT);
        var edit = new Button("编辑个人信息");
        edit.getStyleClass().add("edit");
        edit.setOnMouseClicked(e -> {
            OneLoginView login = new OneLoginView();
            login.show();
        });

        // 他的成就
        var wtNoteList = new WTNoteList(bm.user().accomplishment());
        var flowPane1 = new FlowPane();
        flowPane1.setVgap(5);
        flowPane1.setHgap(10);
        flowPane1.setPadding(new Insets(10, 0, 10, 0));
        bm.user().tag().forEach(data -> flowPane1.getChildren().add(new WTLabel(data)));
        authorBox.getChildren().addAll(box, box2, edit, wtNoteList, flowPane1);
        authorBox.setSpacing(15);
        leftbox.getChildren().add(authorBox);
        this.setLeft(leftbox);

        // 展示作者的作品，以及工作时间
        var centerBox = new VBox();
        var flowPane = new FlowPane();
        flowPane.setStyle("-fx-background-color: -def-backgroud-color");
        ThreadUtil.execAsync(() -> {
            bm.novelProjects().forEach(np -> {
                Platform.runLater(() -> flowPane.getChildren().add(new BookComponent(np)));
            });
        });
        var scrollPane = new ScrollPane();
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        {
            scrollPane.setContent(flowPane);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }

        var works = new VBox();
        var title = new Label("作品：");
        title.getStyleClass().add("title");
        flowPane.prefWidthProperty().bind(works.widthProperty());
        flowPane.prefHeightProperty().bind(works.heightProperty());
        works.getChildren().addAll(title, scrollPane);
        works.prefHeightProperty().bind(this.heightProperty().divide(2));
        works.setPadding(new Insets(10, 0, 0, 0));


        // 显示工作情况
        var diary = new HBox();
        var wtCalendar = new WTCalendar(bm.getWorkPlan());
        diary.getChildren().add(wtCalendar);
        centerBox.getChildren().addAll(works, diary);
        this.setCenter(centerBox);
    }

    private static class UserInfoViewHolder {
        public static UserInfoView instance = new UserInfoView();
    }
}
