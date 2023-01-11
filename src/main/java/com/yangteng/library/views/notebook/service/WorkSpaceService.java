package com.yangteng.library.views.notebook.service;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.yangteng.library.comm.Config;
import com.yangteng.library.views.notebook.entity.RecentFiles;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface WorkSpaceService {

    static void save(List<RecentFiles> recentFiles) {
        try {
            FileUtil.writeBytes(JSON.toJSONBytes(recentFiles, JSONWriter.Feature.PrettyFormat), Config.WORKSPACE_PATH);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static List<RecentFiles> get() {
        InputStream inputStream = null;
        List<RecentFiles> recentFiles;
        try {
            inputStream = new FileInputStream(Config.WORKSPACE_PATH);
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
