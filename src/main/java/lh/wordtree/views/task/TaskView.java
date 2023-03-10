package lh.wordtree.views.task;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lh.wordtree.dao.AuthorTaskMapper;
import lh.wordtree.dao.impl.AuthorTaskMapperImpl;
import lh.wordtree.entity.AuthorTask;
import lh.wordtree.views.core.NoteCoreView;

public class TaskView extends VBox {
    private final AuthorTaskMapper authorTaskMapper = new AuthorTaskMapperImpl();

    public TaskView() {
        var labelListView = new ListView<Box>();
        {
            labelListView.prefHeight(NoteCoreView.HEIGHT);
            // 查询该用户所有创建过的任务
            var authorTasks = authorTaskMapper.selectAll();
            authorTasks.forEach(authorTask -> {
                labelListView.getItems().add(new Box(authorTask));
            });
        }
        this.getChildren().addAll(labelListView);
    }

    private static class Box extends HBox {
        private SimpleBooleanProperty isBool = new SimpleBooleanProperty(true);
        private AuthorTask task;

        public Box(AuthorTask task) {
            this.task = task;
            view();
            controller();
        }

        public void setForbidden(boolean b) {
            isBool.set(b);
        }

        private void view() {
            var label = new Label(task.getAuthorName());
            label.setPrefWidth(140);
            var del = new Label("删除");
            del.setStyle("-fx-text-fill: red;");
            var detail = new Label("详情");
            detail.setStyle("-fx-text-fill: green;");
            var add = new Label("添加");
            add.setStyle("-fx-text-fill: blue;");
            this.getChildren().addAll(label, del, detail, add);
            this.setSpacing(6);
        }

        private void controller() {
            isBool.addListener((observable, oldValue, newValue) -> {

            });
        }

    }
}
