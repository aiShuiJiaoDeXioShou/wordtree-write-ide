package lh.wordtree.views.notebook.core;

import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import lh.wordtree.component.WTHide;
import lh.wordtree.component.WTSideBarComponent;

public class NoteCoreView extends BorderPane {
    public static final NoteCoreView INSTANCE = new NoteCoreView();
    public static final double HEIGHT = 650;
    public static final double WIDTH = 1300;

    public NoteCoreView() {
        this.setTop(NoteBookMenuView.INSTANCE);
        this.setCenter(TabMenuBarView.INSTANCE);

        var task = RightToolkitsView.INSTANCE;{
            WTHide borderHover = new WTHide();
            var menusSide = new WTSideBarComponent(task, () -> this.setRight(borderHover));
            menusSide.setName("任务管理器");
            borderHover.setFunc(() -> this.setRight(menusSide));
            this.setRight(borderHover);
            menusSide.setBorder(new Border(new BorderStroke(Paint.valueOf("#f8f9fa"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1, 1, 0, 0))));
        }

        var menus = LeftNoteBookFileTreeView.INSTANCE;
        {
            WTHide borderHover = new WTHide();
            var menusSide = new WTSideBarComponent(menus, () -> this.setLeft(borderHover));
            menusSide.setName("资源管理器");
            borderHover.setFunc(() -> this.setLeft(menusSide));
            this.setLeft(menusSide);
            menusSide.setBorder(new Border(new BorderStroke(Paint.valueOf("#f8f9fa"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1, 1, 0, 0))));
        }
        this.setBottom(new BottomStateView());
    }
}
