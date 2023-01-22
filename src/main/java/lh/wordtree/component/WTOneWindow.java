package lh.wordtree.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lh.wordtree.comm.Config;
import org.jetbrains.annotations.NotNull;

public class WTOneWindow extends Stage {
    private double x = 0.00;
    private double y = 0.00;
    private double width = 0.00;
    private double height = 0.00;
    private final boolean isMax = false;
    private boolean isRight;// 是否处于右边界调整窗口状态
    private boolean isBottomRight;// 是否处于右下角调整窗口状态
    private boolean isBottom;// 是否处于下边界调整窗口状态
    private final double RESIZE_WIDTH = 5.00;
    private double MIN_WIDTH = 250;
    private double MIN_HEIGHT = 200;
    private double xOffset = 0, yOffset = 0;//自定义dialog移动横纵坐标

    private String myTitle;
    protected Pane root = new VBox();

    private final Label label = new Label("");
    private Scene scene;
    ;

    public String getMyTitle() {
        return myTitle;
    }

    public Pane getRoot() {
        return root;
    }

    public void setMyTitle(String myTitle) {
        this.myTitle = myTitle;
        this.label.setText(this.myTitle);
    }

    public WTOneWindow() {
        this.init();
    }

    public WTOneWindow(String myTitle) {
        this.myTitle = myTitle;
        this.label.setText(this.myTitle);
        this.init();
    }

    public WTOneWindow(Pane root, double minWidth, double minHeight) {
        this.MIN_WIDTH = minWidth;
        this.MIN_HEIGHT = minHeight;
        this.root = root;
        this.initStyle(StageStyle.TRANSPARENT);
        scene = new Scene(root, MIN_WIDTH, MIN_HEIGHT);
        this.setScene(scene);
        Config.setStyle(scene);
        this.requestFocus();
        // 添加聚焦事件，如果不聚焦在这个窗体上面，就退出应用窗口
        this.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) this.close();
        });
        this.initEvent(this);
    }


    public WTOneWindow(double minWidth, double minHeight) {
        this.MIN_WIDTH = minWidth;
        this.MIN_HEIGHT = minHeight;
        this.init();
    }

    private void init() {
        this.initStyle(StageStyle.TRANSPARENT);
        var top = new StackPane();
        top.getChildren().add(label);
        StackPane.setAlignment(label, Pos.CENTER);
        top.setPadding(new Insets(10));
        root.getChildren().add(top);
        scene = new Scene(root, MIN_WIDTH, MIN_HEIGHT);
        Config.setStyle(scene);
        this.setScene(scene);
        this.requestFocus();
        // 添加聚焦事件，如果不聚焦在这个窗体上面，就退出应用窗口
        this.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) this.close();
        });
        this.initEvent(this);
    }

    // 添加拖拽事件
    private void initEvent(@NotNull Stage primaryStage) {
        primaryStage.xProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !isMax) {
                x = newValue.doubleValue();
            }
        });
        primaryStage.yProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !isMax) {
                y = newValue.doubleValue();
            }
        });
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !isMax) {
                width = newValue.doubleValue();
            }
        });
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !isMax) {
                height = newValue.doubleValue();
            }
        });

        root.setOnMouseMoved((MouseEvent event) -> {
            event.consume();
            double x = event.getSceneX();
            double y = event.getSceneY();
            double width = primaryStage.getWidth();
            double height = primaryStage.getHeight();
            Cursor cursorType = Cursor.DEFAULT;// 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型
            // 先将所有调整窗口状态重置
            isRight = isBottomRight = isBottom = false;
            if (y >= height - RESIZE_WIDTH) {
                if (x <= RESIZE_WIDTH) {// 左下角调整窗口状态
                    //不处理

                } else if (x >= width - RESIZE_WIDTH) {// 右下角调整窗口状态
                    isBottomRight = true;
                    cursorType = Cursor.SE_RESIZE;
                } else {// 下边界调整窗口状态
                    isBottom = true;
                    cursorType = Cursor.S_RESIZE;
                }
            } else if (x >= width - RESIZE_WIDTH) {// 右边界调整窗口状态
                isRight = true;
                cursorType = Cursor.E_RESIZE;
            }
            // 最后改变鼠标光标
            root.setCursor(cursorType);
        });

        root.setOnMouseDragged((MouseEvent event) -> {

            //根据鼠标的横纵坐标移动dialog位置
            event.consume();
            if (yOffset != 0) {
                primaryStage.setX(event.getScreenX() - xOffset);
                if (event.getScreenY() - yOffset < 0) {
                    primaryStage.setY(0);
                } else {
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            }

            double x = event.getSceneX();
            double y = event.getSceneY();
            // 保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
            double nextX = primaryStage.getX();
            double nextY = primaryStage.getY();
            double nextWidth = primaryStage.getWidth();
            double nextHeight = primaryStage.getHeight();
            if (isRight || isBottomRight) {// 所有右边调整窗口状态
                nextWidth = x;
            }
            if (isBottomRight || isBottom) {// 所有下边调整窗口状态
                nextHeight = y;
            }
            if (nextWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
                nextWidth = MIN_WIDTH;
            }
            if (nextHeight <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
                nextHeight = MIN_HEIGHT;
            }
            // 最后统一改变窗口的x、y坐标和宽度、高度，可以防止刷新频繁出现的屏闪情况
            primaryStage.setX(nextX);
            primaryStage.setY(nextY);
            primaryStage.setWidth(nextWidth);
            primaryStage.setHeight(nextHeight);

        });
        //鼠标点击获取横纵坐标
        root.setOnMousePressed(event -> {
            event.consume();
            xOffset = event.getSceneX();
            if (event.getSceneY() > 46) {
                yOffset = 0;
            } else {
                yOffset = event.getSceneY();
            }
        });
    }
}
