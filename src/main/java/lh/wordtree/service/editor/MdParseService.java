package lh.wordtree.service.editor;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public interface MdParseService {
    // 生成器
    HtmlRenderer renderer = HtmlRenderer.builder().build();
    Parser parser = Parser.builder().build();

    // .md 文件解析器
    static String mdParse(String source) {
        // 解析器
        return renderer.render(parser.parse(source));
    }

}
