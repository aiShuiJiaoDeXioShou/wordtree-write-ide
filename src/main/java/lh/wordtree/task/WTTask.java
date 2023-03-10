package lh.wordtree.task;

public interface WTTask {

    default void apply() {
    }


    default void init() {
    }

    default void end() {
    }

    default void write(String source) {
    }

    default void toggleFile() {
    }

    default void loop() {
    }

    default void save() {
    }
}
