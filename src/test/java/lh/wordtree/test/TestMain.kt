package lh.wordtree.test

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import java.awt.MouseInfo
import java.awt.Robot
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TestMain : Application() {

    override fun start(primaryStage: Stage) {
        val root = StackPane()
        val scene = Scene(root, 300.0, 250.0)
        primaryStage.scene = scene
        primaryStage.show()
        val robot = Robot()
        val service = Executors.newSingleThreadScheduledExecutor()
        service.scheduleAtFixedRate({
            // 监听鼠标的移动事件，并储存在内存当中
            val point = MouseInfo.getPointerInfo().location
            println("Mouse moved to: $point")
            // 监听键盘事件
        }, 0, 1, TimeUnit.SECONDS)
        WinKeyBoard().run()
    }

}

fun main() {
    Application.launch(TestMain::class.java)
}

