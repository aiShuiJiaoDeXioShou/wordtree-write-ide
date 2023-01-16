package com.yangteng.library.component;

import javafx.scene.control.Alert;

public interface WTFxAlert {
    static void show(String text,Alert.AlertType... alertType) {
        var alert = new Alert(Alert.AlertType.WARNING);
        if (alertType.length > 0) {
            alert.setAlertType(alertType[0]);
        }
        alert.setTitle(text);
        alert.show();
    }
}
