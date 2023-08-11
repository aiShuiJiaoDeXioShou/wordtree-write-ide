package lh.wordtree.ui

import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.util.Duration
import lh.wordtree.ui.controls.*
import lh.wordtree.ui.utils.ClassLoaderUtils


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

class UiTest5 : Application() {
    val root = BorderPane()
    val scene = Scene(root, 500.0, 600.0)
    override fun start(primaryStage: Stage?) {
        primaryStage!!.title = "UI TEST"
        val box = HBox()
        val button = WTButton("按钮")
        box.children.add(button)
        root.center = box
        primaryStage.scene = scene
        primaryStage.show()
    }

}

class UiTest6 : Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage!!.title = "UI测试"
        val root = BorderPane()
        val box = VBox()
        root.center = box
        val scene = Scene(root)
        primaryStage.scene = scene
        primaryStage.show()
    }
}

class UiTest7: Application() {
    override fun start(stage: Stage?) {
        val canvas = Canvas()
        val pane = BorderPane()
        val root = Pane()
        root.children.add(pane)
        root.prefHeightProperty().bind(pane.heightProperty())
        root.prefWidthProperty().bind(pane.widthProperty())
        pane.center = canvas
        stage!!.scene = Scene(root)

        // 画笔的选项卡
        val colorPicker = ColorPicker(Color.BLACK)
        // 粗细
        val slider = Slider(1.0,100.0,1.0)

        // 选择框
        val tg = ToggleGroup()
        val pen = ToggleButton("画笔")
        val xp = ToggleButton("橡皮")
        val circle = ToggleButton("圆形")
        val rectangle = ToggleButton("方块")
        val txt = ToggleButton("文字")
        pen.toggleGroup = tg
        pen.isSelected = true
        xp.toggleGroup = tg
        txt.toggleGroup = tg
        circle.toggleGroup = tg
        rectangle.toggleGroup = tg

        // 图章工具
        val imageBox = HBox()
        fun sendButton(txt: String, icon: String) = ToggleButton(txt).apply {
            this.graphic = ImageView(Image(ClassLoaderUtils.url(icon))).apply {
                fitWidth = 35.0
                fitHeight = 35.0
            }
        }

        fun sendImage(icon: String) = ImageView(Image(ClassLoaderUtils.url(icon))).apply {
            fitWidth = 20.0
            fitHeight = 20.0
        }

        val cbTb = sendButton("\n城堡", "static/draw/城堡.png")
        val cbSp = sendImage("static/draw/城堡.png")
        cbTb.toggleGroup = tg
        imageBox.children.addAll(cbTb)

        // 橡皮工具
        val xpCircle = Circle()
        // 文字工具框
        val txtField = TextField()

        val hbox = HBox()
        hbox.children.addAll(pen, xp, txt, circle, rectangle)

        val box = VBox()
        box.children.addAll(colorPicker, slider, hbox, imageBox)
        box.spacing = 10.0
        pane.left = box

        // 画板功能的实现
        canvas.width = 1080.0
        canvas.height = 760.0
        val ctx = canvas.graphicsContext2D

        // 绘制方法
        val drawLine = EventHandler<MouseEvent> { event->
            ctx.moveTo(event.x, event.y)
            canvas.setOnMouseDragged {
                ctx.lineTo(it.x, it.y)
                ctx.stroke = colorPicker.value
                ctx.lineWidth = slider.value
                ctx.stroke()
            }
            canvas.setOnMouseReleased {
                canvas.onMouseDragged = null
            }
            ctx.beginPath()
        }

        val xpEvent = EventHandler<MouseEvent> {
            if (!root.children.contains(xpCircle)) {
                xpCircle.centerX = slider.value
                xpCircle.centerY = slider.value
                xpCircle.radius = slider.value / 2
                xpCircle.fill = Color.YELLOW
                root.children.add(xpCircle)
            }
            canvas.setOnMouseDragged {
                xpCircle.isVisible = true
                ctx.clearRect(it.x, it.y, slider.value, slider.value)
                val offset = slider.value/2
                // 绘制一个圆跟随鼠标移动
                xpCircle.relocate(it.x - offset + canvas.layoutX, it.y - offset)
            }
            canvas.setOnMouseReleased {
                canvas.onMouseDragged = null
                xpCircle.isVisible = false
            }
        }

        val txtEvent = EventHandler<MouseEvent> {
            if (!root.children.contains(txtField))
                root.children.add(txtField)
            txtField.relocate(it.x + canvas.layoutX, it.y)
            txtField.isVisible = true
            txtField.setOnAction { _ ->
                ctx.font = Font.font(slider.value)
                ctx.fill = colorPicker.value
                ctx.fillText(txtField.text, it.x, it.y)
                ctx.stroke()
                txtField.isVisible = false
            }
        }

        val cbEvent = Runnable {
            canvas.setOnMouseMoved {
                if (!root.children.contains(cbSp)) {
                    root.children.add(cbSp);
                }
                val offset = cbSp.fitWidth / 2
                // 绘制一个精灵跟随鼠标移动
                cbSp.relocate(it.x - offset + canvas.layoutX, it.y - offset + canvas.layoutY)
                cbSp.setOnMouseClicked { _ ->
                    ctx.drawImage(cbSp.image, it.x - offset, it.y - offset, cbSp.fitWidth, cbSp.fitHeight)
                }
            }
        }

        canvas.onMousePressed = drawLine
        tg.selectedToggleProperty().addListener { _, _, newValue ->
            // 初始化
            canvas.onMouseMoved = null
            canvas.onMouseDragged = null
            root.children.remove(xpCircle)
            root.children.remove(cbSp)

            canvas.onMousePressed = when (newValue) {
                pen -> drawLine
                xp -> xpEvent
                txt -> txtEvent
                cbTb -> {
                    cbEvent.run()
                    null
                }

                else -> drawLine
            }
        }

        slider.valueProperty().addListener { _, _, nv ->
            // 这个是橡皮的程序
            xpCircle.apply {
                centerX = nv.toDouble()
                centerY = nv.toDouble()
                radius = nv.toDouble() / 2
                fill = Color.YELLOW
            }

            // 这个是精灵的程序
            cbSp.apply {
                fitWidth = nv.toDouble()
                fitHeight = nv.toDouble()
            }
        }

        stage.title = "地图编辑"
        stage.show()
    }
}

class UiTest8 : Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage!!.title = "地图编辑"
        primaryStage.scene = Scene(WTDrawMap())
        primaryStage.show()
    }

}

fun main() {
    Application.launch(UiTest8::class.java)
}
