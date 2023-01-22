package lh.wordtree.service.impl;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.web.WebView;
import lh.wordtree.App;
import lh.wordtree.comm.Config;
import lh.wordtree.component.editor.WTProgrammingEditor;
import lh.wordtree.component.editor.WTWriterEditor;
import lh.wordtree.service.editor.LanguageConstructorService;
import lh.wordtree.service.editor.MdParseService;
import lh.wordtree.utils.WTFileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class LanguageConstructorServiceImpl implements LanguageConstructorService {
    private final File file;

    public LanguageConstructorServiceImpl(File file) {
        this.file = file;
    }

    public Node build() {
        if (file.getName().contains("地图.json")) {
            var webView = new WebView();
            var engine = webView.getEngine();
            engine.load(url("static/template/map.html"));
            return webView;
        } else if (file.getName().contains("任务.json")) {
            var webView = new WebView();
            var engine = webView.getEngine();
            engine.load(url("static/template/task.html"));
            return webView;
        } else if (file.getName().contains("人物.json")) {
            var webView = new WebView();
            var engine = webView.getEngine();
            engine.load(url("static/template/task.html"));
            return webView;
        } else if (file.getName().contains(".md")) {
            var box = new SplitPane();
            var webView = new WebView();
            var engine = webView.getEngine();
            var code = new WTWriterEditor(file);
            code.textProperty().addListener((observable, oldValue, newValue) -> {
                var s = MdParseService.mdParse(newValue);
                engine.loadContent(s);
            });
            box.getItems().addAll(code, webView);
            return box;
        } else if (file.getName().contains(".wt")) {
            return new WTWriterEditor(file);
        } else if (file.getName().contains(".wtx")) {
            return new WTWriterEditor(file);
        } else if (file.getName().contains(".wtm")) {
            return new WTWriterEditor(file);
        } else {
            // 对编程语言的支持。
            var programmings = Arrays
                    .stream(Objects.requireNonNull(new File(Config.LANGUAGE_CODE_PATH).listFiles()))
                    .map(File::getName)
                    .map(WTFileUtils::firstName).toList();
            if (programmings.contains(WTFileUtils.lastName(file.getName()))) return new WTProgrammingEditor();
        }
        return new WTWriterEditor(file);
    }

    private String url(String path) {
        return Objects.requireNonNull(App.class.getClassLoader().getResource(path)).toExternalForm();
    }

}
