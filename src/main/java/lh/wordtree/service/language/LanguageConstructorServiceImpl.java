package lh.wordtree.service.language;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lh.wordtree.App;
import lh.wordtree.component.editor.WTProgrammingEditor;
import lh.wordtree.component.editor.WTWriterEditor;
import lh.wordtree.config.Config;
import lh.wordtree.service.factory.FactoryBeanService;
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
        if (file.getName().contains(".txt")) {
            return new WTWriterEditor(file);
        } else if (file.getName().contains("地图.json")) {
            WebView webView = FactoryBeanService.getWebView();
            WebEngine engine = webView.getEngine();
            engine.load(url("static/template/map/map.html"));
            return webView;
        } else if (file.getName().contains("任务.json")) {
            WebView webView = FactoryBeanService.getWebView();
            WebEngine engine = webView.getEngine();
            engine.load(url("static/template/task/task.html"));
            return webView;
        } else if (file.getName().contains("人物.json")) {
            var languageService = new WTLanguageServiceImpl(file);
            return languageService.view();
        } else if (file.getName().contains(".md")) {
            WebView webView = FactoryBeanService.getWebView();
            WebEngine engine = webView.getEngine();
            var box = new SplitPane();
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
