package lh.wordtree.service;

/**
 * 这是一个接口用于订阅某类消息的发布
 */
public interface Subscribe {

    void change(String... message);

}
