package com.yangteng.library.utils;

import javafx.scene.Node;

import java.util.Map;

public interface FxStyleUtils {
    static void buildMapStyle(Node node, Map<String, String> styleMap) {
        var str = new StringBuffer();
        styleMap.forEach((k, v) -> {
            str.append(k).append(":").append(v).append(";");
        });
        node.setStyle(str.toString());
    }
}
