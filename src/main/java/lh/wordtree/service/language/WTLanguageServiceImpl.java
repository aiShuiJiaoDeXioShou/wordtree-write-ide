package lh.wordtree.service.language;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lh.wordtree.App;
import lh.wordtree.component.editor.WTLangCodeArea;
import lh.wordtree.plugin.WTPlugLanguage;
import lh.wordtree.service.factory.FactoryBeanService;
import lh.wordtree.service.plugin.WTPluginService;
import lh.wordtree.views.notebook.core.TabMenuBarView;
import netscape.javascript.JSObject;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;

public class WTLanguageServiceImpl implements WTLanguageService {
    private File file;

    private HashMap<String, String> map = new HashMap<>();

    private final WTPlugLanguage language = WTPluginService.pluginService.getPlugLanguages().get(0);

    private String parseText;
    private JSONObject parseObject;
    private String sourceWt;

    public WTLanguageServiceImpl(File file) {
        this.file = file;
        this.parse();
    }

    public File file() {
        return file;
    }

    public void parse() {
        var figureJSON = FileUtil.readString(file(), StandardCharsets.UTF_8);
        parseObject = JSON.parseObject(figureJSON);
        sourceWt = parseObject.getString("wt");
        parseText = language.parse(sourceWt);
        var objects = JSON.parseArray(parseText, JSONObject.class);
        for (JSONObject object : objects) {
            map.put(object.getString("name"), object.toString());
        }
    }

    public Node view() {
        var splitPane = new SplitPane();
        WebView webView = FactoryBeanService.getWebView();
        WebEngine engine = webView.getEngine();
        WTLangCodeArea codeArea = new WTLangCodeArea();

        // 将java获取到的数据发送到该WebEngine
        JSObject win = (JSObject) engine.executeScript("window");
        win.setMember("figureJSON", parseText);//设置变量
        engine.load(url("static/template/figure/figure.html"));
        codeArea.appendText(sourceWt);

        // 添加键盘事件
        Nodes.addInputMap(codeArea, InputMap.consume(keyPressed(S, CONTROL_DOWN), event -> {
            parseObject.put("wt", codeArea.getText());
            FileUtil.writeUtf8String(parseObject.toString(JSONWriter.Feature.PrettyFormat), file);
            var tab = TabMenuBarView.newInstance().getSelectionModel().getSelectedItem();
            var graphic = (Text) tab.getGraphic();
            graphic.setText("");
        }));

        splitPane.getItems().addAll(codeArea, webView);
        return splitPane;
    }


    private String url(String path) {
        return Objects.requireNonNull(App.class.getClassLoader().getResource(path)).toExternalForm();
    }

}
