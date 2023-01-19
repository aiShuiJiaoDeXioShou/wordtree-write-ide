package lh.wordtree.utils;

import lh.wordtree.Launcher;

import java.io.InputStream;

public interface ClassLoaderUtils {

    static InputStream load(String path) {
        return Launcher.class.getClassLoader().getResourceAsStream(path);
    }

}
