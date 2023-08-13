package lh.wordtree.plugin.wtlang

import cn.hutool.core.io.FileUtil
import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter
import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.SplitPane
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.text.Text
import lh.wordtree.comm.config.Config
import lh.wordtree.comm.entity.Figure
import lh.wordtree.editor.LangEditor
import lh.wordtree.plugin.WTPlugLanguage
import lh.wordtree.plugin.WTPluginConfig
import lh.wordtree.plugin.WTPluginType
import lh.wordtree.comm.BeanFactory
import lh.wordtree.service.plugin.WTPluginService
import lh.wordtree.ui.controls.WTNetwork
import lh.wordtree.model.core.TabMenuBarView
import org.fxmisc.wellbehaved.event.EventPattern
import org.fxmisc.wellbehaved.event.InputMap
import org.fxmisc.wellbehaved.event.Nodes
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

class WTLangPlugin() : WTPlugLanguage {
    private val parser: WtParser = SimpleParser()
    override fun init() {

    }

    override fun parse(source: String?): List<Figure> {
        if (source != null) parser.parser(source)
        return parser.figuresAll()
    }

    override fun apply() {

    }

    override fun end() {

    }

    override fun config(): WTPluginConfig {
        return object : WTPluginConfig {
            override fun name(): String = "WT语法解析器"

            override fun version(): String = "1.0.1"

            override fun author(): String = "林河"

            override fun icon(): Image {
                val stream = javaClass.classLoader.getResourceAsStream("static/icon/icon.png")
                return Image(stream)
            }

            override fun cover(): Image {
                val stream = javaClass.classLoader.getResourceAsStream("static/icon/icon.png")
                return Image(stream)
            }

            override fun introduce(): String {
                return "用于解析WT这种特定的文件树语言。"
            }

            override fun type(): WTPluginType {
                return WTPluginType.language
            }

            override fun file(): String {
                return "人物.json"
            }
        }
    }

    override fun view(): Node {
        return Button()
    }
}

class WtLangView(private val file: File) {
    private val plugin: WTPlugLanguage? = WTPluginService.pluginService.plugLanguages["人物.json"]
    private var parseData: List<Figure>? = null
    private var parseObject: JSONObject? = null
    private var sourceWt: String? = null
    private val splitPane = SplitPane()
    private val codeArea = LangEditor()
    private var wtNetwork: WTNetwork? = null

    init {
        myPaser()
    }

    fun file(): File {
        return file
    }

    fun myPaser() {
        var figureJSON = FileUtil.readString(file(), StandardCharsets.UTF_8)
        parseObject = JSON.parseObject(figureJSON)
        if (Objects.isNull(parseObject)) {
            FileUtil.writeUtf8String(Config.WtSourString, file())
            figureJSON = FileUtil.readString(file(), StandardCharsets.UTF_8)
            parseObject = JSON.parseObject(figureJSON)
        }
        sourceWt = parseObject!!.getString("wt")
        // 如果是一个空文件就创造相关的关系
        parseData = plugin!!.parse(sourceWt) as List<Figure>?
        BeanFactory.roles.set(parseData)
    }

    fun view(): Node {
        layout()
        controller()
        return splitPane
    }

    private fun controller() {
        // 添加键盘事件
        Nodes.addInputMap(
            codeArea,
            InputMap.consume(EventPattern.keyPressed(KeyCode.S, KeyCombination.CONTROL_DOWN)) { event: KeyEvent? ->
                save()
                // 重新刷新文件，对文件进行重新解析
                Platform.runLater { flush() }
            })
    }

    private fun layout() {
        wtNetwork = WTNetwork(parseData!!)
        codeArea.appendText(sourceWt)
        splitPane.items.addAll(codeArea, wtNetwork!!.root)
    }

    private fun flush() {
        // 解析文本数据
        myPaser()
        wtNetwork!!.flush(parseData)
    }

    /**
     * 保存数据
     */
    private fun save() {
        parseObject!!["wt"] = codeArea.text
        FileUtil.writeUtf8String(parseObject!!.toString(JSONWriter.Feature.PrettyFormat), file())
        val tab = TabMenuBarView.newInstance().selectionModel.selectedItem
        val graphic = tab.graphic as Text
        graphic.text = ""
    }
}
