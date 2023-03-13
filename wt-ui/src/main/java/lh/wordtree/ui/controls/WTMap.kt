package lh.wordtree.ui.controls

import javafx.scene.canvas.Canvas
import javafx.scene.layout.BorderPane
import java.awt.Label

class WTMap() : BorderPane() {
    val canvas = Canvas()
    val context2D = canvas.graphicsContext2D

    init {
        canvas.width
        center = canvas
    }

    /**
     * 普通的按鈕对象
     */
    class Bi() : Label() {

    }
}