package lh.wordtree.uitest;

import cn.hutool.core.io.FileUtil;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lh.wordtree.component.editor.WTWriterEditor;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;
import org.reactfx.value.Var;

import java.io.File;
import java.util.Optional;

import static org.reactfx.EventStreams.nonNullValuesOf;

public class PopupDemo extends Application {

    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    private VBox createPopupOptions(BoundsPopup p, String showHideButtonText, String toggleViewportText) {
        Button showOrHidePopup = new Button(showHideButtonText);
        showOrHidePopup.setOnAction(ae -> p.invertVisibility());
        Button toggleOutOfViewportOption = new Button(toggleViewportText);
        toggleOutOfViewportOption.setOnAction(ae -> p.invertViewportOption());
        return new VBox(showOrHidePopup, toggleOutOfViewportOption);
    }

    private Subscription feedVisibilityToLabelText(EventStream<Optional<Bounds>> boundsStream, BoundsPopup popup, String item) {
        return boundsStream
                .map(o -> o.isPresent() ? " is " : " is not ")
                .subscribe(visibilityStatus -> popup.setText(item + visibilityStatus + "within the viewport"));
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        var file = new File("D:\\4.goCode\\TheTimer\\main.go");
        var area = new WTWriterEditor(file);
        area.appendText(FileUtil.readUtf8String(file));
        area.setStyle("-fx-font-size: 15;");
        area.setWrapText(true);
        BoundsPopup caretPopup = new BoundsPopup("I am the caret popup button!");

        primaryStage.setScene(new Scene(area, 1000, 600));
        primaryStage.setTitle("Popup Demo");
        primaryStage.show();

        EventStream<Optional<Bounds>> caretBounds = nonNullValuesOf(area.caretBoundsProperty());
        Subscription cBoundsSub = feedVisibilityToLabelText(caretBounds, caretPopup, "Caret");

        Subscription caretPopupSub = EventStreams.combine(caretBounds, caretPopup.outsideViewportValues())
                .subscribe(tuple3 -> {
                    Optional<Bounds> opt = tuple3._1;
                    boolean showPopupWhenCaretOutside = tuple3._2;

                    if (opt.isPresent()) {
                        Bounds b = opt.get();
                        caretPopup.setX(b.getMaxX());
                        caretPopup.setY(b.getMaxY());

                        if (caretPopup.isHiddenTemporarily()) {
                            caretPopup.show(stage);
                            caretPopup.setHideTemporarily(false);
                        }

                    } else {
                        if (!showPopupWhenCaretOutside) {
                            caretPopup.hide();
                            caretPopup.setHideTemporarily(true);
                        }
                    }
                });



        Subscription caretSubs = caretPopupSub.and(cBoundsSub);

        caretPopup.show(primaryStage);
        area.moveTo(0);
        area.requestFollowCaret();
    }

    /**
     * Helper class: Popup that can track when it should temporarily hide itself if its object is outside of the
     * viewport and provides convenience to adding content to both caret/selection popup
     */
    private class BoundsPopup extends Popup {

        /**
         * Indicates whether popup should still be shown even when its item (caret/selection) is outside viewport
         */
        private final Var<Boolean> showWhenItemOutsideViewport = Var.newSimpleVar(true);
        /**
         * Indicates whether popup has been hidden since its item (caret/selection) is outside viewport
         * and should be shown when that item becomes visible again
         */
        private final Var<Boolean> hideTemporarily = Var.newSimpleVar(false);
        private final VBox vbox;
        private final Button button;
        private final Label label;

        BoundsPopup(String buttonText) {
            super();
            button = new Button(buttonText);
            label = new Label();
            vbox = new VBox(button, label);
            vbox.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, null, null)));
            vbox.setPadding(new Insets(5));
            getContent().add(vbox);
        }

        public final EventStream<Boolean> outsideViewportValues() {
            return showWhenItemOutsideViewport.values();
        }

        public final void invertViewportOption() {
            showWhenItemOutsideViewport.setValue(!showWhenItemOutsideViewport.getValue());
        }

        public final boolean isHiddenTemporarily() {
            return hideTemporarily.getValue();
        }

        public final void setHideTemporarily(boolean value) {
            hideTemporarily.setValue(value);
        }

        public final void invertVisibility() {
            if (isShowing()) {
                hide();
            } else {
                show(stage);
            }
        }

        public final void setText(String text) {
            label.setText(text);
        }
    }
}
