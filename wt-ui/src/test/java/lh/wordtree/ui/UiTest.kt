package lh.wordtree.ui

import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import javafx.stage.Stage

class UiTest : Application() {
    val bi = Button("画笔")
    val clear = Button("清空")
    val xian = Button("连线")
    val tools = FlowPane().apply {
        orientation = Orientation.HORIZONTAL
        children.addAll(bi, clear, xian)
        hgap = 10.0
    }
    val can = Canvas()
    val context2D = can.graphicsContext2D
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Canvas Test"
        val borderPane = BorderPane()

        // fill 填充形状
        can.width = 1080.0
        can.height = 500.0
        context2D.fill = Color.RED
        context2D.fillRect(100.0, 100.0, 200.0, 50.0)


        context2D.fill = Color.AQUA
        context2D.fillText("事件", 100.0, 25.0)

        context2D.fillArc(150.0, 150.0, 400.0, 400.0, 0.0, 400.0, ArcType.OPEN)
        context2D.fill = Color.BEIGE

        // 这个是使用画笔去绘制图案
        context2D.stroke = Color.BISQUE
        context2D.strokeRect(700.0, 100.0, 200.0, 500.0)

        borderPane.center = can
        borderPane.top = tools
        val scene = Scene(borderPane)
        primaryStage.scene = scene
        primaryStage.show()
        this.controller()
    }

    fun controller() {
        clear.onMouseClicked = EventHandler {
            println("查出了")
            context2D.clearRect(0.0, 0.0, 500.0, 500.0)
        }
        can.onMouseMoved = EventHandler {
            val x = it.x
            val y = it.y
            println("x = ${x}, y = ${y}")
        }
    }
}

/**
 * 绘制流程图
 */
class UiTest2 : Application() {
    val canvas = Canvas()
    val context2D = canvas.graphicsContext2D
    override fun start(primaryStage: Stage?) {
        primaryStage!!.title = "Canvas Test"
        val borderPane = BorderPane()
        canvas.width = 1080.0
        canvas.height = 500.0

        var c = c(280.0, 100.0, Role("萧炎", "炎帝", "炎盟", ""))
        var c1 = c(200.0, 300.0, Role("萧熏儿", "帝母", "炎盟", "妻子"))
        c.intersect(c1)
        var c2 = c(200.0, 400.0, Role("美杜莎", "小三", "炎盟", "妻子"))
        c.intersect(c2)

        borderPane.center = canvas
        val scene = Scene(borderPane)
        primaryStage.scene = scene
        primaryStage.show()
        this.controller()
    }

    /**
     * 生成人名或者是图像
     */
    private fun c(x: Double, y: Double, role: Role): RoleView {
        return RoleView(x = x, y = y, context2D = context2D, role = role)
    }

    private fun controller() {

    }
}

fun main() {
    Application.launch(UiTest2::class.java)
}