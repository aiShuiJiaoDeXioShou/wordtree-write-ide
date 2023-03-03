package lh.wordtree.plugin.wtlang

import java.io.File

data class Restrict(val start: String, val end: String)
data class Value(val name: String, val value: String)
data class Figure(val name: String, val description: String, var figures: ArrayList<Figure>)
interface WtParser {
    fun figuresAll(): ArrayList<Figure>
    fun figure(character: String): ArrayList<Figure>
    fun keyWords(): List<String>
    fun symbols(): List<String>
    fun varList(): List<Value>
    fun valList(): Map<String, Value>
    fun defList(): List<Value>
    fun restricts(): List<Restrict>
    fun parser(file: File)
    fun parser(source: String)
}

class SimpleParser : WtParser {

    constructor(file: File) {
        parser(file)
    }

    constructor() {}

    private val keywords = arrayListOf(
        "val", "var", "def"
    )
    private val symbols = arrayListOf(
        "=>", "="
    )
    private val restricts = arrayListOf(Restrict("{", "}"))
    private var varList = arrayListOf<Value>()
    private var defList = arrayListOf<Value>()
    private var valList = hashMapOf<String, Value>()

    override fun parser(file: File) {
        parser(file.readLines())
    }

    override fun parser(source: String) {
        parser(source.split("\n"))
    }

    private fun zero() {
        varList = arrayListOf()
        defList = arrayListOf()
        valList = hashMapOf()
    }

    private fun parser(source: List<String>) {
        if (defList.size > 0) {
            zero()
        }
        var nowValue = ""
        var valueBool = false
        var valueName = ""
        // 简单的语法分析与语法采集
        for (it in source) {
            // 碰到注释跳过解析
            if (it.isBlank() || it.substring(0, 1) == "#") continue
            if (valueBool) {
                nowValue += it.trim()
                if (it.trim() == "}") {
                    valList[valueName] = Value(valueName, nowValue)
                    valueBool = false
                    nowValue = ""
                    valueName = ""
                }
                continue
            }
            val tokens = it.trim().split(" ")
            if (tokens.size < 4) continue
            val keyword = tokens[0]
            val name = tokens[1]
            val symbol = tokens[2]
            var value = tokens[3]
            if (value == "{") {
                nowValue += value
                valueName += name
                valueBool = true
                continue
            }
            if (!keywords.contains(keyword)) throw InstantiationException("没有该关键字！")
            if (!symbols.contains(symbol)) throw InstantiationException("没有该运算符号！")
            if (keyword == "var" && symbol == "=") {
                varList += Value(name, value)
            }
            if (keyword == "val" && symbol == "=") {
                valList[name] = Value(name, value)
            }
            if (keyword == "def" && symbol == "=>") {
                value += "," + tokens[4] + "," + tokens[5]
                defList.add(Value(name, value))
            } else throw InstantiationException("等式不合法！")
        }

    }

    /**
     * 分析def关系列表,根据def的关系生成对应的人物关系树
     */
    override fun figuresAll(): ArrayList<Figure> {
        val figures = arrayListOf<Figure>()
        // 去重处理
        val distinct = defList.stream().map { it.name }.toList().distinct().toMutableList()
        val valDis = defList.stream().map { it.value.split(",")[0] }.toList().distinct()
        distinct += valDis
        for (def in distinct) {
            val f = Figure(def, "", arrayListOf())
            figures += f
            collectionOr(f)
        }
        return figures
    }

    override fun figure(character: String): ArrayList<Figure> {
        val figures = arrayListOf<Figure>()
        val f = Figure(character, "", arrayListOf())
        figures += f
        collectionOr(f)
        return figures
    }

    private fun clears(figures: ArrayList<Figure>) {
        figures.forEach { i ->
            i.figures.forEach {
                if (it.figures.size > 0) {
                    var index = 0
                    it.figures.forEachIndexed { ii, f ->
                        if (f.name == i.name) index = ii
                    }
                    it.figures.removeAt(index)
                }
            }
        }
    }

    private fun collection(def: Figure) {
        for (d in defList) {
            val v = d.value.split(",")
            val cName = v[0]
            val cDes = v[2]
            val contraryDes = v[1]
            if (def.name == d.name) {
                val f = Figure(cName, cDes, arrayListOf())
                def.figures += f
                collection(f)
            } else if (def.name == cName) {
                val f = Figure(d.name, contraryDes, arrayListOf())
                def.figures += f
            }
        }
    }

    private fun collectionOr(def: Figure) {
        for (d in defList) {
            val v = d.value.split(",")
            val cName = v[0]
            val cDes = v[2]
            val contraryDes = v[1]
            if (def.name == d.name) {
                val f = Figure(cName, cDes, arrayListOf())
                def.figures += f
            } else if (def.name == cName) {
                val f = Figure(d.name, contraryDes, arrayListOf())
                def.figures += f
            }
        }
    }

    private fun collectionAll(def: Figure) {
        for (d in defList) {
            val v = d.value.split(",")
            val cName = v[0]
            val cDes = v[2]
            val contraryDes = v[1]
            if (def.name == d.name) {
                val f = Figure(cName, cDes, arrayListOf())
                def.figures += f
                collectionAll(f)
            } else if (def.name == cName) {
                val f = Figure(d.name, contraryDes, arrayListOf())
                def.figures += f
                collection2(f)
            }
        }
    }

    private fun collection2(def: Figure) {
        for (d in defList) {
            val v = d.value.split(",")
            val cName = v[0]
            val cDes = v[2]
            if (def.name == d.name) {
                val f = Figure(cName, cDes, arrayListOf())
                def.figures += f
                collection2(f)
            }
        }
    }

    override fun keyWords(): List<String> {
        return keywords
    }

    override fun symbols(): List<String> {
        return symbols
    }

    override fun varList(): List<Value> {
        return varList
    }

    override fun valList(): Map<String, Value> {
        return valList
    }

    override fun defList(): List<Value> {
        return defList
    }

    override fun restricts(): List<Restrict> {
        return restricts
    }
}