package lh.wordtree.model.setting;

import cn.hutool.core.io.FileUtil;
import lh.wordtree.comm.config.Config;

import java.nio.charset.StandardCharsets;

public class SettingModel {
    public String getSettingValue() {
        return FileUtil.readString(Config.INIT_PATH, StandardCharsets.UTF_8.name());
    }
    public void saveSettingText(String text) {
        FileUtil.writeString(text, Config.INIT_PATH, StandardCharsets.UTF_8);
    }
}
