package lh.wordtree.comm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;

public class WTFileUtils {

    public static void copyFolder(String oldPath, String newPath) {
        try {
            File oldFile = new File(oldPath);
            var newFile = new File(newPath);
            if (oldFile.isFile()) {
                Files.copy(oldFile.toPath(), newFile.toPath());
            } else {
                //如果文件夹不存在 则建立新文件夹
                String[] file = oldFile.list();
                newFile.mkdirs();
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
        if (file == null) return null;
        String filename = file.getName();
        //文件没有后缀名的情况
        if (filename.lastIndexOf(".") == -1) {
            return "";
        }
        // 这种返回的是没有.的后缀名
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public static String lastExtension(File file) {
        if (file == null) return null;
        String filename = file.getName();
        //文件没有后缀名的情况
        if (filename.lastIndexOf(".") == -1) {
            return "";
        }
        // 这种返回的是没有.的后缀名
        return filename.substring(filename.lastIndexOf("."));
    }

    public static String lastName(String fileName) {
        //文件没有后缀名的情况
        if (fileName.lastIndexOf(".") == -1) {
            return "";
        }
        // 这种返回的是没有.的后缀名
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String firstName(String fileName) {
        //文件没有后缀名的情况
        if (!fileName.contains("-")) {
            return "";
        }
        // 这种返回的是没有.的后缀名
        return fileName.substring(0, fileName.indexOf("-"));
    }

}
