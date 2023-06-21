package lh.wordtree.plugin.bookshelf;

import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ReaderTextBuilder {
    private final List<String> titles = new ArrayList<>();
    private final List<C> cs = new ArrayList<>();
    public File file;

    public static @NotNull ReaderTextBuilder builder(File file) {
        ReaderTextBuilder self = new ReaderTextBuilder();
        self.file = file;
        return self;
    }

    public List<C> find() {
        Pattern p = Pattern.compile("(^第\\d+章|^第[一二三四五六七八九十]|^\\d+).+[^\\r\\n]+");
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            StringBuilder buffer = new StringBuilder();
            C c = null;
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                Matcher matcher = p.matcher(s);
                if (matcher.find()) {
                    if (buffer.length() > 0) buffer.delete(0, buffer.length() - 1);
                    String title = matcher.group();
                    c = new C(title, buffer.toString());
                    cs.add(c);
                } else {
                    buffer.append(s).append('\n');
                    if (c != null) c.content = buffer.toString();

                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("文件读取异常：\n" + e.getMessage());
        } catch (IOException e) {
            System.err.println("读取文本行内时操作异常：\n" + e.getMessage());
        }
        return cs;
    }

    /**
     * 只获取章节标题，不获取内容信息
     */
    public List<String> findTitle() {
        if (cs.isEmpty()) {
            Pattern p = Pattern.compile("(^第\\d+章|^第[一二三四五六七八九十]|^\\d+).+[^\\r\\n]+");
            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                    Matcher matcher = p.matcher(s);
                    if (matcher.find()) {
                        String title = matcher.group();
                        titles.add(title);
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("文件读取异常：\n" + e.getMessage());
            } catch (IOException e) {
                System.err.println("读取文本行内时操作异常：\n" + e.getMessage());
            }
        } else {
            return cs.stream()
                    .map(cs -> cs.title)
                    .toList();
        }
        return titles;
    }

    /**
     * 根据章节标题，获取内容信息
     */
    public String findTitleByContent(String title) {
        if (cs.isEmpty()) {
            if (titles.isEmpty()) findTitle();
            int i = titles.indexOf(title);
            int next = i + 1;
            StringBuilder buffer = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                boolean bol = false;
                for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                    if (s.trim().equals(titles.get(i))) bol = true;
                    if (next < titles.size())
                        if (s.trim().equals(titles.get(next))) {
                            bol = false;
                            break;
                        }
                    if (bol) buffer.append(s).append('\n');
                }
            } catch (FileNotFoundException e) {
                System.err.println("文件读取异常：\n" + e.getMessage());
            } catch (IOException e) {
                System.err.println("读取文本行内时操作异常：\n" + e.getMessage());
            }
            return buffer.toString();
        } else {
            return cs.stream()
                    .filter(cs -> cs.title.equals(title))
                    .map(cs -> cs.content)
                    .toList().get(0);
        }
    }

    public static class C {
        String title, content;

        public C(String title, String content) {
            this.title = title;
            this.content = content;
        }

        @Override
        public String toString() {
            return "C{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

}
