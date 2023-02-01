package lh.wordtree.service.factory;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.web.WebView;
import lh.wordtree.entity.Author;
import lh.wordtree.entity.NovelProject;

import java.io.File;

public abstract class FactoryBeanService {
    public static SimpleObjectProperty<File> nowFile = new SimpleObjectProperty<>();
    public static SimpleObjectProperty<File> nowRootFile = new SimpleObjectProperty<>();
    public static SimpleStringProperty rowLine = new SimpleStringProperty("0:0");
    public static SimpleObjectProperty nowWorkSpace = new SimpleObjectProperty();
    public static SimpleStringProperty time = new SimpleStringProperty("");
    public static SimpleStringProperty number = new SimpleStringProperty("");
    public static SimpleBooleanProperty isWt = new SimpleBooleanProperty(false);
    public static SimpleObjectProperty<NovelProject> novelProject = new SimpleObjectProperty<>();
    public static SimpleObjectProperty<Author> user = new SimpleObjectProperty<>();

    public static WebView getWebView() {
        return new WebView();
    }
}
