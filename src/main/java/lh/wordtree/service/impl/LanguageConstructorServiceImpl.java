package lh.wordtree.service.impl;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.web.WebView;
import lh.wordtree.App;
import lh.wordtree.component.WTCoder;
import lh.wordtree.service.LanguageConstructorService;
import lh.wordtree.service.MdParseService;

import java.io.File;

public class LanguageConstructorServiceImpl implements LanguageConstructorService {
    private File file;

    public LanguageConstructorServiceImpl(File file) {
        this.file = file;
    }

    public Node build() {
        if (file.getName().equals("地图.json")) {
            var webView = new WebView();
            var engine = webView.getEngine();
            engine.load(url("static/template/map.html"));
            return webView;
        } else if (file.getName().equals("任务.json")) {
            var webView = new WebView();
            var engine = webView.getEngine();
            engine.load(url("static/template/task.html"));
            return webView;
        } else if (file.getName().equals("人物.json")) {
            var webView = new WebView();
            var engine = webView.getEngine();
            engine.load(url("static/template/task.html"));
            return webView;
        } else if (file.getName().contains(".md")) {
            var box = new SplitPane();
            var webView = new WebView();
            var engine = webView.getEngine();
            var code = new WTCoder(file);
            code.textProperty().addListener((observable, oldValue, newValue) -> {
                var s = MdParseService.mdParse(newValue);
                engine.loadContent(s);
            });
            box.getItems().addAll(code, webView);
            return box;
        } else if (file.getName().contains(".wt")) {

        } else if (file.getName().contains(".wtx")) {

        } else if (file.getName().contains(".wtm")) {

        }
        var code = new WTCoder(file);
        return code;
    }

    private String url(String path) {
        return App.class.getClassLoader().getResource(path).toExternalForm();
    }

}
