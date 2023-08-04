package lh.wordtree.views.core;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import lh.wordtree.plugin.WTPluginExtended;
import lh.wordtree.plugin.WTPluginType;
import lh.wordtree.service.plugin.WTPluginService;
import lh.wordtree.views.nowfileoutline.NowFileOutlineView;
import lh.wordtree.views.task.TaskBoxView;

import java.util.Map;

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
        tabTask.setContent(new TaskBoxView());

        var outlineTab = new Tab();
        outlineTab.setText("大纲管理");
        outlineTab.setContent(NowFileOutlineView.newInstance());

        pane.getTabs().addAll(tabTask, outlineTab);
        registerToolbarPlugin(pane);

        this.getChildren().add(pane);
        NowFileOutlineView.newInstance().prefHeightProperty().bind(pane.prefHeightProperty());
    }

    /**
     * 注册工具栏插件
     */
    private void registerToolbarPlugin(TabPane tabPane) {
        WTPluginService ps = WTPluginService.pluginService;
        // 加载插件
        Map<String, WTPluginExtended> wtPlugins = ps.extendedPlugin();
        wtPlugins.forEach((pluginName, wtPluginExtended) -> {
            if (wtPluginExtended.config().type().equals(WTPluginType.toolBar)) {
                Tab tab = new Tab(pluginName);
                tab.setContent(wtPluginExtended.view());
                tabPane.getTabs().add(tab);
            }
        });
    }

}
