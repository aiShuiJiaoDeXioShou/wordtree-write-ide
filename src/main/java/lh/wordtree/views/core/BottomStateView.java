package lh.wordtree.views.core;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RuntimeUtil;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import lh.wordtree.comm.utils.CharsetDetectUtil;
import lh.wordtree.component.editor.WTWriterEditor;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.service.language.CountryService;

import java.util.Map;

public class BottomStateView extends BorderPane {
    private BottomStateView() {
        FactoryBeanService.nowFile.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                charsetName = CharsetDetectUtil.detect(newValue);
                code.setText(charsetName);
            }
        });
        this.getStyleClass().add("bottom-coder");
        this.setPrefHeight(height);
        var buttonBar = new HBox();
        buttonBar.setPrefHeight(height);
        buttonBar.setMaxHeight(height);
        buttonBar.getStyleClass().add("bottom-button-bar");
        buttonBar.getChildren().addAll(code, tab, nowRow, lock, terminal);
        var leftButtonBar = new HBox();
        leftButtonBar.setPadding(new Insets(0, 0, 0, 15));
        leftButtonBar.getStyleClass().add("bottom-button-bar");
        leftButtonBar.setSpacing(10);
        leftButtonBar.getChildren().addAll(git, workTime, workNumber);
        this.setLeft(leftButtonBar);
        this.setRight(buttonBar);
        controller();
    }

    public static BottomStateView newInstance() {
        return BottomStateViewHolder.instance;
    }

    private Map<String, String> language = CountryService.language;
    private final double height = 18;
    public Label code = new Label();
    public Label tab = new Label(language.get("四个空格"));
    public Label nowRow = new Label();
    public Label lock = new Label("lock");
    public Label terminal = new Label(language.get("终端"));
    public Label git = new Label("git");
    public Popup chartPopup = new Popup();
    public Label workTime = new Label();
    public Label workNumber = new Label();
    private String charsetName;

    {
        nowRow.textProperty().bind(FactoryBeanService.rowLine);
    }

    {
        lock.setOnMouseClicked(e -> {
            var nowWorkSpace = FactoryBeanService.nowWorkSpace.get();
            if (nowWorkSpace instanceof WTWriterEditor writerEditor) {
                if (lock.getStyle().contains("-fx-text-fill: #e03131")) {
                    lock.setStyle("-fx-text-fill: white");
                    writerEditor.setEditable(true);
                    return;
                }
                writerEditor.setEditable(false);
                lock.setStyle("-fx-text-fill: #e03131");
            }
        });
    }

    {
        terminal.setOnMouseClicked(e -> {
            RuntimeUtil.exec("cmd /k start");
        });
    }

    {
        git.setStyle("-fx-text-fill: #b2f2bb");
    }

    {
        var chartViews = new ListView<Label>();
        {
            chartViews.maxHeight(200);
        }
        // 获取所有可以处理的编码
        chartViews.getItems().addAll(new Label("UTF-8"), new Label("GBK"), new Label("ISO-8895-1"), new Label("UTF-16"));
        chartViews.getItems().forEach(item -> {
            item.prefWidthProperty().bind(chartViews.widthProperty());
            item.setOnMouseClicked(e -> {
                if (charsetName != null && !charsetName.equals("")) {
                    var alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("确定要转换编码格式吗？");
                    alert.setContentText("将" + charsetName + "转化为" + item.getText() + "\n转化编码格式，此过程将不可逆转！");
                    var buttonType = alert.showAndWait();
                    if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                        ThreadUtil.execAsync(() -> CharsetUtil.convert(FactoryBeanService.nowFile.get(), CharsetUtil.charset(charsetName), CharsetUtil.charset(item.getText())));
                        code.setText(item.getText());
                        alert.close();
                    }
                }

            });
        });
        chartViews.setStyle("-fx-pref-height: 170");
        chartPopup.getContent().add(chartViews);
    }

    {
        workTime.textProperty().bind(FactoryBeanService.time);
    }

    {
        workNumber.textProperty().bind(FactoryBeanService.number);
    }

    private static class BottomStateViewHolder {
        public static BottomStateView instance = new BottomStateView();
    }

    private void controller() {
        code.setOnMouseClicked(e -> {
            if (!chartPopup.isShowing()) {
                chartPopup.show(code, e.getScreenX(), e.getScreenY() - 180);
            } else chartPopup.hide();
        });
    }
}
