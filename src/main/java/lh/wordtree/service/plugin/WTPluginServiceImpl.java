package lh.wordtree.service.plugin;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ReflectUtil;
import lh.wordtree.comm.utils.WTFileUtils;
import lh.wordtree.plugin.WTPlugLanguage;
import lh.wordtree.plugin.WTPlugin;
import lh.wordtree.plugin.wtlang.WTLangPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public class WTPluginServiceImpl implements WTPluginService {

    private String src;
    private List<WTPlugin> plugins = new ArrayList<>();
    private Map<String, WTPlugLanguage> plugLanguages = new HashMap<>();

    public WTPluginServiceImpl(String src) {
        this.src = src;
        sendJar();
        // 注册自带的插件
        registered(new WTLangPlugin());
    }

    public Map<String, WTPlugLanguage> getPlugLanguages() {
        return plugLanguages;
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
            jar.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
