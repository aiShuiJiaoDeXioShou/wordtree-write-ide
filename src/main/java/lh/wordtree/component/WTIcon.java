package lh.wordtree.component;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lh.wordtree.utils.ClassLoaderUtils;
import lh.wordtree.utils.SvgUtils;

public class WTIcon extends ImageView {
    public WTIcon(String path) {
        var image = SvgUtils.imageFromSvg(ClassLoaderUtils.load(path));
        this.setImage(image);
        this.setFitWidth(WtIconSize.MIN.value);
        this.setFitHeight(WtIconSize.MIN.value);
    }

    public WTIcon(Image image) {
        this.setImage(image);
        this.setFitWidth(WtIconSize.MIN.value);
        this.setFitHeight(WtIconSize.MIN.value);
    }

    public WTIcon(String path, WtIconSize wtIconSize) {
        var image = SvgUtils.imageFromSvg(ClassLoaderUtils.load(path));
        this.setImage(image);
        this.setFitWidth(wtIconSize.value);
        this.setFitHeight(wtIconSize.value);
    }

    public WTIcon(String path, double w, double h) {
        var image = SvgUtils.imageFromSvg(ClassLoaderUtils.load(path));
        this.setImage(image);
        this.setFitWidth(w);
        this.setFitHeight(h);
    }

    public enum WtIconSize {
        MIN(15), MAX(35), AUTO(20);
        public Integer value = 15;

        WtIconSize(Integer value) {
            this.value = value;
        }
    }
}
