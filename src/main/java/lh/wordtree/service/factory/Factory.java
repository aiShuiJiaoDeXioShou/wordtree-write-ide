package lh.wordtree.service.factory;

import javafx.scene.web.WebView;

public abstract class Factory {
    public static WebView getWebView() {
        return new WebView();
    }
}
