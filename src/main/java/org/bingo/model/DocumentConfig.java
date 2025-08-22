package org.bingo.model;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.nio.file.Path;

@Data
@AllArgsConstructor
public class DocumentConfig {
    private String themeName;

    private Path icons;
    private Path header;
    private Path frame;
    private Path freeSpace;
    private Path callingCardsHeader;
    private Path token;
    private Path instructions;
    private Path font;
    private Path scissorsIcon;
    private Path output;

    private float marginTopInches;
    private float marginRightInches;
    private float marginBottomInches;
    private float marginLeftInches;
}
