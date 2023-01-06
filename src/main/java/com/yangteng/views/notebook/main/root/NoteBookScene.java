package com.yangteng.views.notebook.main.root;

import com.yangteng.views.main.home.HomeScene;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

import java.util.Objects;

public class NoteBookScene extends Scene {
    public final static NoteBookScene INTER = new NoteBookScene(NoteBookRootView.INTER);
    public NoteBookScene(Parent root) {
        super(root);
        this.getStylesheets().add(Objects.requireNonNull(HomeScene.class.getResource("light.css")).toExternalForm());
        this.keyMap();
    }

    /**
     * 定义全局快捷键
     */
    private void keyMap() {
        KeyCodeCombination keyCodeCombination = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        this.getAccelerators().put(keyCodeCombination, () -> {
            // 获取当前鼠标的焦点

        });
    }

}
