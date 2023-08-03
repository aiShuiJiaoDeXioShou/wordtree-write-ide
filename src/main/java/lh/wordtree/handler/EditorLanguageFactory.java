package lh.wordtree.handler;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.utils.ClassLoaderUtils;
import lh.wordtree.comm.utils.WTFileUtils;
import lh.wordtree.editor.ProgrammingEditor;
import lh.wordtree.editor.WriterEditor;
import lh.wordtree.plugin.wtlang.WtLangView;
import lh.wordtree.comm.BeanFactory;
import lh.wordtree.service.language.MdParseService;
import lh.wordtree.service.language.OutlineServiceImpl;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class EditorLanguageFactory {
    private final HashMap<String, EditorLanguageHandler<Node, File>> map = new HashMap<>();
    private EditorLanguageFactory() {
        this.registered(".txt", WriterEditor::new);
        this.registered("outer", WriterEditor::new);
        this.registered("地图.json", f -> {
            WebView webView = BeanFactory.getWebView();
            WebEngine engine = webView.getEngine();
            engine.load("https://azgaar.github.io/Fantasy-Map-Generator/");
            return webView;
        });
        this.registered("任务.json", f -> {
            WebView webView = BeanFactory.getWebView();
            WebEngine engine = webView.getEngine();
            engine.load(ClassLoaderUtils.url("static/template/task/task.html"));
            return webView;
        });
        this.registered("人物.json", f -> new WtLangView(f).view());
        this.registered("大记事.json", f -> new OutlineServiceImpl(f).view());
        this.registered(".md", f -> {
            WebView webView = BeanFactory.getWebView();
            WebEngine engine = webView.getEngine();
            var box = new SplitPane();
            var code = new WriterEditor(f);
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
        programmings.forEach(name -> this.registered(name, f -> new ProgrammingEditor()));
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
