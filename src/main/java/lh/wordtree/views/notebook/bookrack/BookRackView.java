package lh.wordtree.views.notebook.bookrack;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class BookRackView extends BorderPane {

    public final static BookRackView INSTANCE = new BookRackView();;

    public BookRackView() {
        this.myLayout();
    }

    public void update() {
        this.myLayout();
    }

    private void myLayout() {
        this.setPrefSize(700, 600);
        this.setPadding(new Insets(0, 0, 10, 0));

        var centerBox = new VBox();
        centerBox.getChildren().addAll();

        var rightBox = new BookRackRightView();
        this.setRight(rightBox);
    }
}
