package org.bingo.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CardConfig {
    private String gridColorHex;
    private String labelColorHex;

    private float gridLineThicknessInches;

    private float headerSpacingTopInches;
    private float headerSpacingRightInches;
    private float headerSpacingBottomInches;
    private float headerSpacingLeftInches;

    private float gridMarginRightInches;
    private float gridMarginBottomInches;
    private float gridMarginLeftInches;

    private float cellSpacingTopInches;
    private float cellSpacingRightInches;
    private float gridCellSpacingBottomInches;
    private float gridCellSpacingLeftInches;

    private int cardAmount;
    private int docAmount;

    private String fileName;

    private int rows;
    private int columns;

    private boolean freeSpaceEnabled;
    private boolean scissorsIconEnabled;
}
