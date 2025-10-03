package org.bingo.ui;

import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.Getter;

@Getter
public class EmojiGameTabData {
    private final StringProperty theme =  new SimpleStringProperty("");
    private final StringProperty emojiGame = new SimpleStringProperty("");
    private final StringProperty answerKey = new SimpleStringProperty("");
    private final StringProperty frame = new SimpleStringProperty("");
    private final StringProperty output = new SimpleStringProperty("");
    private final DoubleProperty spacingTop = new SimpleDoubleProperty(0.0f);
    private final DoubleProperty spacingBottom = new SimpleDoubleProperty(0.0f);
    private final DoubleProperty spacingLeft = new SimpleDoubleProperty(0.0f);
    private final DoubleProperty spacingRight = new SimpleDoubleProperty(0.0f);
    private final DoubleProperty twoPPSpacingTop = new SimpleDoubleProperty(0.0f);
    private final DoubleProperty twoPPSpacingBottom = new SimpleDoubleProperty(0.0f);
    private final DoubleProperty twoPPSpacingLeft = new SimpleDoubleProperty(0.0f);
    private final DoubleProperty twoPPSpacingRight = new SimpleDoubleProperty(0.0f);

    public void reset() {
        theme.set("");
        frame.set("");
        emojiGame.set("");
        answerKey.set("");
        output.set("");
        spacingTop.set(0.0f);
        spacingBottom.set(0.0f);
        spacingLeft.set(0.0f);
        spacingRight.set(0.0f);
        twoPPSpacingTop.set(0.0f);
        twoPPSpacingBottom.set(0.0f);
        twoPPSpacingLeft.set(0.0f);
        twoPPSpacingRight.set(0.0f);
    }
}
