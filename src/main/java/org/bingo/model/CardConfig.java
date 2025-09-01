package org.bingo.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import com.itextpdf.kernel.colors.DeviceRgb;

import java.nio.file.Path;


@Data
@AllArgsConstructor
public class CardConfig {
    private DeviceRgb gridColor;
    private DeviceRgb labelColor;

    private float gridLineThicknessInches;
    private float additionalMarginTop;
    private float additionalMarginBottom;

    private float headerSpacingTopInches;
    private float headerSpacingRightInches;
    private float headerSpacingBottomInches;
    private float headerSpacingLeftInches;

    private float gridSpacingRightInches;
    private float gridSpacingBottomInches;
    private float gridSpacingLeftInches;

    private float labelHeightRatio;
    private float cellSpacingXRatio;
    private float cellSpacingYRatio;
    private float cellGapRatio;

    private int cardAmount;
    private int docAmount;

    private Path fileName;

    private int rows;
    private int columns;

    private boolean freeSpaceEnabled;
    private boolean scissorsIconEnabled;
}
