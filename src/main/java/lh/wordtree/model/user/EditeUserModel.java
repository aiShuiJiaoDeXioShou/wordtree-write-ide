package lh.wordtree.model.user;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import lh.wordtree.archive.entity.Author;
import lh.wordtree.comm.config.Config;

public class EditeUserModel  {
    // 刷新用户的配置文件信息
    public void flush(Author author) {
        var bytes = JSON.toJSONBytes(author, JSONWriter.Feature.PrettyFormat);
        FileUtil.writeBytes(bytes, Config.USER_CONFIG_PATH);
    }
}
