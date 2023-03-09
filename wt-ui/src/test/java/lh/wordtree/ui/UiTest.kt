package lh.wordtree.ui

import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import javafx.stage.Stage
import javafx.util.Duration

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

        var c = c(280.0, 100.0, Role("萧炎", "炎帝", "炎盟", "", arrayListOf()))
        var c1 = c(200.0, 300.0, Role("萧熏儿", "帝母", "炎盟", "妻子", arrayListOf()))
        c.intersect(c1)
        var c2 = c(200.0, 400.0, Role("美杜莎", "小三", "炎盟", "妻子", arrayListOf()))
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

class UiTest3 : Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage!!.title = "Canvas Test"
        val borderPane = BorderPane()
        val wtNetwork = WTNetwork(
            listOf(
                Role(
                    "萧炎", "传说当中的顶级强者", "炎帝", "",
                    arrayListOf(
                        Role("萧薰儿", "传说当中的顶级强者", "炎帝", "爸爸", arrayListOf()),
                        Role("美杜莎", "传说当中的顶级强者", "炎帝", "父亲", arrayListOf()),
                        Role("萧鳞", "传说当中的顶级强者", "炎帝", "父亲", arrayListOf()),
                    )
                ),
                Role(
                    "萧炎1", "传说当中的顶级强者", "炎帝", "",
                    arrayListOf(
                        Role("萧薰儿1", "传说当中的顶级强者", "炎帝", "爸爸", arrayListOf()),
                        Role("美杜莎1", "传说当中的顶级强者", "炎帝", "父亲", arrayListOf()),
                        Role("萧鳞1", "传说当中的顶级强者", "炎帝", "父亲", arrayListOf()),
                    )
                ),
                Role(
                    "萧炎2", "传说当中的顶级强者", "炎帝", "",
                    arrayListOf(
                        Role("萧薰儿2", "传说当中的顶级强者", "炎帝", "爸爸", arrayListOf()),
                        Role("美杜莎2", "传说当中的顶级强者", "炎帝", "父亲", arrayListOf()),
                        Role("萧鳞2", "传说当中的顶级强者", "炎帝", "父亲", arrayListOf()),
                    )
                )
            )
        )
        val scene = Scene(wtNetwork.root)
        primaryStage.scene = scene
        primaryStage.show()
    }
}

class UiTest4 : Application() {
    val root: BorderPane = BorderPane()
    override fun start(stage: Stage?) {
        val scene = Scene(root)
        stage!!.title = "任务管理"
        val url = UiTest4().javaClass.classLoader.getResource("static/play/时钟秒表运行.mp3")?.toExternalForm()
        println(url)
        val media = Media(url)
        val mediaPlayer = MediaPlayer(media)
        root.center = VBox().apply {
            prefWidth = 800.0
            prefHeight = 500.0
            val button = Button("点击响铃声")
            children.add(button)
            mediaPlayer.stopTime = Duration.minutes(1.0)
            button.onMouseClicked = EventHandler {
                mediaPlayer.play()
            }
        }
        stage.scene = scene
        stage.show()
    }

}

fun main() {
    Application.launch(UiTest4::class.java)
}