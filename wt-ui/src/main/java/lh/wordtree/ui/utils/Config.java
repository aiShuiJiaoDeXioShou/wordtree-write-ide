package lh.wordtree.ui.utils;

import javafx.scene.Scene;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.util.Objects;

public class Config {
    public static Theme theme() {
        return Theme.LIGHT;
    }

    public static void setStyle(Scene scene) {
        JMetro metro;
        if (theme().equals(Theme.LIGHT)) {
            metro = new JMetro(Style.LIGHT);
        } else metro = new JMetro(Style.DARK);
        metro.setScene(scene);
        // 必须要在setScene下面,得先设置了场景,才能设置覆盖样式
        metro.getOverridingStylesheets().addAll(
                getStyle("static/style/light.css"),
                getStyle("static/style/base.css")
        );
    }

    public static String getStyle(String path) {
        return Objects.requireNonNull(Config.class.getClassLoader().getResource(path)).toExternalForm();
    }

    public enum Theme {
        DARK, LIGHT
    }
}
