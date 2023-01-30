package lh.wordtree.entity;

public record Record(Integer id, String context, String title) {

    public String toString() {
        return title;
    }
}