package lh.wordtree.service.plugin;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ReflectUtil;
import lh.wordtree.plugin.WTPlugin;
import lh.wordtree.utils.WTFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class WTPluginServiceImpl implements WTPluginService {

    private String src;
    private List<WTPlugin> plugins = new ArrayList<>();

    public WTPluginServiceImpl(String src) {
        this.src = src;
    }

    public String src() {
        return src;
    }

    /**
     * 扫描指定位置的jar包,加载本地的jar包
     */
    public List<WTPlugin> sendJar() {
        var file = new File(src());
        for (String f : file.list()) {
            if (WTFileUtils.lastName(f).equals("jar")) {
                parsePlugin(src() + "/" + f);
            }
        }
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
            jar.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
