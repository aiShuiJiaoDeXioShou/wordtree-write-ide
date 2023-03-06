package lh.wordtree.service.language;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.text.Text;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.entity.Figure;
import lh.wordtree.component.editor.WTLangCodeArea;
import lh.wordtree.plugin.WTPlugLanguage;
import lh.wordtree.service.plugin.WTPluginService;
import lh.wordtree.ui.WTNetwork;
import lh.wordtree.views.core.TabMenuBarView;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;

public class WTLanguageServicePlusImpl implements WTLanguageService {
    private final WTPlugLanguage language = WTPluginService.pluginService.getPlugLanguages().get(0);
    private File file;
    private List<Figure> parseData;
    private JSONObject parseObject;
    private String sourceWt;
    private SplitPane splitPane = new SplitPane();
    private WTLangCodeArea codeArea = new WTLangCodeArea();

    public WTLanguageServicePlusImpl(File file) {
        this.file = file;
        this.parse();
    }

    public File file() {
        return file;
    }

    public void parse() {
        var figureJSON = FileUtil.readString(file(), StandardCharsets.UTF_8);
        parseObject = JSON.parseObject(figureJSON);
        if (Objects.isNull(parseObject)) {
            FileUtil.writeUtf8String(Config.WtSourString, file);
            figureJSON = FileUtil.readString(file(), StandardCharsets.UTF_8);
            parseObject = JSON.parseObject(figureJSON);
        }
        sourceWt = parseObject.getString("wt");
        // 如果是一个空文件就创造相关的关系
        parseData = (List<Figure>) language.parse(sourceWt);
    }

    public Node view() {
        this.layout();
        this.controller();
        return splitPane;
    }

    private void controller() {
        // 添加键盘事件
        Nodes.addInputMap(codeArea, InputMap.consume(keyPressed(S, CONTROL_DOWN), event -> {
            this.save();
            // 重新刷新文件，对文件进行重新解析
            Platform.runLater(this::flush);
        }));
    }

    private void layout() {
        var wtNetwork = new WTNetwork(parseData);
        codeArea.appendText(sourceWt);
        splitPane.getItems().addAll(codeArea, wtNetwork.getRoot());
    }

    private void flush() {
        // 解析文本数据
        this.parse();
        splitPane.getItems().remove(1);
        var wtNetwork = new WTNetwork(parseData);
        splitPane.getItems().add(wtNetwork.getRoot());
    }

    /**
     * 保存数据
     */
    private void save() {
        parseObject.put("wt", codeArea.getText());
        FileUtil.writeUtf8String(parseObject.toString(JSONWriter.Feature.PrettyFormat), file);
        var tab = TabMenuBarView.newInstance().getSelectionModel().getSelectedItem();
        var graphic = (Text) tab.getGraphic();
        graphic.setText("");
    }

}
