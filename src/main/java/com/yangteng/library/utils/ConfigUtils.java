package com.yangteng.library.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class ConfigUtils {

    public static String getProperties(String name) {
        Properties properties = new Properties();{
            try {
                properties.load(ConfigUtils.class.getClassLoader().getResourceAsStream("static/config/init.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty(name);
    }

    public static URL getPath() {
        return ConfigUtils.class.getClassLoader().getResource("static/config/init.properties");
    }
}
