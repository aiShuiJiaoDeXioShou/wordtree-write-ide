package lh.wordtree.service.plugin;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ReflectUtil;
import lh.wordtree.comm.config.Config;
import lh.wordtree.comm.utils.WTFileUtils;
import lh.wordtree.plugin.WTPlugLanguage;
import lh.wordtree.plugin.WTPlugin;
import lh.wordtree.plugin.WTPluginExtended;
import lh.wordtree.plugin.action.ActionPlugin;
import lh.wordtree.plugin.bookshelf.Bookshelf;
import lh.wordtree.plugin.wtlang.WTLangPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public class WTPluginServiceImpl implements WTPluginService {

    private String src;
    private List<WTPlugin> plugins = new ArrayList<>();
    private Map<String, WTPlugLanguage> plugLanguages = new HashMap<>();
    private Map<String, WTPluginExtended> extendeds = new HashMap<>();

    public WTPluginServiceImpl(String src) {
        Path of = Path.of(src);
        if (!Files.exists(of)) {
            try {
                Files.createDirectory(of);
            } catch (IOException e) {
                Config.log.error("创建文件失败！");
            }
        }
        this.src = src;
        sendJar();
        // 注册自带的插件
        registered(new WTLangPlugin());
        registered(new ActionPlugin());
        registered(new Bookshelf());
    }

    public Map<String, WTPlugLanguage> getPlugLanguages() {
        return plugLanguages;
    }

    public Map<String, WTPluginExtended> extendedPlugin() {
        return extendeds;
    }

    /**
     * 手动注册插件
     *
     * @param plugin 需要注册的插件
     */
    public void registered(WTPlugin plugin) {
        plugins.add(plugin);
        if (plugin instanceof WTPlugLanguage language) {
            plugLanguages.put(language.config().file(), language);
        }
        if (plugin instanceof WTPluginExtended extended) {
            extendeds.put(extended.config().name(), extended);
        }
    }

    public String src() {
        return src;
    }

    /**
     * 扫描指定位置的jar包,加载本地的jar包
     */
    public void sendJar() {
        if (plugins.size() > 0) {
            plugins = new ArrayList<>();
        }
        var file = new File(src());
        for (String f : file.list()) {
            if (WTFileUtils.lastName(f).equals("jar")) {
                parsePlugin(src() + "/" + f);
            }
        }
    }

    public List<WTPlugin> getPlugins() {
        return plugins;
    }

    /**
     * 加载远程仓库的jar包并且下载到本地
     */
    public List<WTPlugin> getWebPlugins() {

        return null;
    }

    /**
     * 根据jar文件解析一个一个插件文件
     *
     * @param jarPath
     */
    private void parsePlugin(String jarPath) {
        try {
            var jar = new JarFile(jarPath);
            var manifest = jar.getManifest();
            var pluginClassName = manifest.getMainAttributes().getValue("Plugin-Start");
            Class<?> loadClass = ClassLoaderUtil.loadClass(new File(src()), pluginClassName);
            Object o = ReflectUtil.newInstance(loadClass);
            if (o instanceof WTPlugin plugin) {
                plugins.add(plugin);
            }
            if (o instanceof WTPlugLanguage plugLanguage) {
                plugLanguages.put(plugLanguage.config().file(), plugLanguage);
            }
            if (o instanceof WTPluginExtended extended) {
                extendeds.put(extended.config().name(), extended);
            }
            jar.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
