package lh.wordtree.service.plugin;

import lh.wordtree.plugin.WTPlugLanguage;
import lh.wordtree.plugin.WTPlugin;
import lh.wordtree.plugin.WTPluginExtended;

import java.util.List;
import java.util.Map;

public interface WTPluginService {
    WTPluginService pluginService = new WTPluginServiceImpl("D:\\ytjava\\ideayt\\u3\\build\\libs");

    void sendJar();

    List<WTPlugin> getPlugins();

    List<WTPlugin> getWebPlugins();

    Map<String, WTPlugLanguage> getPlugLanguages();

    Map<String, WTPluginExtended> extendedPlugin();

    void registered(WTPlugin plugin);
}
