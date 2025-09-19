package org.bingo.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;

@Getter
public class BingoCardsTabData {
    private final DoubleProperty headerSpacingTop =  new SimpleDoubleProperty();
    private final DoubleProperty headerSpacingRight = new SimpleDoubleProperty();
    private final DoubleProperty headerSpacingBottom = new SimpleDoubleProperty();
    private final DoubleProperty headerSpacingLeft = new SimpleDoubleProperty();
    private final DoubleProperty gridLineThickness = new SimpleDoubleProperty();
    private final DoubleProperty gridSpacingRight= new SimpleDoubleProperty();
    private final DoubleProperty gridSpacingBottom = new SimpleDoubleProperty();
    private final DoubleProperty gridSpacingLeft= new SimpleDoubleProperty();
    private final DoubleProperty labelSize = new SimpleDoubleProperty();
    private final DoubleProperty cellHorizontalSpacing = new SimpleDoubleProperty();
    private final DoubleProperty cellVerticalSpacing= new SimpleDoubleProperty();
    private final DoubleProperty cellGap = new SimpleDoubleProperty();
    private final IntegerProperty copies = new SimpleIntegerProperty();

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
