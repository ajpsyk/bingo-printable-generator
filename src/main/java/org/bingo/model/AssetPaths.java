package org.bingo.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.nio.file.Path;

/**
 * Holds file paths required for generating bingo PDFs.
 */
@Data
@Builder(toBuilder = true)
public class AssetPaths {
    @NonNull Path framePath;
    @NonNull Path headerPath;
    @NonNull Path iconsPath;
    @NonNull Path fontPath;
    @NonNull Path freeSpacePath;
    @NonNull Path instructionsPath;
    @NonNull Path scissorsIconPath;
    @NonNull Path tokenPath;
    @NonNull Path callingCardsHeaderPath;
}
