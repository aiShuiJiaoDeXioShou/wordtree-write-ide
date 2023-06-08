package lh.wordtree.editor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lh.wordtree.App;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.entity.Figure;
import lh.wordtree.comm.BeanFactory;
import lh.wordtree.service.language.WTWriterEditorService;
import lh.wordtree.service.language.WTWriterEditorServiceImpl;
import lh.wordtree.service.task.TaskService;
import lh.wordtree.task.ITask;
import lh.wordtree.views.core.TabMenuBarView;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.event.MouseOverTextEvent;
import org.fxmisc.richtext.model.PlainTextChange;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.Subscription;

import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.F;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;

public class WriterEditor extends CodeArea {
    Executor executor = Executors.newSingleThreadExecutor();
    private List<String> KEYWORDS = BeanFactory.roles.get()
            .stream().map(Figure::getName).toList();
    private SimpleObjectProperty<List<Figure>> roles = new SimpleObjectProperty<>();
    private String KEYWORD_PATTERN = "(" + String.join("|", KEYWORDS) + ")";
    private String PAREN_PATTERN = "\\(|\\)";
    private String BRACE_PATTERN = "\\{|\\}";
    private String BRACKET_PATTERN = "\\[|\\]";
    private String SEMICOLON_PATTERN = "\\;";
    private String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private File file = null;
    private final CoderPopup popup = new CoderPopup(this);
    private String source = null;
    private Tools tools = null;
    private ChangeListener<Integer> curoserMoveEvnet = (observable, oldValue, newValue) -> {
        // 获取当前row line在coder中的位置
        var paragraph = this.getCurrentParagraph() + 1;
        var column = this.getCaretColumn();
        // 获取当前选中的字符
        var selectedText = this.getSelectedText();
        // 改变底部状态栏的状态
        BeanFactory.rowLine.set(paragraph + ":" + column + "(" + selectedText.length() + "字符" + ")");
        if (selectedText.length() > 0) {
            BeanFactory
                    .rowLine
                    .set(paragraph + ":" + column +
                            "(" + selectedText.length() + "字符" + ")");
        } else {
            BeanFactory.rowLine.set(paragraph + ":" + column);
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
        if (source != null) {
            source = FileUtil.readUtf8String(file);
        }
    };
    /**
     * Ctrl+F查找字符或者替换字符操作
     */
    private Consumer<KeyEvent> c_f = (e) -> {
        if (tools == null) tools = new Tools(this);
        if (tools.isShowing()) return;
        tools.show();
    };

    public String source() {
        if (source == null) {
            source = FileUtil.readUtf8String(file);
        }
        return source;
    }

    /**
     * 检测文本编辑器文本修改事件
     */
    Consumer<List<PlainTextChange>> textChange = plainTextChanges -> {
        for (PlainTextChange plainTextChange : plainTextChanges) {
            var inserted = plainTextChange.getInserted();
            TaskService.INSTANCE.start(ITask.WRITE, inserted);
        }
    };

    public WriterEditor(File file) {
        this.file = file;
        this.init();
    }

    private void init() {
        roles.bind(BeanFactory.roles);
        this.view();
        this.listener();
        // 添加对文本变化的监听事件,先把自动补全功能写在这里,引入正在写入任务
        this.multiPlainChanges().subscribe(textChange);
        CodeArea codeArea = this;
        codeArea.getStyleClass().add("code-area");
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setContextMenu(new DefaultContextMenu());
        this.onEditing();
        this.flushWordHighligting();
        roles.addListener((observable, oldValue, newValue) -> {
            KEYWORDS = newValue.stream().map(Figure::getName).toList();
            KEYWORD_PATTERN = "(" + String.join("|", KEYWORDS) + ")";
            PATTERN = Pattern.compile(
                    "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                            + "|(?<PAREN>" + PAREN_PATTERN + ")"
                            + "|(?<BRACE>" + BRACE_PATTERN + ")"
                            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                            + "|(?<STRING>" + STRING_PATTERN + ")"
                            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
            );
            flushWordHighligting();
        });
    }

    private void flushWordHighligting() {
        Subscription subscribe = this.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .retainLatestUntilLater(executor)
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(this.multiPlainChanges())
                .filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        this.setStyleSpans(0, highlighting);
        if (tools != null && !tools.t.getText().isBlank()) tools.f_e.run();
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = this.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<>() {
            protected StyleSpans<Collection<String>> call() {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public void view() {
        this.setPrefWidth(500);
        this.setPrefHeight(700);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setWrapText(true);
        this.getStyleClass().add("writer-editor");
    }

    public void listener() {
        // 监听光标移动事件
        this.caretPositionProperty().addListener(curoserMoveEvnet);
        // 添加键盘事件
        Nodes.addInputMap(this, InputMap.consume(keyPressed(S, CONTROL_DOWN), this.c_s));
        Nodes.addInputMap(this, InputMap.consume(keyPressed(F, CONTROL_DOWN), this.c_f));
    }

    private void onEditing() {
        // 鼠标停浮事件
        this.setMouseOverTextDelay(Duration.ofSeconds(1));
        this.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
            ThreadUtil.execAsync(() -> {
                int chIdx = e.getCharacterIndex();
                Point2D pos = e.getScreenPosition();
                String source = this.getText(chIdx - 3, chIdx + 2);
                // 使用分词，获取可靠的词语
                Result parse = TokenizerUtil.createEngine().parse(source);
                AtomicReference<Word> nowWord = new AtomicReference<>();
                parse.forEach(word -> {
                    if (word.getText().contains(this.getText(chIdx, chIdx + 1))) {
                        nowWord.set(word);
                    }
                });
                Platform.runLater(() -> {
                    if (nowWord.get() != null) {
                        this.popup.info.setText("""
                                @ 词语小助手：
                                                                
                                -- 词语： %s,
                                -- 词性：%s,
                                -- 解释：%s,
                                -- 出自: %s,
                                """.formatted(nowWord.get().getText(), "", "", ""));
                        popup.isSelect.setValue(false);
                        popup.show(this, pos.getX(), pos.getY() + 20);
                    }

                });
            });

        });
        this.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> {
            if (popup.isSelect.getValue()) return;
            popup.hide();
        });
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
        public SimpleBooleanProperty isSelect = new SimpleBooleanProperty(true);
        ;
        private CodeArea codeArea;
        // 选择选项
        private ListView<Label> listView = new ListView<>();
        private String source = "";
        // 详情显示
        private Label info = new Label("");

        public CoderPopup(CodeArea code) {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(2);
            dropShadow.setOffsetY(2);
            dropShadow.setColor(Color.valueOf("#ced4da"));
            listView.setEffect(dropShadow);
            this.setWidth(200);
            this.setHeight(300);

            // 设置详情显示与页面显示位置
            info.setMinWidth(150);
            info.setMinHeight(40);
            info.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            info.setPadding(new Insets(10));
            info.setEffect(dropShadow);
            info.setTranslateX(200 + info.getMinWidth());
            this.getContent().add(info);

            this.codeArea = code;
            this.setAutoHide(true);
            Label t = new Label("Ctrl+向下箭头和向上箭头 将在编辑器的上下移动光标");
            t.setStyle("-fx-text-fill: #adb5bd;-fx-background-color: #f7f8fa;-fx-padding: 5");
            BorderPane pane = new BorderPane();
            pane.setTop(listView);
            pane.setBottom(t);
            pane.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-border-color: #ced4da; -fx-border-radius: 8; -fx-border-width: 1;");
            this.getContent().add(pane);
            Runnable f = () -> {
                if (this.isShowing()) {
                    var label = listView.getSelectionModel().getSelectedItems().get(0);
                    // 插入所执行时间的文本,获取当前光标所在位置
                    var index = codeArea.getCaretPosition();
                    // 插入代码提示文本
                    codeArea.replaceText(index - source.length(), index, label.getText());
                    this.hide();
                }
            };
            listView.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case ENTER -> {
                        f.run();
                    }
                    case LEFT, RIGHT, ESCAPE -> {
                        this.hide();
                    }
                }
            });
            listView.itemsProperty().addListener((observable, oldValue, newValue) -> {
                newValue.forEach(label -> {
                    label.setOnMouseClicked(e -> {
                        if (e.getClickCount() == 2) {
                            f.run();
                        }
                    });
                });
            });
            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) return;
                this.info.setText(newValue.getText());
            });
            isSelect.addListener((observable, oldValue, newValue) -> {
                pane.setVisible(newValue);
                if (!newValue) {
                    info.setTranslateX(0);
                } else {
                    info.setTranslateX(200 + info.getMinWidth());
                }
            });
        }

        public void popupShow() {
            // 获取现在光标的所在位置
            var bounds = codeArea.getCaretBounds().get();
            this.setX(bounds.getMaxX());
            this.setY(bounds.getMaxY());
            this.show(App.primaryStage);
            listView.requestFocus();
            listView.getSelectionModel().select(0);
            isSelect.setValue(true);
        }

        /**
         * @param list   代码提示表现
         * @param source 原文本
         */
        public void update(ObservableList<Label> list, String source) {
            listView.itemsProperty().set(list);
            this.source = source;
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

    private static class Tools extends Stage {
        MFXButton f = new MFXButton("下一个");
        MFXButton p = new MFXButton("上一个");
        MFXButton th = new MFXButton("替换");
        MFXButton thAll = new MFXButton("全部替换");
        MFXTextField y = new MFXTextField();
        MFXTextField t = new MFXTextField();
        WriterEditor we;
        StyleSpans<Collection<String>> 原样式 = null;
        StyleSpans<Collection<String>> 高亮样式 = null;
        int wordIndex = 0;

        Runnable f_e = () -> {
            StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
            int lastIndex = 0;
            Pattern pattern = Pattern.compile(y.getText());
            Matcher matcher = pattern.matcher(we.getText());
            while (matcher.find()) {
                spansBuilder.add(Collections.emptyList(), matcher.start() - lastIndex);
                spansBuilder.add(Collections.singleton("select"), matcher.end() - matcher.start());
                lastIndex = matcher.end();
            }
            spansBuilder.add(Collections.emptyList(), we.getText().length() - lastIndex);
            StyleSpans<Collection<String>> spans = spansBuilder.create();
            // 每次使用原样式去覆盖新的样式
            高亮样式 = 原样式.overlay(spans, (oldStyles, newStyles) -> {
                List<String> styles = new ArrayList<>(oldStyles);
                styles.addAll(newStyles);
                return styles;
            });
            we.setStyleSpans(0, 高亮样式);
        };

        Runnable f_e_next = () -> {
            int index = 0;
            if (高亮样式 == null) 高亮样式 = 原样式;
            StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
            int lastIndex = 0;
            Pattern pattern = Pattern.compile(y.getText());
            Matcher matcher = pattern.matcher(we.getText());
            while (matcher.find()) {
                index++;
                if (index == wordIndex) {
                    spansBuilder.add(Collections.emptyList(), matcher.start() - lastIndex);
                    spansBuilder.add(Collections.singleton("focus"), matcher.end() - matcher.start());
                    lastIndex = matcher.end();
                }
            }
            if (wordIndex > index) wordIndex = 0;
            spansBuilder.add(Collections.emptyList(), we.getText().length() - lastIndex);
            StyleSpans<Collection<String>> spans = spansBuilder.create();
            // 每次使用原样式去覆盖新的样式
            StyleSpans<Collection<String>> combinedSpans = 高亮样式.overlay(spans, (oldStyles, newStyles) -> {
                List<String> styles = new ArrayList<>(oldStyles);
                styles.addAll(newStyles);
                return styles;
            });
            we.setStyleSpans(0, combinedSpans);
        };

        EventHandler<MouseEvent> ms = (e) -> {
            Pattern pattern = Pattern.compile(y.getText());
            Matcher matcher = pattern.matcher(we.getText());
            if (matcher.find()) {
                we.replaceText(matcher.start(), matcher.end(), t.getText());
            }
            wordIndex = 0;
        };

        EventHandler<MouseEvent> msAll = (e) -> {
            Pattern pattern = Pattern.compile(y.getText());
            Matcher matcher = pattern.matcher(we.getText());
            while (matcher.find()) {
                we.replaceText(matcher.start(), matcher.end(), t.getText());
            }
            wordIndex = 0;
        };

        public Tools(WriterEditor we) {
            this.we = we;
            原样式 = we.getStyleSpans(0, we.getText().length());
            this.getIcons().add(new Image(Config.APP_ICON));

            // 实现界面初始化
            this.setAlwaysOnTop(true);
            double w = 540;
            double h = 250;
            BorderPane pane = new BorderPane();
            VBox root = new VBox();
            pane.setCenter(root);
            y.setFloatingText("查找文本");
            t.setFloatingText("替换的文本");
            y.setMinWidth(300);
            t.setMinWidth(300);
            HBox box = new HBox();
            th.getStyleClass().add("primary");
            thAll.getStyleClass().add("error");
            box.getChildren().addAll(p, f, th, thAll);
            box.setSpacing(15);
            box.setAlignment(Pos.CENTER);
            root.setSpacing(15);
            root.getChildren().addAll(y, t, box);
            root.setAlignment(Pos.CENTER);
            Scene scene = new Scene(pane, w, h);
            this.setScene(scene);
            Config.setBaseStyle(scene);
            scene.getStylesheets().add(Config.src("static/style/self.css"));
            this.setTitle("注意这是[" + we.file.getName() + "]文件的替换窗口");
            this.setOnCloseRequest(e -> {
                this.y.setText("");
                this.t.setText("");
            });
            // 对界面功能的实现
            y.textProperty().addListener((observable, oldValue, newValue) -> {
                f_e.run();
            });
            f.setOnMouseClicked(e -> {
                wordIndex++;
                f_e_next.run();
            });
            p.setOnMouseClicked(e -> {
                wordIndex--;
                if (wordIndex == -1) wordIndex = 1;
                f_e_next.run();
            });
            th.setOnMouseClicked(e -> {
                ms.handle(e);
            });
            thAll.setOnMouseClicked(msAll);
        }
    }
}
