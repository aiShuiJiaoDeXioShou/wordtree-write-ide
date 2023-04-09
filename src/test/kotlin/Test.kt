import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import lh.wordtree.plugin.bookshelf.Bookshelf

class UiTest6 : Application() {
    val root = StackPane()
    val scene = Scene(root, 500.0, 600.0)
    override fun start(primaryStage: Stage?) {
        primaryStage!!.title = "UI TEST"
        root.children.add(Bookshelf().view())
        primaryStage.scene = scene
        primaryStage.show()
    }

}

fun main() {
    Application.launch(UiTest6::class.java)
}