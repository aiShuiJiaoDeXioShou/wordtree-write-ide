package com.yangteng.library.views.notebook.component;

import com.yangteng.library.utils.ConfigUtils;
import com.yangteng.library.utils.MyFileUtils;
import com.yangteng.library.utils.FxStyleUtils;
import com.yangteng.library.utils.LoadingLanguageUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.collection.ListModification;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyCode extends CodeArea {

    public static final HashMap<String, String> styleMap = new HashMap<>();

    private File file;

    private String[] KEYWORDS;

    private String KEYWORD_PATTERN;
    private String PAREN_PATTERN;
    private String BRACE_PATTERN;
    private String BRACKET_PATTERN;
    private String SEMICOLON_PATTERN;
    private String STRING_PATTERN;
    private String COMMENT_PATTERN;

    private Pattern PATTERN;

    public MyCode(File file) {
        this.file = file;
        // 初始化语言构造工厂
        this.initLanguageFactory();
        this.setPrefWidth(500);
        this.setPrefHeight(600);
        this.setPadding(new Insets(5, 5, 5, 5));
        this.setWrapText(true);
        styleMap.put("-fx-font-size", ConfigUtils.getProperties("codeFont"));
        // 初始化样式表
        FxStyleUtils.buildMapStyle(this, styleMap);
        this.start();
    }

    private void initLanguageFactory() {
        KEYWORDS = LoadingLanguageUtils.load(MyFileUtils.lastName(file)).code;
        KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        PAREN_PATTERN = "\\(|\\)";
        BRACE_PATTERN = "\\{|\\}";
        BRACKET_PATTERN = "\\[|\\]";
        SEMICOLON_PATTERN = "\\;";
        STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
        COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"   // for whole text processing (text blocks)
                + "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)";  // for visible paragraph processing (line by line)

        PATTERN = Pattern.compile(
                "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                        + "|(?<PAREN>" + PAREN_PATTERN + ")"
                        + "|(?<BRACE>" + BRACE_PATTERN + ")"
                        + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                        + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                        + "|(?<STRING>" + STRING_PATTERN + ")"
                        + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        );
    }

    public void start() {
        CodeArea codeArea = this;
        codeArea.getStyleClass().add("code");
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setContextMenu( new DefaultContextMenu() );
        codeArea.getVisibleParagraphs().addModificationObserver(
                new VisibleParagraphStyler<>( codeArea, this::computeHighlighting )
        );
        final Pattern whiteSpace = Pattern.compile( "^\\s+" );
        codeArea.addEventHandler( KeyEvent.KEY_PRESSED, KE -> {
            if ( KE.getCode() == KeyCode.ENTER ) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher( codeArea.getParagraph( currentParagraph-1 ).getSegments().get( 0 ) );
                if ( m0.find() ) Platform.runLater( () -> codeArea.insertText( caretPosition, m0.group() ) );
            }
        });
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private class VisibleParagraphStyler<PS, SEG, S> implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>> {
        private final GenericStyledArea<PS, SEG, S> area;
        private final Function<String,StyleSpans<S>> computeStyles;
        private int prevParagraph, prevTextLength;

        public VisibleParagraphStyler( GenericStyledArea<PS, SEG, S> area, Function<String,StyleSpans<S>> computeStyles )
        {
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
                        int startPos = area.getAbsolutePosition( paragraph, 0 );
                        area.setStyleSpans( startPos, computeStyles.apply( text ) );
                    }
                    prevTextLength = text.length();
                    prevParagraph = paragraph;
                }
            });
        }
    }

    private class DefaultContextMenu extends ContextMenu {
        private MenuItem fold, unfold, print;
        public DefaultContextMenu() {
            fold = new MenuItem( "Fold selected text" );
            fold.setOnAction( AE -> { hide(); fold(); } );

            unfold = new MenuItem( "Unfold from cursor" );
            unfold.setOnAction( AE -> { hide(); unfold(); } );

            print = new MenuItem( "Print" );
            print.setOnAction( AE -> { hide(); print(); } );

            getItems().addAll( fold, unfold, print );
        }
        private void fold() {
            ((CodeArea) getOwnerNode()).foldSelectedParagraphs();
        }
        private void unfold() {
            CodeArea area = (CodeArea) getOwnerNode();
            area.unfoldParagraphs( area.getCurrentParagraph() );
        }
        private void print() {
            System.out.println( ((CodeArea) getOwnerNode()).getText() );
        }
    }
}
