package lh.wordtree.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用这个在指定中间层中发布消息
 */
public abstract class Subscriber {
    private static final Map<String, ArrayList<Subscribe>> subscribes = new HashMap<>();

    /**
     * 响应订阅的请求
     * @param subscribe 消息源
     * @param handler 消息处理中心
     */
    public static void on(String subscribe, Subscribe handler) {
        ArrayList<Subscribe> sb = subscribes.get(subscribe);
        if (sb == null) sb = new ArrayList<>();
        sb.add(handler);
        subscribes.put(subscribe, sb);
    }

    /**
     * 订阅消息
     */
    public static void subscribe(String topic,String... demand) {
        subscribes.forEach((s, subscribes) -> {
            if (topic.equals(s)) {
                for (Subscribe subscribe : subscribes) {
                    subscribe.change(demand);
                }
            }
        });
    }
}
