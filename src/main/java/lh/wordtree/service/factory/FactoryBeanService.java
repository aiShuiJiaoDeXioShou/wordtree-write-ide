package lh.wordtree.service.factory;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.web.WebView;
import lh.wordtree.comm.entity.Figure;
import lh.wordtree.comm.entity.NovelProject;
import lh.wordtree.entity.Author;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.util.List;

public abstract class FactoryBeanService {
    public static SimpleObjectProperty<File> nowFile = new SimpleObjectProperty<>();
    public static SimpleObjectProperty<File> nowRootFile = new SimpleObjectProperty<>();
    public static SimpleStringProperty rowLine = new SimpleStringProperty("0:0");
    public static SimpleObjectProperty nowWorkSpace = new SimpleObjectProperty();
    public static SimpleStringProperty time = new SimpleStringProperty("");
    public static SimpleStringProperty number = new SimpleStringProperty("");
    public static SimpleBooleanProperty isWt = new SimpleBooleanProperty(false);
    public static SimpleObjectProperty<CodeArea> nowCodeArea = new SimpleObjectProperty<>();
    public static SimpleObjectProperty<NovelProject> novelProject = new SimpleObjectProperty<>();
    public static SimpleObjectProperty<Author> user = new SimpleObjectProperty<>();
    public static SimpleObjectProperty<List<Figure>> roles = new SimpleObjectProperty<>();
    public static SimpleDoubleProperty heigth = new SimpleDoubleProperty();

    public static WebView getWebView() {
        return new WebView();
    }
}
