package com.yangteng.library;

import javafx.scene.input.Clipboard;
import org.junit.jupiter.api.Test;

public class Test3 {

    @Test
    public void test1() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        var file = clipboard.getString();
        System.out.println(file);
    }
}
