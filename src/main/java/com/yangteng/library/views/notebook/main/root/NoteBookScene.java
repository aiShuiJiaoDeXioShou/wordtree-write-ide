package com.yangteng.library.views.notebook.main.root;

import com.yangteng.library.views.notebook.component.MyCode;
import com.yangteng.library.views.notebook.main.core.TabMenuBarView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NoteBookScene extends Scene {
    public final static NoteBookScene INSTANCE = new NoteBookScene(NoteBookRootView.INSTANCE);
    public NoteBookScene(Parent root) {
        super(root);
        this.keyMap();
    }

    /**
     * 定义全局快捷键
     */
    private void keyMap() {
        // 快捷键 Ctrl + S
        KeyCodeCombination keyCodeCombination = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        this.getAccelerators().put(keyCodeCombination, () -> {
            // 保存编辑区的文本内容
            var inter = TabMenuBarView.INSTANCE;
            Tab nowTab = inter.getSelectionModel().getSelectedItem();
            MyCode content = (MyCode)nowTab.getContent();
            if (content.isHover()) {
                try {
                    // 保存文件
                    Files.writeString(Path.of(nowTab.getId()), content.getText());
                    nowTab.setGraphic(new Text(""));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
