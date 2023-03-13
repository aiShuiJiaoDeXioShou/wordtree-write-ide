package lh.wordtree.plugin.bookshelf

import javafx.scene.Node
import javafx.scene.image.Image
import lh.wordtree.plugin.WTPlugin
import lh.wordtree.plugin.WTPluginConfig
import lh.wordtree.plugin.WTPluginType

class Bookshelf() : WTPlugin {
    override fun view(): Node {
        return super.view()
    }

    override fun config(): WTPluginConfig {
        return object : WTPluginConfig {
            override fun name(): String {
                return "阅读器服务"
            }

            override fun version(): String {
                TODO("Not yet implemented")
            }

            override fun author(): String {
                return "林河"
            }

            override fun icon(): Image {
                TODO("Not yet implemented")
            }

            override fun introduce(): String {
                TODO("Not yet implemented")
            }

            override fun type(): WTPluginType {
                return WTPluginType.menu
            }

        }
    }

}