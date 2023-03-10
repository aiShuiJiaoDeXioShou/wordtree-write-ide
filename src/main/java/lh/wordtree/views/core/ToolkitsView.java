package lh.wordtree.views.core;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import lh.wordtree.views.task.TaskView;

// 右侧的小组件栏
public class ToolkitsView extends AnchorPane {
    public static ToolkitsView newInstance() {
        return ToolkitsViewViewHolder.instance;
    }

    private static class ToolkitsViewViewHolder {
        public static ToolkitsView instance = new ToolkitsView();
    }

    public ToolkitsView() {
        this.view();
    }

    private void view() {
        this.getStyleClass().add("right-toolkits");
        this.setPrefSize(NoteCoreView.WIDTH * 0.17, NoteCoreView.HEIGHT);
        var pane = new TabPane();
        pane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        var tabTask = new Tab();
        tabTask.setText("任务管理");
        var outlineTab = new Tab();
        outlineTab.setText("大纲管理");
        outlineTab.setContent(new Label("大纲管理"));
        var toolsTab = new Tab();
        toolsTab.setText("小工具");
        toolsTab.setContent(new Label("小工具"));
        tabTask.setContent(new TaskView());
        pane.getTabs().addAll(tabTask, outlineTab, toolsTab);
        this.getChildren().add(pane);
    }

    private void controller() {

    }
}
