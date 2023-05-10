package lh.wordtree.plugin.bookshelf

import com.alibaba.fastjson2.annotation.JSONField
import lh.wordtree.comm.config.Config
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
class BookParse(@JSONField() val file: File) {
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