package lh.wordtree.plugin.wtlang

import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import lh.wordtree.comm.entity.Figure
import lh.wordtree.plugin.WTPlugLanguage
import lh.wordtree.plugin.WTPluginConfig
import lh.wordtree.plugin.WTPluginType

class WTLangPlugin : WTPlugLanguage {
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
                val stream = javaClass.classLoader.getResourceAsStream("static/icon/33资源.png")
                return Image(stream)
            }

            override fun introduce(): String {
                return "用于解析WT这种特定的文件树语言。"
            }

            override fun type(): WTPluginType {
                return WTPluginType.language
            }
        }
    }


    override fun view(): Node {
        var pane = BorderPane()
        return pane
    }

}