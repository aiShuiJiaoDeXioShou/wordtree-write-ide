package lh.wordtree.views.toolbox.home;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lh.wordtree.utils.FXBuildUtils;

public class RightShowViwe extends VBox {

    public static final RightShowViwe INSTANCE = new RightShowViwe();

    public RightShowViwe() {
        this.setPrefWidth(800);
        this.setPadding(new Insets(20));
        // 关于界面的编写
        {
            var hello = new Label("欢迎使用小腾工具箱！");
            var shortcutKey = new Label("Alt + e 快速关闭和打开文件树");
            new FXBuildUtils<Label>()
                    .addAll(hello, shortcutKey)
                    .StyleUtil(label -> label.getStyleClass().add("embedded-prompt"));
            var show = new VBox();{
                show.setSpacing(10);
                show.setAlignment(Pos.CENTER);
                show.getChildren().addAll(hello, shortcutKey);
                show.prefHeightProperty().bind(this.heightProperty());
                show.prefWidthProperty().bind(this.widthProperty());
            }

            this.getChildren().addAll(show);
        }

    }

}
