package lh.wordtree.ui.controls

import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import lh.wordtree.ui.utils.ClassLoaderUtils

class WTDrawMap : Pane() {
    val canvas = Canvas()
    val pane = BorderPane()

    // 画笔的选项卡
    val colorPicker = ColorPicker(Color.BLACK)

    // 粗细
    val slider = Slider(1.0, 100.0, 1.0)

    // 工具选项卡
    val tg = ToggleGroup()
    val pen = ToggleButton("画笔")
    val xp = ToggleButton("橡皮")
    val circle = ToggleButton("圆形")
    val rectangle = ToggleButton("方块")
    val txt = ToggleButton("文字")
    val select = ToggleButton("选择")
    val hbox = HBox()
    val box = VBox()

    // 橡皮工具
    val xpCircle = Circle()

    // 文字工具框
    val txtField = TextField()
    val centerPane = Pane()

    // 图章工具
    val imageBox = HBox()
    private fun generateTolBut(txt: String, icon: String) = ToggleButton(txt).apply {
        this.graphic = ImageView(Image(ClassLoaderUtils.url(icon))).apply {
            fitWidth = 35.0
            fitHeight = 35.0
        }
    }

    private fun generateTolBut(icon: String) = ImageView(Image(ClassLoaderUtils.url(icon))).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }

    val cbTb = generateTolBut("\n城堡", "static/draw/城堡.png")
    val cbSp = generateTolBut("static/draw/城堡.png")

    // 画板功能的实现
    val ctx = canvas.graphicsContext2D

    // 线条绘制
    private val drawLine = EventHandler<MouseEvent> { event ->
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

    // 绘制圆形
    private val drawCircle = EventHandler<MouseEvent> { event ->
        ctx.stroke = colorPicker.value
        ctx.lineWidth = slider.value
        var x = event.x
        var y = event.y
        canvas.setOnMouseDragged {
            ctx.strokeArc(it.x, it.y, 100.0, 100.0, 0.0, 360.0, ArcType.OPEN)
        }
        canvas.setOnMouseReleased {
            canvas.onMouseDragged = null
        }
    }

    // 橡皮工具绘制
    private val xpEvent = EventHandler<MouseEvent> {
        if (!this.children.contains(xpCircle)) {
            xpCircle.centerX = slider.value
            xpCircle.centerY = slider.value
            xpCircle.radius = slider.value / 2
            xpCircle.fill = Color.YELLOW
            this.children.add(xpCircle)
        }
        canvas.setOnMouseDragged {
            xpCircle.isVisible = true
            ctx.clearRect(it.x, it.y, slider.value, slider.value)
            val offset = slider.value / 2
            // 绘制一个圆跟随鼠标移动
            xpCircle.relocate(it.x - offset + centerPane.layoutX, it.y - offset + centerPane.layoutY)
        }
        canvas.setOnMouseReleased {
            canvas.onMouseDragged = null
            xpCircle.isVisible = false
        }
    }

    // 字体工具绘制
    private val txtEvent = EventHandler<MouseEvent> {
        if (!this.children.contains(txtField))
            this.children.add(txtField)
        txtField.relocate(it.x + centerPane.layoutX, it.y + centerPane.layoutY)
        txtField.isVisible = true
        txtField.setOnAction { _ ->
            ctx.font = Font.font(slider.value)
            ctx.fill = colorPicker.value
            ctx.fillText(txtField.text, it.x, it.y)
            txtField.isVisible = false
        }
    }

    // 图章工具绘制
    private fun cbEvent() {
        canvas.setOnMouseMoved {
            if (!this.children.contains(cbSp)) {
                this.children.add(cbSp)
            }
            val offset = cbSp.fitWidth / 2
            // 绘制一个精灵跟随鼠标移动
            cbSp.relocate(it.x - offset + centerPane.layoutX, it.y - offset + centerPane.layoutY)
            cbSp.setOnMouseClicked { _ ->
                ctx.drawImage(cbSp.image, it.x - offset, it.y - offset, cbSp.fitWidth, cbSp.fitHeight)
            }
        }
    }

    init {
        // 初始化主结构参数
        prefHeightProperty().bind(pane.heightProperty())
        prefWidthProperty().bind(pane.widthProperty())
        pane.left = box
        canvas.width = 1080.0
        canvas.height = 860.0
        centerPane.style = "-fx-background-color: white;-fx-effect: dropshadow(three-pass-box,#8c8c8c, 10.0,0, 0, 0);"
        centerPane.children.add(canvas)
        pane.center = centerPane
        children.add(pane)

        box.children.addAll(colorPicker, slider, hbox, imageBox)
        box.spacing = 10.0
        box.prefHeightProperty().bind(pane.heightProperty())
        box.style = "-fx-background-color: white;-fx-padding: 10;"

        // 设置画笔组
        pen.toggleGroup = tg
        pen.isSelected = true
        xp.toggleGroup = tg
        txt.toggleGroup = tg
        circle.toggleGroup = tg
        rectangle.toggleGroup = tg
        hbox.children.addAll(pen, xp, txt, circle, rectangle, select)

        // 设置图章工具
        cbTb.toggleGroup = tg
        imageBox.children.addAll(cbTb)

        canvas.onMousePressed = drawLine

        tg.selectedToggleProperty().addListener { _, _, newValue ->
            // 初始化
            canvas.onMouseMoved = null
            canvas.onMouseDragged = null
            this.children.remove(xpCircle)
            this.children.remove(cbSp)

            canvas.onMousePressed = when (newValue) {
                pen -> drawLine
                xp -> xpEvent
                txt -> txtEvent
                cbTb -> {
                    cbEvent()
                    null
                }

                circle -> drawCircle
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
    }
}