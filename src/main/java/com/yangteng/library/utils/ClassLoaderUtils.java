package com.yangteng.library.utils;

import com.yangteng.library.Launcher;

import java.io.InputStream;

public interface ClassLoaderUtils {

    static InputStream load(String path) {
        return Launcher.class.getClassLoader().getResourceAsStream(path);
    }

    static String loadConfig(String name) {
        // 通过所在不同系统配置具体文件路径
        return "";
    }

}
