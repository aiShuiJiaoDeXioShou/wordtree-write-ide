package lh.wordtree.component;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

/**
 * 自动补全Popup
 */
public class WTPopup extends Popup {

    /**
     * 代码提示的选中区域
     */
    private final VBox selectBox = new VBox();

    /**
     * 代码提示的说明区域
     */
    private final BorderPane description = new BorderPane();

    /**
     * 根
     */
    private final HBox root = new HBox();

    public WTPopup() {
        this.init();
        this.layout();
        this.controller();
    }

    private void layout() {
        root.getChildren().addAll(selectBox,description);
        this.getContent().add(root);
    }

    private void init() {
        this.setAutoHide(true);
    }

    private void controller() {

    }

}
