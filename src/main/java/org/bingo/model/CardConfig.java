package org.bingo.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import com.itextpdf.kernel.colors.DeviceRgb;


@Data
@AllArgsConstructor
public class CardConfig {
    private DeviceRgb gridColor;
    private DeviceRgb labelColor;

    private float gridLineThicknessInches;

    private float headerSpacingTopInches;
    private float headerSpacingRightInches;
    private float headerSpacingBottomInches;
    private float headerSpacingLeftInches;

    private float gridSpacingRightInches;
    private float gridSpacingBottomInches;
    private float gridSpacingLeftInches;

    private float cellSpacingXRatio;
    private float cellSpacingYRatio;

    private int cardAmount;
    private int docAmount;

    private String fileName;

    private int rows;
    private int columns;

    private boolean freeSpaceEnabled;
    private boolean scissorsIconEnabled;
}
