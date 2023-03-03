package lh.wordtree.views.bookrack;

import cn.hutool.core.thread.ThreadUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lh.wordtree.dao.AuthorTaskMapper;
import lh.wordtree.dao.DAO;

public class BookRackRightView extends VBox {
    private AuthorTaskMapper authorTaskMapper = DAO.getSqlSessionFactory().getMapper(AuthorTaskMapper.class);

    public BookRackRightView() {
        var title = new VBox();
        title.getChildren().add(new Label("任务管理"));
        title.setPadding(new Insets(0, 0, 10, 0));
        var labelListView = new ListView<Label>();
        {
            labelListView.prefHeightProperty().bind(this.heightProperty());
            ThreadUtil.execAsync(() -> {
                // 查询该用户所有创建过的任务
                var authorTasks = authorTaskMapper.selectAll();
                Platform.runLater(() -> {
                    authorTasks.forEach(authorTask -> {
                        var label = new Label();
                        label.setText(authorTask.getDescribe());
                        label.setGraphic(new Text(authorTask.getNumber().toString()));
                        labelListView.getItems().add(label);
                    });
                });
            });
        }
        this.getChildren().addAll(title, labelListView);
    }
}
