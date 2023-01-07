package com.yangteng.library.views.notebook.main.bookrack;

import javafx.scene.layout.BorderPane;

public class BookRackView extends BorderPane {

    public final static BookRackView INSTANCE = new BookRackView();

    public BookRackView() {
        this.setPrefSize(700, 600);
    }

}
