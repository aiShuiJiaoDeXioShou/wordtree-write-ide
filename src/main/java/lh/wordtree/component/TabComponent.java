package lh.wordtree.component;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Objects;

public class TabComponent extends Tab {

    private MenuItem other = new MenuItem();
    private MenuItem now = new MenuItem();
    private MenuItem all = new MenuItem();
    private MenuItem left = new MenuItem();
    private MenuItem right = new MenuItem();

    // 设置右击点击事件，关闭其他的选项卡
    public TabComponent(TabPane tabPane) {
        other.setText("关闭其他选项卡");
        now.setText("关闭当前选项卡");
        all.setText("关闭全部");
        left.setText("关闭左边");
        right.setText("关闭右边");
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(other, now, all, left, right);
        this.setContextMenu(contextMenu);
        other.setOnAction(event -> tabPane.getTabs().removeIf(tab -> !tab.equals(this)));
        now.setOnAction(event -> tabPane.getTabs().remove(this));
        all.setOnAction(event -> tabPane.getTabs().removeIf(tab -> !Objects.isNull(tab)));
        left.setOnAction(event -> tabPane.getTabs().removeIf(tab -> {
            var now = tabPane.getTabs().indexOf(this);
            var i = tabPane.getTabs().indexOf(tab);
            return i < now;
        }));
        right.setOnAction(event -> tabPane.getTabs().removeIf(tab -> {
            var now = tabPane.getTabs().indexOf(this);
            var i = tabPane.getTabs().indexOf(tab);
            return i > now;
        }));
    }
}
