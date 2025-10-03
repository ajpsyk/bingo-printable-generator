package org.printable.config;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;

/**
 * Holds file paths required for generating bingo PDFs.
 */
@Data
@Builder(toBuilder = true)
public class AssetPaths {
    @Builder.Default Path framePath = null;
    @Builder.Default Path headerPath = null;
    @Builder.Default Path iconsPath = null;
    @Builder.Default Path fontPath = null;
    @Builder.Default Path freeSpacePath = null;
    @Builder.Default Path instructionsPath = null;
    @Builder.Default Path scissorsIconPath = null;
    @Builder.Default Path tokenPath = null;
    @Builder.Default Path callingCardsHeaderPath = null;
}
