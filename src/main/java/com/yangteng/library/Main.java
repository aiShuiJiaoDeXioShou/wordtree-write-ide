package com.yangteng.library;

import com.yangteng.library.utils.ClassLoaderUtils;
import javafx.application.Application;
import org.apache.log4j.PropertyConfigurator;

public class Main {
    public static void main(String[] args) {
        PropertyConfigurator.configure(ClassLoaderUtils.load("static/config/log4j.properties"));
        Application.launch(Index.class);
    }
}
