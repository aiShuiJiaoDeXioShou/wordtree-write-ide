package lh.wordtree.views.task;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class TaskView extends VBox {
    private TaskView() {
        Button button = new Button("启动");
        this.getChildren().addAll(button);
        this.setAlignment(Pos.CENTER);
        button.setOnMouseClicked(e -> {

        });
    }

    public static TaskView newInstance() {
        return TaskView.TaskViewHolder.instance;
    }

    private void listen() {
        // 监听鼠标移动事件与键盘事件，调用win32 api

    }

    private static class TaskViewHolder {
        public static TaskView instance = new TaskView();
    }

}
