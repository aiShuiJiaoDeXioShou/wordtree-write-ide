package lh.wordtree.service.file;

import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.List;

public interface MenuFileService {
    List<File> getFiles();

    TreeItem<Label> getTree();

    List<TreeItem<Label>> getDirList();

    List<TreeItem<Label>> getFileList();
}
