package com.yangteng.library.utils;

import com.alibaba.fastjson2.JSON;
import com.yangteng.library.comm.Config;
import com.yangteng.library.views.notebook.entity.LanguageConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface LoadingLanguageUtils {
    static LanguageConfig load(String languageCode) {
        LanguageConfig languageConfig = null;
        InputStream load = null;
        try {
            String path = Config.LANGUAGE_CODE_PATH + "/" + languageCode + "-code.json";
            load = new FileInputStream(path);
            if (load == null) {
                return new LanguageConfig();
            }
            languageConfig = JSON.parseObject(load.readAllBytes(), LanguageConfig.class);
            load.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return languageConfig;
    }
}