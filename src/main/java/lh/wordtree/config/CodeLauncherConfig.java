package lh.wordtree.config;

import java.util.List;
import java.util.Map;

public interface CodeLauncherConfig {
    // 初始化语言配置文件
    List<Map<String, String>> LANGUAGE_CODE_DATA = List.of(
            Map.of("java", """
                        {
                          "fileName": "java",
                          "code": [
                            "abstract","assert","boolean","break","byte","case","catch","char",
                            "class","const","continue","default","do","double","else","enum","extends",
                            "final","finally","float","for","goto","if","implements","import","instanceof",
                            "int","interface","long","native","new","package","private","protected",
                            "public","return","short","static","strictfp","super","switch","synchronized",
                            "this","throw","throws","transient","try","void","volatile","while"
                          ]
                        }
                    """),
            Map.of("go", """
                    {
                      "fileName": "go",
                      "code": [
                        "import",
                        "package",
                        "chan",
                        "const",
                        "func",
                        "interface",
                        "map",
                        "struct",
                        "type",
                        "var",
                        "break",
                        "case",
                        "continue",
                        "default",
                        "defer",
                        "else",
                        "fallthrough",
                        "for",
                        "go",
                        "goto",
                        "if",
                        "range",
                        "return",
                        "select",
                        "switch"
                      ]
                    }
                    """),
            Map.of("cpp", """
                    {
                      "fileName": "cpp",
                      "code": ["if","return"],
                    }
                    """),
            Map.of("c", """
                    {
                      "fileName": "c",
                      "code": ["if","return"],
                    }
                    """),
            Map.of("cs", """
                    {
                      "fileName": "c",
                      "code": ["if","return"],
                    }
                    """),
            Map.of("js", """
                    {
                      "fileName": "c",
                      "code": ["if","return"],
                    }
                    """),
            Map.of("rs", """
                    {
                      "fileName": "c",
                      "code": ["if","return"],
                    }
                    """)
    );
}
