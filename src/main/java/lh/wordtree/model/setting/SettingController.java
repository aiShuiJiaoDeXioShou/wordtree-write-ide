package lh.wordtree.model.setting;

import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class SettingController {
    private SettingView view;
    private SettingModel model;

    public SettingController(SettingView view) {
        this.view = view;
        view.treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::leftTreeAction);
        view.apply.setOnMouseClicked(this::ok);
    }

    private void ok(MouseEvent event) {
        var center = view.borderPane.getCenter();
        if (center instanceof TextArea) {
            // 对配置文件重新设置
            model.saveSettingText(((TextArea) center).getText());
        }
    }

    private void leftTreeAction(MouseEvent event) {
        var node = event.getPickResult().getIntersectedNode();
        if (node instanceof Text) {
            var text = ((Text) node).getText();
            TextArea textArea = null;
            switch (text) {
                case "常规设置" -> {
                    var settingValue = model.getSettingValue();
                    if (textArea == null) textArea = new TextArea();
                    textArea.setText(settingValue);
                    view.borderPane.setCenter(textArea);
                }
                case "编辑器设置" -> {

                }
            }
        }
    }
}
