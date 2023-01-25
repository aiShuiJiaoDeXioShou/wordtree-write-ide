package lh.wordtree.service.setting;

import cn.hutool.core.io.FileUtil;
import lh.wordtree.comm.Config;

import java.nio.charset.StandardCharsets;

public interface SettingService {

    /**
     * 返回所有设置文本
     */
    default String getSettingValue() {
        return FileUtil.readString(Config.INIT_PATH, StandardCharsets.UTF_8.name());
    }

    /**
     * 根据文本生成GUI
     */
    default void generateGUI() {
    }

    /**
     * 保存设置文件
     */
    default void saveSettingText(String text) {
        FileUtil.writeString(text, Config.INIT_PATH, StandardCharsets.UTF_8);
    }

}
