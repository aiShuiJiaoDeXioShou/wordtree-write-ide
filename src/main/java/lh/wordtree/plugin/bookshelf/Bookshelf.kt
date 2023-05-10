package lh.wordtree.plugin.bookshelf

import cn.hutool.core.io.FileUtil
import cn.hutool.core.thread.ThreadUtil
import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONWriter
import io.github.palexdev.materialfx.controls.MFXButton
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.stage.FileChooser
import lh.wordtree.App
import lh.wordtree.comm.config.Config
import lh.wordtree.comm.utils.ClassLoaderUtils
import lh.wordtree.comm.utils.WTFileUtils
import lh.wordtree.component.CpIcon
import lh.wordtree.plugin.WTPluginConfig
import lh.wordtree.plugin.WTPluginExtended
import lh.wordtree.plugin.WTPluginType
import java.io.File

data class BookshelfItem(var file: File)

class View() : StackPane() {
    private val bookshelfUrl = Config.APP_CONFIG_DIR + "/bookshelf"
    private val bsurl = File(bookshelfUrl)
    private val flowPane = FlowPane()
    private val root = BorderPane().apply {
        configInit()
        top = topView()
        center = centerView()
        style = "-fx-background-color: #ffff;"
    }

    init {
        this.children.add(root)
    }

    fun configInit() {
        if (!bsurl.exists()) {
            bsurl.mkdirs()
            return
        }
        val listFiles = bsurl.listFiles()
        if (listFiles != null) {
            ThreadUtil.execAsync {
                val books = listFiles.toList()
                    .stream()
                    .filter { it.name.contains(".json") }
                    .map { JSON.parseObject(it.readBytes(), BookshelfItem::class.java) }
                    .toList()
                books.forEach {
                    Platform.runLater { flowPane.children.add(BookView(it)) }
                }
            }
        }
    }

    fun topView() = HBox().apply {

        val iconSize = 25.0

        val sort = CpIcon(Image(ClassLoaderUtils.url("static/icon/排序.png")), "排序").apply {
            this.imageView().fitWidth = iconSize
            this.imageView().fitHeight = iconSize
            this.cursor = Cursor.HAND
        }

        val setting = CpIcon(Image(ClassLoaderUtils.url("static/icon/setting.png")), "设置").apply {
            this.imageView().fitWidth = iconSize
            this.imageView().fitHeight = iconSize
            this.cursor = Cursor.HAND
        }

        val flush = CpIcon(Image(ClassLoaderUtils.url("static/icon/刷新.png")), "刷新").apply {
            this.imageView().fitWidth = iconSize
            this.imageView().fitHeight = iconSize
            this.cursor = Cursor.HAND
        }

        val icon = Label("阅读器").apply {
            style = "-fx-font-weight: bold;-fx-font-size: 25;"
        }

        val search = HBox().apply {
            val field = TextField()
            field.prefWidth = 144.0
            field.prefHeight = 25.0
            val searchButton = CpIcon(Image(ClassLoaderUtils.url("static/icon/搜索.png")), "搜索")
            searchButton.imageView().fitHeight = 25.0
            searchButton.imageView().fitWidth = 25.0
            style = "-fx-background-color: #ededed;-fx-background-radius: 25;-fx-text-fill: #ffff;-fx-padding: 8 15;"
            children.addAll(field, searchButton)
            this.alignment = Pos.CENTER
        }

        val import = MFXButton("从本地导入").apply {
            onMouseClicked = EventHandler {
                val fileChooser = FileChooser()
                val file = fileChooser.showOpenDialog(App.primaryStage)
                val cf = WTFileUtils.fileName(file).plus(".config.json")
                var bookItem = BookshelfItem(file)
                var book = BookView(bookItem)
                if (bsurl.list()?.contains(cf) != true) {
                    val path = "$bookshelfUrl/$cf"
                    val f = FileUtil.touch(path)
                    FileUtil.writeBytes(
                        JSON.toJSONBytes(bookItem, JSONWriter.Feature.PrettyFormat), f
                    )
                }
                fileChooser.title = "请选择你要导入的小说"
                flowPane.children.add(book)
            }
            this.rippleRadius = 25.0
            style =
                "-fx-background-color: #4b4b4b;-fx-background-radius: 25;-fx-pref-height: 35;-fx-pref-width: 144;-fx-text-fill: #ffff;"
        }

        val resume = MFXButton("网络下载").apply {
            this.rippleRadius = 25.0
            style =
                "-fx-background-color: #ffff;-fx-background-radius: 25;-fx-pref-height: 35; -fx-pref-width: 144;-fx-border-color: #4b4b4b;-fx-border-radius: 25;-fx-border-width: 1;"
        }

        children.addAll(icon, search, sort, setting, flush, import, resume)
        spacing = 20.0
        padding = Insets(15.0)
    }

    fun centerView() = BorderPane().apply {
        flowPane.apply {
            padding = Insets(15.0)
            hgap = 20.0
            vgap = 20.0
        }
        center = flowPane
    }

}

class BookView(book: BookshelfItem) : VBox() {
    init {
        val label = Label("TXT").apply {
            style = """
                    -fx-background-color: #fbbf10;
                    -fx-text-fill: #ffff;
                    -fx-padding: 10;
                    -fx-pref-width: 90;
                """.trimIndent()
        }
        val title = Label(WTFileUtils.fileName(book.file))
        spacing = 10.0
        style = """
                -fx-background-color: #ffff;
                -fx-pref-width: 100;
                -fx-min-height: 140;
                -fx-padding: 10 0;
            """.trimIndent()
        val dropshadow = DropShadow() // 阴影向外
        dropshadow.radius = 10.0 // 颜色蔓延的距离
        dropshadow.offsetX = 0.0 // 水平方向，0则向左右两侧，正则向右，负则向左
        dropshadow.offsetY = 0.0 // 垂直方向，0则向上下两侧，正则向下，负则向上
        dropshadow.spread = 0.8 // 颜色变淡的程度
        dropshadow.color = Paint.valueOf("#f0f0f0") as Color? // 设置颜色
        effect = dropshadow
        children.addAll(label, title)
        this.onMouseClicked = EventHandler {
            val reader = WTReader(book.file)
            reader.show()
        }
    }
}

class Bookshelf() : WTPluginExtended {
    override fun view(): Node {
        return View()
    }

    override fun config(): WTPluginConfig {
        return object : WTPluginConfig {
            override fun name(): String {
                return "阅读器服务"
            }

            override fun version(): String {
                return "1.0.0"
            }

            override fun author(): String {
                return "林河"
            }

            override fun icon(): Image {
                return Image(ClassLoaderUtils.url("static/icon/书架管理.png"))
            }

            override fun introduce(): String {
                return """
                    书架管理
                """.trimIndent()
            }

            override fun type(): WTPluginType {
                return WTPluginType.menu
            }

        }
    }
}