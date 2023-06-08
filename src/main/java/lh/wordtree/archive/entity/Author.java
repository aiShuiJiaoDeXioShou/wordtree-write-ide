package lh.wordtree.archive.entity;

import java.util.ArrayList;

public record Author(
        String name, // 笔名
        String signature, // 签名
        String imgPath, // 头像路径
        String password, // 加密密码
        ArrayList<String> tag,
        ArrayList<String> accomplishment
) {
}
