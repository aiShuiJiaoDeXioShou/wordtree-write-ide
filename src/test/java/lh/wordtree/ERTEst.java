package lh.wordtree;

import org.junit.jupiter.api.Test;

public class ERTEst {
    @Test
    public void test4() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < i; j++) {
                System.out.println(i + " * " + j + " = " + i * j);
                if (i == j) System.out.println();
            }
        }
    }
}
