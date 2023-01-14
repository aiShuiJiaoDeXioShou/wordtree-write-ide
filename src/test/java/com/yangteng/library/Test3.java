package com.yangteng.library;

import cn.hutool.core.io.FileUtil;
import javafx.scene.input.Clipboard;
import org.junit.jupiter.api.Test;

public class Test3 {

    @Test
    public void test1() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        var file = clipboard.getString();
        System.out.println(file);
    }

    @Test
    public void test2() {
        var userHomePath = FileUtil.getUserHomePath();
        System.out.println(userHomePath);
    }
}
