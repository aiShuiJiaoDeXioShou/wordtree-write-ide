package lh.wordtree.plugin.bookshelf

import io.github.palexdev.materialfx.controls.MFXButton
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.stage.FileChooser
import javafx.stage.Stage
import lh.wordtree.App
import lh.wordtree.comm.config.Config
import lh.wordtree.comm.utils.ClassLoaderUtils
import lh.wordtree.component.CpIcon
import lh.wordtree.plugin.WTPluginConfig
import lh.wordtree.plugin.WTPluginExtended
import lh.wordtree.plugin.WTPluginType
import org.fxmisc.richtext.CodeArea
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import java.io.File

/**
 * 连接数据库，初始化数据库对象
 */
val database = Database.connect(Config.SQLITE_JDBC_CONFIG_PATH)
val Database.books get() = this.sequenceOf(Books)

interface Chapter : Entity<Chapter> {
    companion object : Entity.Factory<Chapter>()

    val id: Int
    var bookId: Book
    var title: String
    var bookTitle: String
    var context: String
}

interface Book : Entity<Book> {
    companion object : Entity.Factory<Book>()

    val id: Int
    var title: String
    var author: String
    var url: String
    var imageUrl: String
    var chapters: ArrayList<Chapter>
}

// 定义 Book 和 Chapter 表
object Books : Table<Book>("book") {
    val id = int("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    val author = varchar("author").bindTo { it.author }
    val url = varchar("url").bindTo { it.url }
    val imageUrl = varchar("image_url").bindTo { it.imageUrl }
}

object Chapters : Table<Chapter>("chapter") {
    val id = int("id").primaryKey().bindTo { it.id }
    val bookId = int("book_id").references(Books) { it.bookId }
    val title = varchar("title").bindTo { it.title }
    val content = varchar("content").bindTo { it.context }
}

/**
 * 书籍解析器，将一本书籍分为不同的章节和书名，最后导入，统计字数
 */
class BookParse(val file: File) {
    // 根据正则表达式解析一本text文档里面所有的章节名，然后放入定义好的Book里面去
    init {
        parse()
    }

    fun parse() {
        // 读取文本文件
        database.useTransaction {
            val bookContent = file.readText()

            val chapterRegex = Regex(
                "^(第[0-9]+章：|第[0-9]+节：|第[0-9]+篇：|一、|二、|三、|四、|五、|六、|七、|八、|九、|十、)[^\\r\\n]+",
                RegexOption.MULTILINE
            )
            val chapters = chapterRegex.findAll(bookContent).map { matchResult ->
                val title = matchResult.value.trim()
                val startIndex = matchResult.range.last + 1
                val endIndex = if (matchResult.next() != null) matchResult.next()!!.range.first else bookContent.length
                val context = bookContent.substring(startIndex, endIndex).trim()
                Chapter {
                    this.title = title
                    this.context = context
                }
            }.toList()

            val book = Book {
                this.title = file.name
                this.author = "无名氏"
                this.url = file.path
                this.imageUrl = "https://example.com/book/image.png"
                this.chapters.addAll(chapters)
            }
            database.books.add(book)
        }
    }

    fun parseChapters(bookContent: String): List<Chapter> {
        // 按照章节名称解析出每一章节的内容
        val regex = "(第\\d+章|\\d+\\s*、).*"
        return regex.toRegex().findAll(bookContent)
            .map { matchResult ->
                val title = matchResult.value.trim()
                val context = matchResult.next()?.value?.trim() ?: ""
                Chapter {
                    this.title = title
                    this.context = context
                }
            }.toList()
    }
}

/**
 * 网络爬虫
 */
class BookReptile() {

}

class ReaderCoder() : CodeArea() {
    init {
        prefWidth = 500.0
        prefHeight = 700.0
        padding = Insets(5.0, 5.0, 5.0, 5.0)
        this.isWrapText = true
        isEditable = false
        styleClass.add("writer-editor")
    }
}

class Reader() : Stage() {
    private val reader = ReaderCoder()

    init {
        scene = Scene(reader)
    }
}

class Bookshelf() : WTPluginExtended {
    override fun view(): Node {
        return BorderPane().apply {
            top = Top()
            center = Content()
            style = """
                -fx-background-color: #ffff;
            """.trimIndent()
        }
    }

    class Content() : BorderPane() {
        val oprings = VBox(BorderPane().apply {
            left = Label("选择")
            center = Label("我的书架")
            right = CpIcon(Image(ClassLoaderUtils.url("static/icon/排序.png")), "排序")
        }).apply {
            padding = Insets(30.0, 40.0, 30.0, 40.0)
        }

        init {
            top = oprings
            val flowPane = FlowPane().apply {
                children.addAll(Book("斗破苍穹"))
                padding = Insets(15.0)
                hgap = 20.0
                vgap = 20.0
            }
            center = flowPane
        }
    }

    class Book(val title: String) : VBox() {
        init {
            val label = Label("TXT").apply {
                style = """
                    -fx-background-color: #fbbf10;
                    -fx-text-fill: #ffff;
                    -fx-padding: 10;
                    -fx-pref-width: 90;
                """.trimIndent()
            }
            val title = Label(title)
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
        }
    }

    class Top() : HBox() {
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
            style = """
                 -fx-font-weight: bold;
                 -fx-font-size: 25;
            """.trimIndent()
        }
        val search = HBox().apply {
            val field = TextField()
            field.prefWidth = 144.0
            field.prefHeight = 25.0
            val serchButton = CpIcon(Image(ClassLoaderUtils.url("static/icon/搜索.png")), "搜索")
            serchButton.imageView().fitHeight = 25.0
            serchButton.imageView().fitWidth = 25.0
            style = """
                -fx-background-color: #ededed;
                -fx-background-radius: 25;
                -fx-text-fill: #ffff;
                -fx-padding: 8 15;
            """.trimIndent()
            children.addAll(field, serchButton)
            this.alignment = Pos.CENTER
        }
        val import = MFXButton("从本地导入").apply {
            onMouseClicked = EventHandler {
                val fileChooser = FileChooser()
                fileChooser.title = "请选择你要导入的小说"
                val file = fileChooser.showOpenDialog(App.primaryStage)
            }
            this.rippleRadius = 25.0
            style = """
                -fx-background-color: #4b4b4b;
                -fx-background-radius: 25;
                -fx-pref-height: 35;
                -fx-pref-width: 144;
                -fx-text-fill: #ffff;
            """.trimIndent()
        }
        val resume = MFXButton("网络下载").apply {
            this.rippleRadius = 25.0
            style = """
                -fx-background-color: #ffff;
                -fx-background-radius: 25;
                -fx-pref-height: 35;
                -fx-pref-width: 144;
                -fx-border-color: #4b4b4b;
                -fx-border-radius: 25;
                -fx-border-width: 1;
            """.trimIndent()
        }

        init {
            children.addAll(icon, search, sort, setting, flush, import, resume)
            spacing = 20.0
            padding = Insets(15.0)
        }
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