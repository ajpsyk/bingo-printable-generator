package org.bingo.model;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;

import java.nio.file.Path;
import java.util.Random;

import lombok.Data;
import lombok.Builder;
import lombok.NonNull;

/**
 * Holds document level configurations.
 */
@Data
@Builder(toBuilder = true)
public class DocumentConfig {
    @NonNull AssetPaths assets;
    @NonNull Path outputPath;

    @Builder.Default DeviceRgb fontColor = new DeviceRgb(0,0,0);
    @Builder.Default PageSize pageSize = PageSize.LETTER;
    @Builder.Default Random seed = new Random(1);

    @Builder.Default float marginTopInches = 0f;
    @Builder.Default float marginRightInches = 0f;
    @Builder.Default float marginBottomInches = 0f;
    @Builder.Default float marginLeftInches = 0f;
}
