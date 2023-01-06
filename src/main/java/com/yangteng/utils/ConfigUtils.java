package com.yangteng.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class ConfigUtils {

    public static String getProperties(String name) {
        Properties properties = new Properties();{
            try {
                properties.load(new FileInputStream(getPath().getFile()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty(name);
    }

    public static URL getPath() {
        return ConfigUtils.class.getClassLoader().getResource("config.properties");
    }
}
