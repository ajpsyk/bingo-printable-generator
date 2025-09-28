package org.bingo.ui;

import javafx.beans.property.*;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class AssetPathsTabData {
    private final StringProperty theme =  new SimpleStringProperty("");
    private final ObjectProperty<Color> gridColor = new SimpleObjectProperty<>(Color.BLACK);
    private final ObjectProperty<Color> fontColor = new SimpleObjectProperty<>(Color.BLACK);
    private final StringProperty bingoIcons = new SimpleStringProperty("");
    private final StringProperty header = new SimpleStringProperty("");
    private final StringProperty frame = new SimpleStringProperty("");
    private final StringProperty freeSpace = new SimpleStringProperty("");
    private final StringProperty ccHeader = new SimpleStringProperty("");
    private final StringProperty token = new SimpleStringProperty("");
    private final StringProperty output = new SimpleStringProperty("");
    private final StringProperty instructions = new SimpleStringProperty("");
    private final StringProperty font = new SimpleStringProperty("");
    private final StringProperty scissors = new SimpleStringProperty("");
    private final BooleanProperty enableLabels = new SimpleBooleanProperty(true);

    public void reset() {
        theme.set("");
        gridColor.set(Color.BLACK);
        fontColor.set(Color.BLACK);
        bingoIcons.set("");
        header.set("");
        frame.set("");
        freeSpace.set("");
        ccHeader.set("");
        token.set("");
        output.set("");
        instructions.set("");
        font.set("");
        scissors.set("");
        enableLabels.set(true);
    }
}
