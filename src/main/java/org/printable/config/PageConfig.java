package org.printable.config;

import lombok.Builder;
import lombok.Data;
import com.itextpdf.kernel.colors.DeviceRgb;

/**
 * Holds page level configurations.
 */
@Data
@Builder(toBuilder = true)
public class PageConfig {
    @Builder.Default float headerSpacingTopInches = 0f;
    @Builder.Default float headerSpacingRightInches = 0f;
    @Builder.Default float headerSpacingBottomInches = 0f;
    @Builder.Default float headerSpacingLeftInches = 0f;

    @Builder.Default DeviceRgb gridLineColor = new DeviceRgb(0,0,0);
    @Builder.Default int gridRowAmount = 0;
    @Builder.Default int gridColumnAmount = 0;
    @Builder.Default float gridLineThicknessInches = 0f;
    @Builder.Default float gridSpacingRightInches = 0f;
    @Builder.Default float gridSpacingBottomInches = 0f;
    @Builder.Default float gridSpacingLeftInches = 0f;

    @Builder.Default float labelHeightRatio = 0f;
    @Builder.Default float cellSpacingXRatio = 0f;
    @Builder.Default float cellSpacingYRatio = 0f;
    @Builder.Default float cellGapRatio = 0f;

    @Builder.Default int copies = 0;
}
