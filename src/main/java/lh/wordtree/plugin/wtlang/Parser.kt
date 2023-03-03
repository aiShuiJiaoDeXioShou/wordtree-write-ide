package lh.wordtree.plugin.wtlang

import java.io.File

enum class TokenType {
    // 运算符
    operator,

    // 常量
    value,

    // 关键字
    keyword,

    // 标识符
    identifier,

    // 界限符
    boundary,
}

/**
 * 每一个字词对应着每一个Token值
 */
class Token {
    val type: TokenType = TokenType.value
}

interface Parser {
    fun keyWord(): List<String>
    fun operator(): List<String>
    fun parser(wt: File)
    fun grammar(wt: File)
}

class ParserImpl : Parser {

    override fun keyWord(): List<String> {
        return arrayListOf()
    }

    override fun operator(): List<String> {
        return arrayListOf()
    }

    override fun parser(wt: File) {
        val readLines = wt.readLines()
        readLines.forEach {

        }
    }

    /**
     * 分析每一个句子的语法是否正确
     */
    override fun grammar(wt: File) {
        // 在默认的约定中
    }

}