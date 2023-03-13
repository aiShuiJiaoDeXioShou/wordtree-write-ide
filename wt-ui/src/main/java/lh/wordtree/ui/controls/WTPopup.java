package lh.wordtree.ui.controls;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.stage.Popup;

import java.awt.*;

public class WTPopup extends Popup {

    public WTPopup(Region targetNode, Node srcNode) {
        this.getContent().add(srcNode);
        targetNode.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Point p = MouseInfo.getPointerInfo().getLocation();
                this.show(targetNode, p.getX(), p.getY() - (60 + targetNode.getPrefHeight()));
            } else {
                this.hide();
            }
        });
    }

}
