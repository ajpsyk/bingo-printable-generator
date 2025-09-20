package org.bingo.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;

@Getter
public class BingoCardsTabData {
    private final DoubleProperty headerSpacingTop =  new SimpleDoubleProperty(0.3f);
    private final DoubleProperty headerSpacingRight = new SimpleDoubleProperty(0.3f);
    private final DoubleProperty headerSpacingBottom = new SimpleDoubleProperty(0.3f);
    private final DoubleProperty headerSpacingLeft = new SimpleDoubleProperty(0.3f);
    private final DoubleProperty gridLineThickness = new SimpleDoubleProperty(0.014f);
    private final DoubleProperty gridSpacingRight= new SimpleDoubleProperty(0.3f);
    private final DoubleProperty gridSpacingBottom = new SimpleDoubleProperty(0.3f);
    private final DoubleProperty gridSpacingLeft= new SimpleDoubleProperty(0.3f);
    private final DoubleProperty labelSize = new SimpleDoubleProperty(0.125f);
    private final DoubleProperty cellHorizontalSpacing = new SimpleDoubleProperty(0.05f);
    private final DoubleProperty cellVerticalSpacing= new SimpleDoubleProperty(0.05f);
    private final DoubleProperty cellGap = new SimpleDoubleProperty(0.05f);
    private final IntegerProperty copies = new SimpleIntegerProperty(100);

    public void reset() {
        headerSpacingTop.set(0.3f);
        headerSpacingRight.set(0.3f);
        headerSpacingBottom.set(0.3f);
        headerSpacingLeft.set(0.3f);
        gridLineThickness.set(0.014f);
        gridSpacingRight.set(0.3f);
        gridSpacingBottom.set(0.3f);
        gridSpacingLeft.set(0.3f);
        labelSize.set(0.125f);
        cellHorizontalSpacing.set(0.05f);
        cellVerticalSpacing.set(0.05f);
        cellGap.set(0.05f);
        copies.set(100);
    }
}
