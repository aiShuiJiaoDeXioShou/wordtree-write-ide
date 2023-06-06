package lh.wordtree.comm.utils;


import java.io.InputStream;
import java.util.Objects;

public interface ClassLoaderUtils {

    static InputStream load(String path) {
        return ClassLoaderUtils.class.getClassLoader().getResourceAsStream(path);
    }

    static String url(String path) {
        return Objects.requireNonNull(
                ClassLoaderUtils.class.getClassLoader().getResource(path)
        ).toExternalForm();
    }

}
