package lh.wordtree.uitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchAlgorithm {

    private TrieNode root;

    public MatchAlgorithm() {
        root = new TrieNode();
    }

    /**
     * 添加一个数据源
     *
     * @param word 数据源的字符串
     */
    public void add(String word) {
        if (word == null || word.length() == 0) {
            return;
        }
        TrieNode cur = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!cur.children.containsKey(c)) {
                cur.children.put(c, new TrieNode());
            }
            cur = cur.children.get(c);
        }
        cur.isWord = true;
    }

    /**
     * 匹配输入值，并返回匹配到的值
     *
     * @param input 输入值
     * @return 匹配到的值，按照优先级排序
     */
    public List<String> verify(String input) {
        List<String> res = new ArrayList<>();
        if (input == null || input.length() == 0) {
            return res;
        }
        TrieNode cur = root;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!cur.children.containsKey(c)) {
                break;
            }
            cur = cur.children.get(c);
            if (cur.isWord) {
                res.add(input.substring(0, i + 1));
            }
        }
        return res;
    }

    // Trie树的节点类
    private class TrieNode {
        Map<Character, TrieNode> children;
        boolean isWord;

        public TrieNode() {
            children = new HashMap<>();
            isWord = false;
        }
    }
}

