package lh.wordtree.service.language;

import javafx.scene.Node;
import lh.wordtree.App;
import lh.wordtree.handler.EditorLanguageFactory;

import java.io.File;
import java.util.Objects;

public class LanguageConstructorServiceImpl implements LanguageConstructorService {
    private final File file;

    public LanguageConstructorServiceImpl(File file) {
        this.file = file;
    }

    public Node build() {
        return EditorLanguageFactory.newInstance().handler(file);
    }

    private String url(String path) {
        return Objects.requireNonNull(App.class.getClassLoader().getResource(path)).toExternalForm();
    }

}
