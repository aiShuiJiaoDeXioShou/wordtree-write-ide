package com.yangteng.library.views.notebook.dao;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.yangteng.library.utils.ClassLoaderUtils;
import com.yangteng.library.views.notebook.entity.RecentFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public interface WorkSpaceMapper {

    static void save(List<RecentFiles> recentFiles) {
        try {
            var resource = ClassLoader.getSystemClassLoader().getResource("static/config/workspace.json");
            assert resource != null;
            Files.write(Path.of(resource.toURI()), JSON.toJSONBytes(recentFiles, JSONWriter.Feature.PrettyFormat));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static List<RecentFiles> get() {
        var inputStream = ClassLoaderUtils.load("static/config/workspace.json");
        assert inputStream != null;
        List<RecentFiles> recentFiles;
        try {
            recentFiles = JSON.parseArray(inputStream.readAllBytes(), RecentFiles.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return recentFiles;
    }

}
