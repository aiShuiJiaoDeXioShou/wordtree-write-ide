package lh.wordtree.ui.controls

import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Skin
import javafx.scene.control.skin.ButtonSkin
import lh.wordtree.ui.utils.ClassLoaderUtils


class WTButton : Button {
    private val STYLE_CLASS = "wt-button";
    private val STYLESHEET: String = ClassLoaderUtils.loadUrl("static/css/WTButton.css")

    constructor() {
        init()
    }

    constructor(text: String?) : super(text) {
        init()
    }

    constructor(text: String?, graphic: Node?) : super(text, graphic) {
        init()
    }

    private fun init() {
        this.styleClass.add(STYLE_CLASS)
        println("样式表如图：" + STYLESHEET)
    }

    override fun createDefaultSkin(): Skin<*> {
        return WTButtonSkin(this)
    }

    override fun getUserAgentStylesheet(): String {
        return STYLESHEET
    }
}

class WTButtonSkin(val button: WTButton) : ButtonSkin(button) {

}