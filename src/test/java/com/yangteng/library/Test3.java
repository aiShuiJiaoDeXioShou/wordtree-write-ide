package com.yangteng.library;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import javafx.scene.input.Clipboard;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static void main(String[] args) {
        var parse = LocalDateTimeUtil.parse("2023-01-14 04:38:58", DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        System.out.println(parse);
    }

    @Test
    public void test3() {
        var parse = LocalDateTime.parse("2023-01-14 04:38:58");
        System.out.println(parse);
    }
}
