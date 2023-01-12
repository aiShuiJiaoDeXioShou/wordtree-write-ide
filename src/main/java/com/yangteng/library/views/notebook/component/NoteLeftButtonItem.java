package com.yangteng.library.views.notebook.component;

import javafx.scene.control.Tooltip;
import jfxtras.styles.jmetro.MDL2IconFont;

public class NoteLeftButtonItem extends MDL2IconFont {
    public NoteLeftButtonItem(String icon, String tooltipText) {
        super(icon);
        this.setId(tooltipText);
        var tooltip = new Tooltip(tooltipText);
        Tooltip.install(this, tooltip);
    }
}
