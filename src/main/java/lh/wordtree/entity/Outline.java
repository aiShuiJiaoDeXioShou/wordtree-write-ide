package lh.wordtree.entity;

import java.util.ArrayList;
import java.util.List;

public class Outline {
    private String title;
    private String description;
    private String content;
    private List<String> figures;

    public Outline() {
    }

    public Outline(String title, String description, String content, ArrayList<String> figures) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.figures = figures;
    }

    public List<String> getFigures() {
        return figures;
    }

    public void setFigures(List<String> figures) {
        this.figures = figures;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
