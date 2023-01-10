package com.yangteng.library.views.notebook.service;

import com.sun.jna.platform.FileUtils;
import com.yangteng.library.utils.ConfigUtils;
import com.yangteng.library.utils.MyFileUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public interface FileService {

    default void copy(String file) {
        // 获取系统的剪切板
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.put(DataFormat.FILES, List.of(new File(file)));
        clipboard.setContent(clipboardContent);
    }

    default boolean del(String filePath) {
        if (Boolean.parseBoolean(ConfigUtils.getProperties("isDelRecycling"))) {
            // 将指定文件添加到回收站里面
            FileUtils fileUtils = FileUtils.getInstance();
            if (fileUtils.hasTrash()) {
                try {
                    fileUtils.moveToTrash(new File[] { new File(filePath) });
                    return true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            // 若配置文件不为true则永久删除该项
           return new File(filePath).delete();
        }
        return false;
    }

    default List<String> paste(String target) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        var sourceFiles = clipboard.getFiles();
        System.out.println(sourceFiles);
        var targetFile = new File(target);
        if (targetFile.isFile()) {
            targetFile = targetFile.getParentFile();
        }
        var files = new ArrayList<String>();
        for (File sourceFile : sourceFiles) {
            String newFilePath = targetFile.getPath() + "\\" + sourceFile.getName();
            files.add(newFilePath);
            containsFile(sourceFile, newFilePath);
        }
        return files;
    }

    private static String containsFile(File sourceFile, String newFilePath) {
        var newFile = new File(newFilePath);
        // 判断拷贝目标目录是否存在相同的文件
        if (newFile.exists()) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("重命名");
            var textField = new TextField();
            textField.setText(newFile.getName());
            // 文本框设置聚焦包过选中文本
            File finalNewFile = newFile;
            Platform.runLater(() -> {
                textField.requestFocus();
                textField.selectRange(0, finalNewFile.getName().length());
            });
            alert.setGraphic(textField);
            var buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.OK) {
                String newPath = newFile.getParent() + "\\" + textField.getText();
                newFile = new File(newPath);
                try {
                    // 对sourceFile文件进行复制到目标文件目录
                    MyFileUtils.copyFolder(sourceFile.getPath(), newFile.getPath());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else  {
            try {
                // 对sourceFile文件进行复制到目标文件目录
                MyFileUtils.copyFolder(sourceFile.getPath(), newFile.getPath());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return newFile.getPath();
    }

    default boolean createFile(String path) {
        var file = new File(path);
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    default boolean createFolder(String path) {
        var file = new File(path);
        if (!file.exists()) {
            try {
                return file.mkdirs();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    default void openFileDir(String file) {
        try {
            String[] cmd = new String[]{
                    "cmd","/c","start"," ",file
            };
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default String rename(String file, String rename) {
        var f = new File(file);
        var newPath = f.getParent() + "\\" + rename;
        var b = f.renameTo(new File(newPath));
        return b?newPath: null;
    }

}
