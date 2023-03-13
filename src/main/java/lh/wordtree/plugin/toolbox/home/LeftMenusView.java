package lh.wordtree.plugin.toolbox.home;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import lh.wordtree.plugin.toolbox.library.LibraryView;
import lh.wordtree.plugin.toolbox.setting.SettingView;
import lh.wordtree.plugin.toolbox.tools.ToolsView;
import lh.wordtree.ui.controls.WTMenu;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LeftMenusView extends VBox {

    public static final LeftMenusView INSTANCE = new LeftMenusView();
    public static List<Map<WTMenu, Pane>> router;
    private Integer onMenuClickIndex = -1;

    public LeftMenusView() {
        this.setPrefWidth(200);
        this.setStyle("-fx-background-color: #34495e;");
        this.setPadding(new Insets(15));
        var index = new WTMenu(this, "首页", "static/icon/home.png");
        var lib = new WTMenu(this, "图书馆", "static/icon/lib.png");
        var tools = new WTMenu(this, "工具箱", "static/icon/tools.png");
        var setting = new WTMenu(this, "设置", "static/icon/setting.png");
        router = Arrays.asList(
                Map.of(index, IndexView.INSTANCE),
                Map.of(lib, LibraryView.INSTANCE),
                Map.of(tools, ToolsView.INSTANCE),
                Map.of(setting, SettingView.INSTANCE)
        );

        rotation(router);
    }

    private void rotation(List<Map<WTMenu, Pane>> views) {
        for (int i = 0; i < views.size(); i++) {
            Map<WTMenu, Pane> view = views.get(i);
            int finalI = i;
            view.forEach((menu, pane) -> {
                menu.setOnMouseClicked(mouseEvent -> {
                    ObservableList<Node> children = RightShowViwe.INSTANCE.getChildren();
                    if (children.size() > 0) {
                        children.remove(0);
                        children.add(pane);
                    } else {
                        children.add(pane);
                    }

                    menu.setBorder(new Border(new BorderStroke(Paint.valueOf("#3498db"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 4))));
                    if (onMenuClickIndex != -1) {
                        Map<WTMenu, Pane> oldView = views.get(onMenuClickIndex);
                        oldView.forEach((oldMenu, oldPane) -> oldMenu.setBorder(Border.EMPTY));
                    }
                    onMenuClickIndex = finalI;
                });
            });
        }
    }

}
