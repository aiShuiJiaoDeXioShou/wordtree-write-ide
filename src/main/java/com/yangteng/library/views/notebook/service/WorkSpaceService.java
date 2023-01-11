package com.yangteng.library.views.notebook.service;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.yangteng.library.utils.ClassLoaderUtils;
import com.yangteng.library.views.notebook.entity.RecentFiles;

import java.io.IOException;
import java.util.List;

public interface WorkSpaceService {

    static void save(List<RecentFiles> recentFiles) {
        try {
            FileUtil.writeBytes(
                    JSON.toJSONBytes(recentFiles, JSONWriter.Feature.PrettyFormat),
                    WorkSpaceService.class.getClassLoader().getResource("static/config/workspace.json").toURI().getPath()
            );
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
