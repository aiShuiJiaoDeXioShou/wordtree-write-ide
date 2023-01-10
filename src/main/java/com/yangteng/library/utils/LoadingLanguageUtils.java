package com.yangteng.library.utils;

import com.alibaba.fastjson2.JSON;
import com.yangteng.library.views.notebook.entity.LanguageConfig;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public interface LoadingLanguageUtils {
    static LanguageConfig load(String languageCode) {
        Logger logger = LoggerFactory.getLogger(LoadingLanguageUtils.class);
        LanguageConfig languageConfig = null;
        InputStream load = null;
        try {
            load = ClassLoaderUtils.load("static/language-code/" + languageCode + "-code.json");
            logger.info("正在解析语言包位置: static/language-code/" + languageCode + "-code.json");
            if (load == null) {
                logger.error("没有该文件的解析资源包，请及时设计。");
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