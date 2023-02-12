package lh.wordtree.utils;

import lh.wordtree.App;
import lh.wordtree.Launcher;

import java.io.InputStream;
import java.util.Objects;

public interface ClassLoaderUtils {

    static InputStream load(String path) {
        return Launcher.class.getClassLoader().getResourceAsStream(path);
    }

    static String url(String path) {
        return Objects.requireNonNull(App.class.getClassLoader().getResource(path)).toExternalForm();
    }

}
