package com.yangteng.library.component;

import javafx.scene.control.Tooltip;
import jfxtras.styles.jmetro.MDL2IconFont;

public class WTNoteLeftButtonItem extends MDL2IconFont {
    public WTNoteLeftButtonItem(String icon, String tooltipText) {
        super(icon);
        this.setId(tooltipText);
        var tooltip = new Tooltip(tooltipText);
        Tooltip.install(this, tooltip);
    }
}
