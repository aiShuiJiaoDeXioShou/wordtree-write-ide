package lh.wordtree.views.notebook.bookrack;

import cn.hutool.core.thread.ThreadUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
import lh.wordtree.component.*;
import lh.wordtree.entity.NovelProject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class BookRackView extends BorderPane {

    private BookRackView() {
        this.myLayout();
    }

    public static BookRackView newInstance() {
        return BookRackViewHolder.instance;
    }

    private static class BookRackViewHolder {
        public static BookRackView instance = new BookRackView();
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
        {
            view.getStyleClass().add("head");
        }
        var size = 240.0;
        ThreadUtil.execAsync(() -> {
            try {
                var image = new Image(
                        new FileInputStream("C:\\Users\\28322\\Pictures\\Saved Pictures\\touxiang.jpg"),
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
                Platform.runLater(() -> WTMessage.sendError("加载头像失败，请确认头像图片所在位置！"));
            }
        });
        var box2 = new VBox();
        var labelName = new Label("叶璇");
        {
            labelName.getStyleClass().add("name");
        }
        var maximName = new Label("今天也是一个晴天。");
        {
            maximName.getStyleClass().add("maxim");
        }
        box2.getChildren().addAll(labelName, maximName);
        box2.setAlignment(Pos.BOTTOM_LEFT);
        var edit = new Button("编辑个人信息");
        edit.getStyleClass().add("edit");
        var wtNoteList = new WTNoteList(List.of("共码字：90万", "小有名气", "在平台上挣到不少钱"));
        var flowPane1 = new FlowPane();
        flowPane1.setVgap(5);
        flowPane1.setHgap(10);
        flowPane1.setPadding(new Insets(10, 0, 10, 0));
        flowPane1.getChildren().addAll(
                new WTLabel("吃货"),
                new WTLabel("猫奴"),
                new WTLabel("不会煮饭"),
                new WTLabel("沸羊羊"),
                new WTLabel("聪明人"),
                new WTLabel("编程"),
                new WTLabel("JAVA"),
                new WTLabel("WEB"),
                new WTLabel("GOLANG")
        );
        authorBox.getChildren().addAll(view, box2, edit, wtNoteList, flowPane1);
        authorBox.setSpacing(15);
        leftbox.getChildren().add(authorBox);
        this.setLeft(leftbox);

        // 展示作者的作品，以及工作时间
        var centerBox = new VBox();
        var flowPane = new FlowPane();
        flowPane.setStyle("-fx-background-color: -def-backgroud-color");
        ThreadUtil.execAsync(() -> {
            for (int i = 0; i < 6; i++) {
                var novelProject = new NovelProject("斗破苍穹", "D:\\eshop\\pic\\20210706113530073900.png", "起点", "D:\\eshop\\pic\\20210706113530073900.png", "爽文", "消炎", 2000000, 120, LocalDateTime.now(), LocalDateTime.now(), "的附件是电脑黑客技术,对抗然后就舍得离开房间速度,可能会就少得可怜集散地,立刻附件是的离开和。");
                var wtBook = new WTBook(novelProject);
                Platform.runLater(() -> flowPane.getChildren().add(wtBook));
            }
            var loading = new Label("加载更多");
            {
                loading.setCursor(Cursor.HAND);
                loading.setStyle("-fx-text-fill: #74c0fc");
                loading.prefWidthProperty().bind(flowPane.widthProperty());
            }
            flowPane.getChildren().add(loading);
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
        var wtCalendar = new WTCalendar(Map.of(
                "2023-01-01", 1000,
                "2023-02-01", 1000,
                "2023-02-02", 3000,
                "2023-02-03", 4000,
                "2023-02-04", 6000,
                "2023-02-10", 8000,
                "2023-03-10", 8000,
                "2023-04-10", 8000
        ));
        diary.getChildren().add(wtCalendar);
        centerBox.getChildren().addAll(works, diary);
        this.setCenter(centerBox);
    }

}
