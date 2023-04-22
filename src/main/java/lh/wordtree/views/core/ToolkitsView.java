package lh.wordtree.views.core;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import lh.wordtree.views.nowfileoutline.NowFileOutlineView;
import lh.wordtree.views.task.TaskBoxView;

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
        this.setPrefWidth(NoteCoreView.WIDTH * 0.17);
        var pane = new TabPane();
        pane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        var tabTask = new Tab();
        tabTask.setText("任务管理");
        var outlineTab = new Tab();
        outlineTab.setText("大纲管理");
        outlineTab.setContent(NowFileOutlineView.newInstance());
        var toolsTab = new Tab();
        toolsTab.setText("小工具");
        toolsTab.setContent(new Label("小工具"));
        tabTask.setContent(new TaskBoxView());
        pane.getTabs().addAll(tabTask, outlineTab, toolsTab);
        this.getChildren().add(pane);
        NowFileOutlineView.newInstance().prefHeightProperty().bind(pane.prefHeightProperty());
    }

}
