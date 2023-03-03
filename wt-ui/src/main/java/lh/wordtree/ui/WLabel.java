package lh.wordtree.ui;

import javafx.scene.control.Label;

public class WLabel extends Label {
    public WLabel(String text, WTLabelState wtLabelState) {
        super(text);
        this.getStyleClass().add("wt-label");

        switch (wtLabelState) {
            case PRIMARY -> this.getStyleClass().add("wt-label-primary");
            case ERROR -> this.getStyleClass().add("wt-label-error");
            case WARNING -> this.getStyleClass().add("wt-label-warning");
            case SUCCESS -> this.getStyleClass().add("wt-label-success");
            case INFO -> this.getStyleClass().add("wt-label-info");
            case BLACK -> this.getStyleClass().add("wt-label-black");
            case WHITE -> this.getStyleClass().add("wt-label-white");
            default -> this.getStyleClass().add("wt-label-white");
        }
    }

    public WLabel(String text) {
        super(text);
        this.getStyleClass().add("wt-label");
        this.getStyleClass().add("wt-label-white");
    }

    public enum WTLabelState {
        PRIMARY, ERROR, WARNING, SUCCESS, INFO, BLACK, WHITE
    }

}
