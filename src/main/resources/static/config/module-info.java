module com.yangteng.library {
    requires javafx.controls;
    requires javafx.web;
    requires org.fxmisc.richtext;
    requires com.alibaba.fastjson2;
    requires sqlite.jdbc;
    requires org.mybatis;
    requires io.vertx.core;
    requires slf4j.log4j12;
    exports com.yangteng.library;
    exports com.yangteng.library.views.notebook.entity;
}