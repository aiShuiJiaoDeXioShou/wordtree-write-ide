package lh.wordtree.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用这个在指定中间层中发布消息
 */
public class Subscriber {
    private final List<Subscribe> subscribeList = new ArrayList<Subscribe>();

    public void register(Subscribe subscribe) {
        subscribeList.add(subscribe);
    }

    /**
     * 发布消息
     */
    public void on(String topic) {
        subscribeList.forEach(subscribe -> subscribe.change(topic));
    }

    /**
     * 发布意外情况
     */
    public void error(String message) {
        subscribeList.forEach(subscribe -> subscribe.change(message));
    }
}
