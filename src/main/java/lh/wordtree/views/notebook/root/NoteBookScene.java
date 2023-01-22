package lh.wordtree.views.notebook.root;

import cn.hutool.core.io.FileUtil;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.text.Text;
import lh.wordtree.App;
import lh.wordtree.component.editor.WTWriterEditor;
import lh.wordtree.views.notebook.core.TabMenuBarView;

import java.nio.charset.StandardCharsets;

public class NoteBookScene extends Scene {
    public final static NoteBookScene INSTANCE = new NoteBookScene();
    public NoteBookScene() {
        super(App.rootPane);
        App.rootPane.getChildren().clear();
        App.rootPane.getChildren().add(NoteBookRootView.INSTANCE);
    }

    /**
     * 定义全局快捷键
     */
    @Deprecated
    private void keyMap() {
        // 快捷键 Ctrl + S
        KeyCodeCombination keyCodeCombination = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        this.getAccelerators().put(keyCodeCombination, () -> {
            // 保存编辑区的文本内容
            var inter = TabMenuBarView.INSTANCE;
            Tab nowTab = inter.getSelectionModel().getSelectedItem();
            if (nowTab == null) return;
            if (nowTab.getContent() instanceof WTWriterEditor content) {
                if (content.isHover()) {
                    // 保存文件
                    FileUtil.writeString(content.getText(), nowTab.getId(), StandardCharsets.UTF_8);
                    nowTab.setGraphic(new Text(""));
                }
            }
            // 说明是md编辑器
            else if (nowTab.getContent() instanceof SplitPane content) {
                if (content.getItems().get(0) instanceof WTWriterEditor coder) {
                    // 保存文件
                    FileUtil.writeString(coder.getText(), nowTab.getId(), StandardCharsets.UTF_8);
                    nowTab.setGraphic(new Text(""));
                }
            }

        });
    }

}
