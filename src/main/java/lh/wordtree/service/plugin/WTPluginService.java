package lh.wordtree.service.plugin;

import lh.wordtree.plugin.WTPlugLanguage;
import lh.wordtree.plugin.WTPlugin;

import java.util.List;

public interface WTPluginService {
    WTPluginService pluginService = new WTPluginServiceImpl("D:\\ytjava\\ideayt\\u3\\build\\libs");
    List<WTPlugin> sendJar();

    List<WTPlugin> getWebPlugins();

    List<WTPlugLanguage> getPlugLanguages();
}
