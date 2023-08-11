package lh.wordtree.test2;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

public class Test2 {

    @Test
    public void test() {
        record R(int value, String name){}
        List<R> rs = List.of(new R(1, "1"), new R(2, "2"), new R(4, "4")
                , new R(9, "9"), new R(0, "0"), new R(3, "3")
        );
        rs.stream()
                .sorted((o1, o2) -> o2.value - o1.value)
                .forEach(System.out::println);
    }

}
