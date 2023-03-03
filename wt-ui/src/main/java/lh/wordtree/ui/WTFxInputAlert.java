package lh.wordtree.ui;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lh.wordtree.ui.utils.SvgUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class WTFxInputAlert extends WTOneWindow {
    public TextField textField;
    private ListView<AlertTask> listView = new ListView<>();
    private List<AlertTask> alertTasks;
    private AtomicReference<AlertTask> index = new AtomicReference<>();
    private double width = 290;
    private Function fnc;

    public WTFxInputAlert(String text, Function fnc) {
        super(new VBox(), 240, 60);
        this.fnc = fnc;
        init(text);
    }

    public WTFxInputAlert(String text, List<AlertTask> alertTasks) {
        init(text);
        this.alertTasks = alertTasks;
        alertTasks.forEach(alertTask -> listView.getItems().add(alertTask));
        listView.setCellFactory(param -> new ListCell<>() {
            protected void updateItem(AlertTask item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    var label = new Label(item.taskName);
                    if (!Objects.equals(item.icon, "")) {
                        var image = SvgUtils.imageFromSvg(WTFxInputAlert.class.getClassLoader().getResourceAsStream(item.icon));
                        var imageView = new ImageView(image);
                        imageView.setFitWidth(15);
                        imageView.setFitHeight(15);
                        label.setGraphic(imageView);
                    }
                    setGraphic(label);
                }
                ;
            }
        });
        listView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    index.set(newValue);
                });
        root.getChildren().add(listView);
    }

    private void init(String text) {
        var box = new HBox();
        textField = new TextField();
        textField.setStyle("""
                   -fx-border-width: 0;
                   -fx-border-radius: 0;
                   -fx-background-color: none;
                """);
        var label = new Label(text);
        box.getChildren().add(textField);
        box.setStyle("-fx-background-color: #ffff;-fx-border-width: 1px;-fx-border-color: #cccc;");
        label.setStyle("""
                    -fx-background-color: #232323;
                    -fx-text-fill: #ffff;
                    -fx-text-alignment: center;
                    -fx-alignment: center;
                    -fx-padding: 6 0;
                """);
        label.setPrefWidth(width + 10);
        textField.setPrefWidth(width);
        textField.setPrefHeight(40);
        root.getChildren().addAll(label, box);
        textField.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> {
                    if (index.get() != null) {
                        index.get().fnc.apply(this);
                    } else {
                        this.fnc.apply(this);
                    }
                    this.hide();
                }
            }
        });
    }

    public interface Function {
        void apply(WTFxInputAlert alert);
    }

    public record AlertTask(String taskName, String icon, Function fnc) {
    }
}
