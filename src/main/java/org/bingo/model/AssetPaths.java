package org.bingo.model;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.nio.file.Path;

@Data
@AllArgsConstructor
public class AssetPaths {
    private Path ICONS_PATH;
    private Path HEADER_PATH;
    private Path FRAME_PATH;
    private Path FREE_SPACE_PATH;
    private Path CALLING_CARDS_HEADER_PATH;
    private Path TOKEN_PATH;
    private Path INSTRUCTIONS_PATH;
    private Path FONT_PATH;
    private Path SCISSORS_PATH;
    private Path OUTPUT_PATH;
}
