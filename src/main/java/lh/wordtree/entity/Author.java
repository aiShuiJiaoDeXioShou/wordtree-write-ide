package lh.wordtree.entity;

public record Author(
        String name, // 笔名
        String signature, // 签名
        String imgPath, // 头像路径
        String password // 加密密码
) {
}
