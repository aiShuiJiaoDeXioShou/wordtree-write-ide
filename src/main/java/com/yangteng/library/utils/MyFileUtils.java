package com.yangteng.library.utils;

import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MyFileUtils {
    private List<File> files;
    private File root;
    private TreeItem<Label> treeRoot;
    private List<TreeItem<Label>> dirList = null;
    private List<TreeItem<Label>> fileList = null;

    public MyFileUtils(File root) {
        this.root = root;
    }

    public List<File> getFiles() {
        files = new ArrayList<>();
        getFiles0(root);
        return files;
    }

    public TreeItem<Label> getTree() {
        dirList = new ArrayList<>();
        fileList = new ArrayList<>();
        treeRoot = new TreeItem<>();
        {
            dirList.add(treeRoot);
            var text = new Label(root.getName());
            text.setId(root.getPath());
            treeRoot.setValue(text);
        }
        if (root.isDirectory()) getFiles1(root, treeRoot);
        return treeRoot;
    }

    public List<TreeItem<Label>> getDirList() {
        if (dirList == null) throw new RuntimeException("你的先启用getTree方法");
        return dirList;
    }

    public List<TreeItem<Label>> getFileList() {
        if (fileList == null) throw new RuntimeException("你的先启用getTree方法");
        return fileList;
    }

    private void getFiles1(File file, TreeItem<Label> treeItem) {
        File[] listFiles = file.listFiles();
        assert listFiles != null;
        for (File f : Arrays.stream(listFiles).sorted().toList()) {
            var item = new TreeItem<Label>();
            {
                var text = new Label(f.getName());
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

    public static void copyFolder(String oldPath, String newPath) {
        try {
            File oldFile = new File(oldPath);
            //如果文件夹不存在 则建立新文件夹
            var newFile = new File(newPath);
            if (oldFile.isFile()) {
                Files.copy(oldFile.toPath(), newFile.toPath());
            } else {
                newFile.mkdirs();
                String[] file = oldFile.list();
                File temp;
                for (int i = 0; i < file.length; i++) {
                    if (oldPath.endsWith(File.separator)) {
                        temp = new File(oldPath + file[i]);
                    } else {
                        temp = new File(oldPath + File.separator + file[i]);
                    }
                    if (temp.isFile()) {
                        FileInputStream input = new FileInputStream(temp);
                        FileOutputStream output = new FileOutputStream(newPath + "/" +
                                (temp.getName()));
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
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();
        }
    }

    public static String lastName(File file){
        if(file==null) return null;
        String filename = file.getName();
        //文件没有后缀名的情况
        if(filename.lastIndexOf(".")==-1){
            return "";
        }
        // 这种返回的是没有.的后缀名
        return filename.substring(filename.lastIndexOf(".")+1);
    }

}
