package lh.wordtree.test;

import javafx.application.Application;
import lh.wordtree.uitest.JavaCoder;
import lh.wordtree.uitest.TooltipDemo;
import org.junit.jupiter.api.Test;

public class Test2 {
    public static void main(String[] args) {
        Application.launch(TooltipDemo.class);
    }

    @Test
    public void test5() {
        Application.launch(JavaCoder.class);
    }
}
