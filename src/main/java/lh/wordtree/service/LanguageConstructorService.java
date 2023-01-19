package lh.wordtree.service;

import com.alibaba.fastjson2.JSON;
import javafx.scene.Node;
import lh.wordtree.comm.Config;
import lh.wordtree.entity.LanguageConfig;
import lh.wordtree.utils.WTFileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface LanguageConstructorService {
    Node build();

    static LanguageConfig initLanguage(File languageFile) {
        LanguageConfig languageConfig;
        InputStream load;
        try {
            String path = Config.LANGUAGE_CODE_PATH + "/" + WTFileUtils.lastName(languageFile) + "-code.json";
            load = new FileInputStream(path);
            if (load == null) {
                return new LanguageConfig();
            }
            languageConfig = JSON.parseObject(load.readAllBytes(), LanguageConfig.class);
            load.close();
        } catch (IOException e) {
            return null;
        }
        return languageConfig;
    }
}