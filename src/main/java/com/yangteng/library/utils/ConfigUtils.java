package com.yangteng.library.utils;

import java.io.IOException;
import java.util.Properties;

public interface ConfigUtils {

    static String getProperties(String name) {
        Properties properties = new Properties();{
            try {
                properties.load(ClassLoaderUtils.load("static/config/init.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty(name);
    }
}
