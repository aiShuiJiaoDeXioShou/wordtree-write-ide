package lh.wordtree.handler.editor;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.utils.ClassLoaderUtils;
import lh.wordtree.comm.utils.WTFileUtils;
import lh.wordtree.component.editor.WTProgrammingEditor;
import lh.wordtree.component.editor.WTWriterEditor;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.service.language.MdParseService;
import lh.wordtree.service.language.OutlineServiceImpl;
import lh.wordtree.service.language.WTLanguageServiceImpl;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class EditorLanguageFactory {
    private final HashMap<String, EditorLanguageHandler<Node, File>> map = new HashMap<>();

    private EditorLanguageFactory() {
        this.registered(".txt", WTWriterEditor::new);
        this.registered("outer", WTWriterEditor::new);
        this.registered("地图.json", f -> {
            WebView webView = FactoryBeanService.getWebView();
            WebEngine engine = webView.getEngine();
            engine.load(ClassLoaderUtils.url("static/template/map/map.html"));
            return webView;
        });
        this.registered("任务.json", f -> {
            WebView webView = FactoryBeanService.getWebView();
            WebEngine engine = webView.getEngine();
            engine.load(ClassLoaderUtils.url("static/template/task/task.html"));
            return webView;
        });
        this.registered("人物.json", f -> new WTLanguageServiceImpl(f).view());
        this.registered("大记事.json", f -> new OutlineServiceImpl(f).view());
        this.registered(".md", f -> {
            WebView webView = FactoryBeanService.getWebView();
            WebEngine engine = webView.getEngine();
            var box = new SplitPane();
            var code = new WTWriterEditor(f);
            code.textProperty().addListener((observable, oldValue, newValue) -> {
                var s = MdParseService.mdParse(newValue);
                engine.loadContent(s);
            });
            box.getItems().addAll(code, webView);
            return box;
        });
        // 对编程语言的支持。
        var programmings = Arrays
                .stream(Objects.requireNonNull(new File(Config.LANGUAGE_CODE_PATH).listFiles()))
                .map(File::getName)
                .map(WTFileUtils::firstName).toList();
        programmings.forEach(name -> this.registered(name, f -> new WTProgrammingEditor()));
    }

    public static EditorLanguageFactory newInstance() {
        return EditorLanguageHandlerHolder.instance;
    }

    public Node handler(File f) {
        var name = WTFileUtils.lastExtension(f);
        var handler = map.get(name);
        if (handler == null) {
            handler = map.get(f.getName());
        }
        if (handler == null) {
            handler = map.get("outer");
        }
        return handler.view(f);
    }

    public void registered(String name, EditorLanguageFunHandler<Node, File> handler) {
        map.put(name, handler);
    }

    public void registered(EditorLanguageHandler<Node, File> handler) {
        map.put(handler.name(), handler);
    }

    private static class EditorLanguageHandlerHolder {
        public static EditorLanguageFactory instance = new EditorLanguageFactory();
    }
}
