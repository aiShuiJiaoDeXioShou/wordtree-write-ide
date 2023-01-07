package com.yangteng.library.utils;

import javafx.scene.control.TreeItem;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileUtils {

    /**
     * 拿到指定路径所有的文件夹和文件
     */
    public static MyFiles getFiles(File file) {
        if (!file.isDirectory()) throw new RuntimeException("检测失败，该File对象并不是文件夹");
        var filesUtil = new MyFiles(file);
        return filesUtil;
    }

    public static class MyFiles {
        private List<File> files;
        private File root;
        private TreeItem<Text> treeRoot;
        private List<TreeItem<Text>> dirList = null;
        private List<TreeItem<Text>> fileList = null;

        public MyFiles(File root) {
            this.root = root;
        }

        public List<File> getFiles() {
            files = new ArrayList<>();
            getFiles0(root);
            return files;
        }

        public TreeItem<Text> getTree() {
            dirList = new ArrayList<>();
            fileList = new ArrayList<>();
            treeRoot = new TreeItem<>();
            {
                dirList.add(treeRoot);
                treeRoot.setValue(new Text(root.getName()));
            }
            getFiles1(root, treeRoot);
            return treeRoot;
        }

        public List<TreeItem<Text>> getDirList() {
            if (dirList == null) throw new RuntimeException("你的先启用getTree方法");
            return dirList;
        }

        public List<TreeItem<Text>> getFileList() {
            if (fileList == null) throw new RuntimeException("你的先启用getTree方法");
            return fileList;
        }

        private void getFiles1(File file, TreeItem<Text> treeItem) {
            File[] listFiles = file.listFiles();
            for (File f : Arrays.stream(listFiles).sorted().toList()) {
                var item = new TreeItem<Text>();
                {
                    var text = new Text(f.getName());
                    {
                        text.setId(f.getPath());
                    }
                    item.setValue(text);
                }
                if (f.isDirectory()) {
                    dirList.add(item);
                    getFiles1(f, item);
                } else {
                    fileList.add(item);
                }
                treeItem.getChildren().add(item);
            }
        }

        private void getFiles0(File file) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                if (f.isDirectory()) {
                    getFiles0(f);
                } else {
                    files.add(f);
                }
            }
        }
    }

    public static void copyFolder(String oldPath, String newPath) {
        try {
            //如果文件夹不存在 则建立新文件夹
            new File(newPath).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();
        }
    }

}
