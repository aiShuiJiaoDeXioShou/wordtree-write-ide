package lh.wordtree.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 编辑指定文件指定行数的工具类，用于编写config文件
 */
public interface FileLinesUtils {

    /**
     * 1.根据指定行读数据
     *
     * @param lineNumber
     * @param path  读取文件的文件路径
     */
    static String readAppointedLineNumber(int lineNumber,String path) {
        String appointedLine = "";
        FileReader in = null;
        LineNumberReader reader = null;
        try {
            in = new FileReader(path);
            reader = new LineNumberReader(in);
            long totalLine = Files.lines(Paths.get(path)).count();
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
    static void closeResource(FileReader in, LineNumberReader reader) {
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
     * @param filePath   要储存的文件路径
     */
    static void setAppointedLineNumber(String filePath, int lineNumber, String data) throws Exception {
        Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        lines.set(lineNumber - 1, data);
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

}
