package com.yangteng.library.views.notebook.main.core;

import com.yangteng.library.views.notebook.component.HideComponent;
import com.yangteng.library.views.notebook.component.SideBarComponent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class NoteCoreView extends BorderPane {
    public static final NoteCoreView INSTANCE = new NoteCoreView();
    public static final double HEIGHT = 650;
    public static final double WIDTH = 1300;

    public NoteCoreView() {
        this.setTop(NoteBookMenuView.INSTANCE);
        this.setCenter(TabMenuBarView.INSTANCE);
        this.setRight(RightToolkitsView.INSTANCE);
        var menus = LeftNoteBookFileTreeView.INSTANCE;
        {
            HideComponent borderHover = new HideComponent();
            var menusSide = new SideBarComponent(menus, () -> this.setLeft(borderHover));
            menusSide.setName("资源管理器");
            borderHover.setFunc(() -> this.setLeft(menusSide));
            this.setLeft(menusSide);
            menusSide.setBorder(new Border(new BorderStroke(Paint.valueOf("#f8f9fa"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1, 1, 0, 0))));
        }
        this.setBottom(BottomStateView.INSTANCE);
    }
}
