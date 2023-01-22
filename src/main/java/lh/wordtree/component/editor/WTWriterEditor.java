package lh.wordtree.component.editor;

import cn.hutool.core.io.FileUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import lh.wordtree.service.editor.WTWriterEditorService;
import lh.wordtree.service.editor.impl.WTWriterEditorServiceImpl;
import lh.wordtree.utils.FxStyleUtils;
import lh.wordtree.views.notebook.core.TabMenuBarView;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.collection.ListModification;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;

public class WTWriterEditor extends CodeArea {

    public static final HashMap<String, String> styleMap = new HashMap<>();

    private File file;

    public WTWriterEditor(File file) {
        this.file = file;
        // 初始化语言构造工厂
        this.setPrefWidth(500);
        this.setPrefHeight(700);
        this.setPadding(new Insets(5, 5, 5, 5));
        this.setWrapText(true);
        this.getStyleClass().add("writer-editor");
//        styleMap.put("-fx-font-size", ConfigUtils.getProperties("codeFont"));
        // 初始化样式表
        FxStyleUtils.buildMapStyle(this, styleMap);
        // 添加对文本变化的监听事件
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            // 先把自动补全功能写在这里
        });
        // 添加键盘事件
        Nodes.addInputMap(this, InputMap.consume(keyPressed(S, CONTROL_DOWN), event -> {
            FileUtil.writeUtf8String(this.getText(), file);
            var tab = TabMenuBarView.INSTANCE.getSelectionModel().getSelectedItem();
            var graphic = (Text)tab.getGraphic();
            graphic.setText("");
        }));
        this.start();
    }

    private void start() {
        CodeArea codeArea = this;
        codeArea.getStyleClass().add("code-area");
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setContextMenu(new DefaultContextMenu());
//        codeArea.getVisibleParagraphs().addModificationObserver(
//                new VisibleParagraphStyler<>(codeArea, this::computeHighlighting)
//        );
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        codeArea.addEventHandler(KeyEvent.KEY_PRESSED, KE -> {
            if (KE.getCode() == KeyCode.ENTER) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(codeArea.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) Platform.runLater(() -> codeArea.insertText(caretPosition, m0.group()));
            }
        });
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        return spansBuilder.create();
    }

    private static class VisibleParagraphStyler<PS, SEG, S> implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>> {
        private final GenericStyledArea<PS, SEG, S> area;
        private final Function<String, StyleSpans<S>> computeStyles;
        private int prevParagraph, prevTextLength;

        public VisibleParagraphStyler(GenericStyledArea<PS, SEG, S> area, Function<String, StyleSpans<S>> computeStyles) {
            this.computeStyles = computeStyles;
            this.area = area;
        }

        @Override
        public void accept( ListModification<? extends Paragraph<PS, SEG, S>> lm )
        {
            if ( lm.getAddedSize() > 0 ) Platform.runLater( () ->
            {
                int paragraph = Math.min( area.firstVisibleParToAllParIndex() + lm.getFrom(), area.getParagraphs().size()-1 );
                String text = area.getText( paragraph, 0, paragraph, area.getParagraphLength( paragraph ) );

                if ( paragraph != prevParagraph || text.length() != prevTextLength )
                {
                    if ( paragraph < area.getParagraphs().size()-1 )
                    {
                        int startPos = area.getAbsolutePosition(paragraph, 0);
                        area.setStyleSpans(startPos, computeStyles.apply(text));
                    }
                    prevTextLength = text.length();
                    prevParagraph = paragraph;
                }
            });
        }
    }

    private static class DefaultContextMenu extends ContextMenu {
        private MenuItem search, translation;
        private WTWriterEditorService service = new WTWriterEditorServiceImpl();

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
