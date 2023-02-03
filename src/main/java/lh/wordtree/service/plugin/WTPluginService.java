package lh.wordtree.service.plugin;

import lh.wordtree.plugin.WTPlugin;

import java.util.List;

public interface WTPluginService {
    List<WTPlugin> sendJar();

    List<WTPlugin> getWebPlugins();
}
