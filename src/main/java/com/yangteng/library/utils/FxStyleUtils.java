package com.yangteng.library.utils;

import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.util.Map;

public interface FxStyleUtils {
    static void buildMapStyle(Node node, Map<String, String> styleMap) {
        var str = new StringBuffer();
        styleMap.forEach((k, v) -> {
            str.append(k).append(":").append(v).append(";");
        });
        node.setStyle(str.toString());
    }

    /**
     * 设置节点拥有通用的border属性
     */
    static void setNodeBorder(Region node, double width, BorderPos borderPos) {
        BorderWidths borderWidths = null;
        switch (borderPos) {
            case DEF -> new BorderWidths(width);
            case LEFT -> new BorderWidths(0, 0, 0, width);
            case RIGHT -> new BorderWidths(0, width, 0, 0);
            case BOTTOM -> new BorderWidths(0, 0, width, 0);
            case TOP -> new BorderWidths(width, 0, 0, 0);
        }
        var borderStroke = new BorderStroke(Paint.valueOf("#f8f9fa"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, borderWidths);
        var border = new Border(borderStroke);
        node.setBorder(border);
    }

    enum BorderPos {
        LEFT, RIGHT, BOTTOM, TOP, DEF;
    }
}
