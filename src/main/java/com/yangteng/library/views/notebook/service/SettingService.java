package com.yangteng.library.views.notebook.service;

import cn.hutool.core.io.FileUtil;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public interface SettingService {

    default URL getSettingIniFilePath() {
        try {
            return SettingService.class.getClassLoader().getResource("static/config/init.properties").toURI().toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回所有设置文本
     */
    default String getSettingValue() {
        return FileUtil.readString(getSettingIniFilePath(), StandardCharsets.UTF_8.name());
    }

    /**
     * 根据文本生成GUI
     */
    default void generateGUI() {

    }

    /**
     * 保存设置文件
     */
    default void saveSettingText(String text) {
        FileUtil.writeString(text, this.getSettingIniFilePath().getPath(), StandardCharsets.UTF_8);
    }

}
