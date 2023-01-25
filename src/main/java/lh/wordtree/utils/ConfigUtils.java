package lh.wordtree.utils;

import lh.wordtree.config.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public interface ConfigUtils {

    static String getProperties(String name) {
        Properties properties = new Properties();{
            try {
                properties.load(new FileInputStream(Config.INIT_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty(name);
    }
}
