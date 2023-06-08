package lh.wordtree.plugin.bookshelf;

import javafx.geometry.Insets;
import lh.wordtree.comm.config.Config;
import org.fxmisc.richtext.CodeArea;

public class WTReaderCoder extends CodeArea {
    public WTReaderCoder() {
        this.getStylesheets().add(Config.src("static/style/editor/reader.css"));
        this.getStyleClass().addAll("reader");
        this.setPrefWidth(500);
        this.setPrefHeight(700);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setWrapText(true);
        // 该文本不可编辑
        this.setEditable(false);
    }
}
