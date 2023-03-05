package lh.wordtree.ui

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Insets
import javafx.scene.Cursor
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.ArcType
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.stage.Popup
import java.awt.MouseInfo
import kotlin.math.pow

class WTNetwork() {

}

data class Role(val name: String, val der: String, val power: String, val relationship: String)
enum class RoleViewType {
    ROUND, TRIANGLE, SQUARE
}

data class RoleViewSkin(
    // 填充的颜色
    var color: Paint = Color.SKYBLUE,
    var borderColor: Paint = Color.BLUE,
    var textColor: Paint = Color.WHITE,
    var lineTextColor: Paint = Color.BLUE,
    var endColor: Paint = Color.BLACK,
)

abstract class BaseContentPopup(role: Role) : Popup()
class RoleView(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val context2D: GraphicsContext,
    val role: Role,
    val roleViewType: RoleViewType = RoleViewType.ROUND,
    var roleViewSkin: SimpleObjectProperty<RoleViewSkin> = SimpleObjectProperty(RoleViewSkin()),
    val popup: BaseContentPopup = ContentPopup(role = role)
) {

    /**
     * 球的大小
     */
    private val size get() = 70.0

    /**
     * 绘制圆形
     */
    private val arcExtent = 360.0
    private val startAngle = 0.0

    /**
     * 字体大小
     */
    private val fontSize get() = 16.0

    /**
     * 判断时候在该目标范围
     */
    var isRangeProperty = SimpleBooleanProperty(false)

    init {
        draw()
        controller()
    }

    /**
     * 绘制指定图案
     */
    fun draw() {
        when (roleViewType) {
            RoleViewType.ROUND -> drawROUND()
            RoleViewType.TRIANGLE -> TODO()
            RoleViewType.SQUARE -> TODO()
        }
    }

    /**
     * 绘制圆形图案
     */
    fun drawROUND() {
        context2D.apply {
            fill = roleViewSkin.value.color
            fillArc(x, y, size, size, startAngle, arcExtent, ArcType.OPEN)
            fill = roleViewSkin.value.borderColor
            strokeArc(x, y, size, size, startAngle, arcExtent, ArcType.OPEN)
            fill = roleViewSkin.value.textColor
            font = Font.font(fontSize)
            textAlign = TextAlignment.CENTER
            fillText(role.name, x + size.div(2), y + size.div(2) + fontSize.div(2) - 1)
        }
    }

    /**
     * 绑定事件
     */
    private fun controller() {
        // 绘制的时候绑定范围
        context2D.canvas.addEventHandler(MouseEvent.MOUSE_MOVED) {
            isRangeProperty.value = (it.x > x && it.y > y && it.x < (x + size) && it.y < (y + size))
        }
        // 设置默认Hover事件
        isRangeProperty.addListener { _, _, bool ->
            val p = MouseInfo.getPointerInfo().location
            val localX = p.x.toDouble()
            val localY = p.y - size
            if (bool) {
                bookDer(localX, localY)
                context2D.canvas.cursor = Cursor.HAND
            } else {
                bookDerClear()
                context2D.canvas.cursor = Cursor.DEFAULT
            }
        }
    }

    /**
     * 清除方法,自我销毁
     */
    fun destroyed() {
        context2D.clearRect(x, y, size, size)
    }

    /**
     * 绘制说明文字框位置
     */
    private fun bookDer(x: Double, y: Double) {
        popup.show(context2D.canvas, x, y)
    }

    /**
     * 关闭说明文本框
     */
    private fun bookDerClear() {
        popup.hide()
    }

    /**
     * 绘制两个图案相交的线，一个光滑的曲线
     */
    fun intersect(targetRole: RoleView) {
        // 起点
        val (top1, right1, bottom1, left1) = computingCenter(this)
        // 目标
        val (top2, right2, bottom2, left2) = computingCenter(targetRole)
        val (cx, cy) = line(bottom1, top2)
        // 添加人物关系说明
        context2D.fill = roleViewSkin.value.lineTextColor
        context2D.fillText(targetRole.role.relationship, cx, cy)
        context2D.textAlign = TextAlignment.CENTER
    }

    /**
     * 计算中心节点
     */
    private fun computingCenter(rv: RoleView): Array<Pair<Double, Double>> {
        // 计算中心点,四周边框的中心点
        val centerX = rv.x + rv.size / 2
        val centerY = rv.y + rv.size / 2
        // 上边框的中心点
        val topCenter = Pair(centerX, rv.y)
        val bottomCenter = Pair(centerX, rv.y + rv.size)
        val leftCenter = Pair(rv.x, centerY)
        val rightCenter = Pair(rv.x + rv.size, centerY)

        return arrayOf(topCenter, rightCenter, bottomCenter, leftCenter)
    }

    /**
     * 绘制前往曲线
     */
    private fun line(start: Pair<Double, Double>, end: Pair<Double, Double>): Pair<Double, Double> {
        // 求曲线控制点
        val (startX, startY) = start
        val (endX, endY) = end
        // 求中心
        // 上三角形底边长为
        val sw = (endX - startX) / 2
        val sh = (endY - startY) / 2
        // 得出中心点的坐标为
        val (cx, cy) = Pair(startX + sw, startY + sh)
        // 算出两个控制点的坐标
        // 设小三角形的高为10
        // 小三角形底边长度z1和高度h
        val h = 10
        val z1 = (sw.pow(2) + sh.pow(2)).pow(1 / 2)
        val p = 30
        context2D.moveTo(startX, startY)
        context2D.bezierCurveTo(cx + p, cy + p, cx - p, cy - p, endX, endY)
        context2D.stroke()

        // 画出小三角形
        val w = 5.0
        context2D.apply {
            beginPath()
            moveTo(endX - w, endY - w * 2)
            lineTo(endX + w, endY - w * 2)
            lineTo(endX, endY)
            closePath()
            stroke()
            fill = roleViewSkin.value.endColor
            fill()
        }

        return Pair(cx, cy)
    }

    /**
     * 绘制起点中心
     */
    private fun drawOrigin(x: Double, y: Double) {
        val size = 10.0
        context2D.fill = Color.BURLYWOOD
        context2D.fillArc(x - size / 2, y - size / 2, size, size, 0.0, 360.0, ArcType.OPEN)
    }

    private fun drawOrigin(size: Pair<Double, Double>) {
        drawOrigin(size.first, size.second)
    }

    private class ContentPopup(
        val bookW: Double = 150.0,
        val bookH: Double = 80.0,
        val role: Role
    ) : BaseContentPopup(role) {
        init {
            width = bookW
            height = bookH
            val box = VBox().apply {
                style = """
                    -fx-border-width: 1;
                    -fx-border-color: #f0f0f0;
                    -fx-border-radius: 5;
                    -fx-background-radius: 5;
                    -fx-background-color: #ffff;
                """.trimIndent()
                prefWidth = 200.0
                prefHeight = 90.0
            }
            val power = Label(role.power).apply {
                textFill = Paint.valueOf("#4077f9")
                prefWidthProperty().bind(box.widthProperty())
                padding = Insets(5.0, 0.0, 0.0, 10.0)
            }
            val der = Label(role.name).apply {
                textFill = Paint.valueOf("#ffe066")
                padding = Insets(5.0, 0.0, 5.0, 10.0)
                style = """
                    -fx-border-width: 0 0 1 0;
                    -fx-border-color: #cccc;
                """.trimIndent()
                prefWidthProperty().bind(box.widthProperty())
            }
            val name = Label("人物详情：${role.der}").apply {
                prefWidthProperty().bind(box.widthProperty())
                padding = Insets(5.0, 0.0, 0.0, 10.0)
            }
            box.children.addAll(power, der, name)
            content.add(box)
        }
    }
}