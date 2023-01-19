package lh.wordtree;

import javafx.application.Application;
import javafx.scene.control.TreeItem;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class Test1 {

    public static URL getPath() {
        return Test1.class.getClassLoader().getResource("config.properties");
    }

    /**
     * 1.根据指定行读数据
     *
     * @param lineNumber
     */
    public static String readAppointedLineNumber(int lineNumber) {
        String appointedLine = "";
        FileReader in = null;
        LineNumberReader reader = null;
        try {
            in = new FileReader(getPath().toURI().getPath());
            reader = new LineNumberReader(in);
            long totalLine = Files.lines(Paths.get(getPath().toURI())).count();
            if (lineNumber < 0 || lineNumber > totalLine) {
                throw new Exception("指定行【" + lineNumber + "】不在文件行数范围内");
            }
            int line = 1;
            reader.setLineNumber(lineNumber);
            long i = reader.getLineNumber();
            String s = "";
            while ((s = reader.readLine()) != null) {
                if (i == line) {
                    appointedLine = s;
                    break;
                }
                line++;
            }
            return appointedLine;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(in, reader);
        }
        return appointedLine;
    }

    /**
     * 2.关闭资源
     *
     * @param in
     * @param reader
     */
    public static void closeResource(FileReader in, LineNumberReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据指定行写数据
     *
     * @param lineNumber 要存的行数
     * @param data       要存储的数据
     */
    public static void setAppointedLineNumber(int lineNumber, String data) throws Exception {
        Path path = Paths.get(getPath().toURI().getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        lines.set(lineNumber - 1, data);
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    @Test
    public void test1() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream(getPath().getFile()));
        String username = properties.getProperty("username");
        System.out.println(username);
    }

    @Test
    public void test2() throws Exception {
        String s = readAppointedLineNumber(2);
        System.out.println(s);
    }

    @Test
    public void test3() {
        var filesUtil = new FilesUtil(new File("D:/wordtree"));
        var files = filesUtil.getTree();
        System.out.println(files);
    }

    @Test
    public void test4() {

    }

    @Test
    public void test5() {
        Application.launch(JavaCoder.class);
    }

    class FilesUtil {
        private List<File> files = new ArrayList<>();
        private File root;
        private TreeItem<Text> treeRoot = new TreeItem();

        public FilesUtil(File root) {
            this.root = root;
        }

        public List<File> getFiles() {
            getFiles0(root);
            return files;
        }

        public TreeItem<Text> getTree() {
            getFiles1(root, treeRoot);
            return treeRoot;
        }

        private void getFiles1(File file, TreeItem<Text> treeItem) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                var item = new TreeItem<Text>();
                {
                    item.setValue(new Text(f.getName()));
                }
                if (f.isDirectory()) {
                    getFiles1(f, item);
                } else {
                    treeItem.getChildren().add(item);
                }
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

}
