package lh.wordtree.service;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

public class WebStarts {
    public final static HttpServer server = Vertx.vertx().createHttpServer();

    public WebStarts start() {
        this.httpService();
        return this;
    }

    public WebStarts stop() {
        server.close();
        return this;
    }

    private void httpService() {
        // 启动http服务器
        server.requestHandler(request -> {
            // 所有的请求都会调用这个处理器处理
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "text/plain");
            // 写入响应并结束处理
            response.end("Hello World!");
        });
        server.listen(8999);
    }

}
