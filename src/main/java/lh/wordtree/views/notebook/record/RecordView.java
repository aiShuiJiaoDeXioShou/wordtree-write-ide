package lh.wordtree.views.notebook.record;

import cn.hutool.core.date.LocalDateTimeUtil;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lh.wordtree.component.WTOneWindow;
import lh.wordtree.entity.Record;


public class RecordView extends BorderPane {
    private final RecordViewModel recordViewModel = new RecordViewModel();
    private final Label add = new Label("新增");
    private final Label lookList = new Label("查看记录");
    private final Label translation = new Label("翻译");
    private final ListView<Label> menuList = new ListView<>();

    public RecordView() {
        menuList.getItems().addAll(add, lookList, translation);
        add.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2) {
                // 获取剪切板的内容
                var systemClipboard = Clipboard.getSystemClipboard();
                var html = systemClipboard.getString();
                var recordMapper = recordViewModel.getRecordMapper();
                recordMapper.insert(new Record(0, html, LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyy年MM月dd日 HH:mm:ss")));
            }
        });
        lookList.setOnMouseClicked(e -> {
            if (e.getClickCount() < 2) return;
            var recordListView = new RecordListView();
            recordListView.show();
        });
        this.setCenter(menuList);
    }

    private class RecordListView extends WTOneWindow {
        private final ListView<Record> list = new ListView<>();
        private final BorderPane menuBorderPane = new BorderPane();
        private final Button search = new Button("搜索");
        private final TextField input = new TextField();

        public RecordListView() {
            super("记录详情");
            label.setStyle("-fx-text-fill: #ffff");
            top.setStyle("-fx-background-color: #495057");
            bindViewModel();
            var box = new HBox();
            box.setStyle("-fx-background-color: white");
            box.setPadding(new Insets(5, 0, 5, 10));
            box.setSpacing(20);
            box.getChildren().addAll(input, search);
            var vBox = new VBox();
            vBox.getChildren().addAll(box, list);
            menuBorderPane.setLeft(vBox);
            root.getChildren().addAll(menuBorderPane);
        }

        private void bindViewModel() {
            list.setItems(recordViewModel.getListProperty());
        }
    }

}
