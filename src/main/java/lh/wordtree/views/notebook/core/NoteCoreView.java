package lh.wordtree.views.notebook.core;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import lh.wordtree.component.WTHide;
import lh.wordtree.component.WTIcon;
import lh.wordtree.component.WTSideBar;

public class NoteCoreView extends BorderPane {
    public static final NoteCoreView INSTANCE = new NoteCoreView();
    public static final double HEIGHT = 650;
    public static final double WIDTH = 1300;

    public NoteCoreView() {
        this.setTop(MenuView.INSTANCE);
        this.setCenter(TabMenuBarView.INSTANCE);

        var task = ToolkitsView.INSTANCE;
        {
            WTHide borderHover = new WTHide();
            var menusSide = new WTSideBar(task, () -> this.setRight(borderHover));
            menusSide.setName("扩展功能");
            menusSide.setId("code-right");
            borderHover.setFunc(() -> this.setRight(menusSide));
            this.setRight(borderHover);
            menusSide.setBorder(new Border(new BorderStroke(Paint.valueOf("#f8f9fa"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1, 1, 0, 0))));
        }

        var menus = FileTreeView.INSTANCE;
        {
            WTHide borderHover = new WTHide();
            var menusSide = new WTSideBar(menus, () -> this.setLeft(borderHover));
            menusSide.setName("资源管理器");
            menusSide.title.setGraphic(new WTIcon(new Image("static/icon/33资源.png")));
            menusSide.setId("code-left");
            borderHover.setFunc(() -> this.setLeft(menusSide));
            this.setLeft(menusSide);
            menusSide.setBorder(new Border(new BorderStroke(Paint.valueOf("#f8f9fa"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1, 1, 0, 0))));
        }
        this.setBottom(BottomStateView.INSTANCE);
    }
}
