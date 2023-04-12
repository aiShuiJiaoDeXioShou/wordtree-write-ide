package lh.wordtree.component.editor;

import cn.hutool.core.io.FileUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import lh.wordtree.App;
import lh.wordtree.comm.utils.FxStyleUtils;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.service.language.WTWriterEditorService;
import lh.wordtree.service.language.WTWriterEditorServiceImpl;
import lh.wordtree.service.task.TaskService;
import lh.wordtree.task.ITask;
import lh.wordtree.views.core.TabMenuBarView;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.PlainTextChange;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;

public class WTWriterEditor extends CodeArea {

    public static final HashMap<String, String> styleMap = new HashMap<>();

    private File file = null;
    private final CoderPopup popup = new CoderPopup(this);
    private ChangeListener<Integer> curoserMoveEvnet = (observable, oldValue, newValue) -> {
        // 获取当前row line在coder中的位置
        var paragraph = this.getCurrentParagraph() + 1;
        var column = this.getCaretColumn();
        // 获取当前选中的字符
        var selectedText = this.getSelectedText();
        // 改变底部状态栏的状态
        FactoryBeanService.rowLine.set(paragraph + ":" + column + "(" + selectedText.length() + "字符" + ")");
        if (selectedText.length() > 0) {
            FactoryBeanService
                    .rowLine
                    .set(paragraph + ":" + column +
                            "(" + selectedText.length() + "字符" + ")");
        } else {
            FactoryBeanService.rowLine.set(paragraph + ":" + column);
        }
    };

    /**
     * Ctrl+S保存文件操作
     */
    private Consumer<KeyEvent> c_s = (e) -> {
        FileUtil.writeUtf8String(this.getText(), file);
        var tab = TabMenuBarView.newInstance().getSelectionModel().getSelectedItem();
        var graphic = (Text) tab.getGraphic();
        graphic.setText("");
    };

    /**
     * 检测文本编辑器文本修改事件
     */
    Consumer<List<PlainTextChange>> textChange = plainTextChanges -> {
        for (PlainTextChange plainTextChange : plainTextChanges) {
            var inserted = plainTextChange.getInserted();
            TaskService.INSTANCE.start(ITask.WRITE, inserted);
        }
    };

    public WTWriterEditor(File file) {
        this.file = file;
        this.init();
    }

    private void init() {
        this.view();
        this.listener();
        // 添加对文本变化的监听事件,先把自动补全功能写在这里,引入正在写入任务
        this.multiPlainChanges().subscribe(textChange);
        CodeArea codeArea = this;
        codeArea.getStyleClass().add("code-area");
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setContextMenu(new DefaultContextMenu());
        this.onEditing();
    }

    public void view() {
        this.setPrefWidth(500);
        this.setPrefHeight(700);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setWrapText(true);
        this.getStyleClass().add("writer-editor");
        // 初始化样式表
        FxStyleUtils.buildMapStyle(this, styleMap);
    }

    public void listener() {
        // 监听光标移动事件
        this.caretPositionProperty().addListener(curoserMoveEvnet);
        // 添加键盘事件
        Nodes.addInputMap(this, InputMap.consume(keyPressed(S, CONTROL_DOWN), this.c_s));
    }

    private void onEditing() {
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        this.addEventHandler(KeyEvent.KEY_PRESSED, KE -> {
            if (KE.getCode() == KeyCode.ENTER) {
                int caretPosition = this.getCaretPosition();
                int currentParagraph = this.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(this.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) Platform.runLater(() -> this.insertText(caretPosition, m0.group()));
            }
        });
        // 将tab将转化为四个空格
        this.addEventFilter(KeyEvent.KEY_PRESSED, KE -> {
            if (KE.getCode() == KeyCode.TAB) {
                int caretPosition = this.getCaretPosition();
                Platform.runLater(() -> {
                    this.insertText(caretPosition, "\s\s\s\s");
                });
                KE.consume();
            }
        });
    }

    public CoderPopup popup() {
        return popup;
    }

    public static class CoderPopup extends Popup {
        private ListView<Label> listView;
        private CodeArea codeArea;

        public CoderPopup(CodeArea code) {
            this.codeArea = code;
            this.setAutoHide(true);
        }

        public void popupShow() {
            // 获取现在光标的所在位置
            var bounds = codeArea.getCaretBounds().get();
            this.setX(bounds.getMaxX());
            this.setY(bounds.getMaxY());
            this.show(App.primaryStage);
        }

        public void update(List<String> list) {
            if (this.getContent().size() > 0) {
                this.getContent().remove(listView);
            }
            listView = new ListView<>();
            for (String source : list) {
                var label = new Label(source);
                listView.getItems().add(label);
            }
            this.getContent().add(listView);
        }

    }
    private static class DefaultContextMenu extends ContextMenu {
        private final MenuItem search;
        private final MenuItem translation;
        private final WTWriterEditorService service = new WTWriterEditorServiceImpl();

        public DefaultContextMenu() {
            search = new MenuItem("查找");
            translation = new MenuItem("翻译");
            search.setOnAction(AE -> {
                hide();
                service.search("");
            });
            getItems().addAll(search, translation);
        }
    }
}
