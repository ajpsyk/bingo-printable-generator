package org.bingo.ui;

import java.util.List;

public class BingoCardsFields {

    public static List<List<FieldSpec<?>>> bingoCardsGroups(BingoCardsTabData bingoCardsTabData) {
        return List.of(
                List.of(
                        new FieldSpec<>("1 Per Page", null, FieldType.HEADER)
                ),
                List.of(
                        new FieldSpec<>("Header Spacing Top:", bingoCardsTabData.getHeaderSpacingTop(), FieldType.DOUBLE),
                        new FieldSpec<>("Header Spacing Right:", bingoCardsTabData.getHeaderSpacingRight(), FieldType.DOUBLE),
                        new FieldSpec<>("Header Spacing Bottom:", bingoCardsTabData.getHeaderSpacingBottom(), FieldType.DOUBLE),
                        new FieldSpec<>("Header Spacing Left:", bingoCardsTabData.getHeaderSpacingLeft(), FieldType.DOUBLE)
                ),
                List.of(
                        new FieldSpec<>("Grid Line Thickness:", bingoCardsTabData.getGridLineThickness(), FieldType.DOUBLE),
                        new FieldSpec<>("Grid Spacing Right:", bingoCardsTabData.getGridSpacingRight(), FieldType.DOUBLE),
                        new FieldSpec<>("Grid Spacing Bottom:", bingoCardsTabData.getGridSpacingBottom(), FieldType.DOUBLE),
                        new FieldSpec<>("Grid Spacing Left:", bingoCardsTabData.getGridSpacingLeft(), FieldType.DOUBLE)
                ),
                List.of(
                        new FieldSpec<>("Label Size:", bingoCardsTabData.getLabelSize(), FieldType.DOUBLE),
                        new FieldSpec<>("Cell Padding:", bingoCardsTabData.getCellHorizontalSpacing(), FieldType.DOUBLE),
                        new FieldSpec<>("Label Spacing Bottom:", bingoCardsTabData.getCellVerticalSpacing(), FieldType.DOUBLE),
                        new FieldSpec<>("Cell Gap:", bingoCardsTabData.getCellGap(), FieldType.DOUBLE)
                ),
                List.of(
                        new FieldSpec<>("Number of copies:", bingoCardsTabData.getCopies(), FieldType.INTEGER)
                ),
                List.of(
                        new FieldSpec<>("Generate 1 Per Page", null, FieldType.BUTTON)
                )
        );
    }

    public static List<List<FieldSpec<?>>> landscapeBingoCardsGroups(BingoCardsTabData landscapeBingoCardsTabData) {
        return List.of(
                List.of(
                        new FieldSpec<>("2 Per Page", null, FieldType.HEADER)
                ),
                List.of(
                        new FieldSpec<>("Header Spacing Top:", landscapeBingoCardsTabData.getHeaderSpacingTop(), FieldType.DOUBLE),
                        new FieldSpec<>("Header Spacing Right:", landscapeBingoCardsTabData.getHeaderSpacingRight(), FieldType.DOUBLE),
                        new FieldSpec<>("Header Spacing Bottom:", landscapeBingoCardsTabData.getHeaderSpacingBottom(), FieldType.DOUBLE),
                        new FieldSpec<>("Header Spacing Left:", landscapeBingoCardsTabData.getHeaderSpacingLeft(), FieldType.DOUBLE)
                ),
                List.of(
                        new FieldSpec<>("Grid Line Thickness:", landscapeBingoCardsTabData.getGridLineThickness(), FieldType.DOUBLE),
                        new FieldSpec<>("Grid Spacing Right:", landscapeBingoCardsTabData.getGridSpacingRight(), FieldType.DOUBLE),
                        new FieldSpec<>("Grid Spacing Bottom:", landscapeBingoCardsTabData.getGridSpacingBottom(), FieldType.DOUBLE),
                        new FieldSpec<>("Grid Spacing Left:", landscapeBingoCardsTabData.getGridSpacingLeft(), FieldType.DOUBLE)
                ),
                List.of(
                        new FieldSpec<>("Label Size:", landscapeBingoCardsTabData.getLabelSize(), FieldType.DOUBLE),
                        new FieldSpec<>("Cell Padding:", landscapeBingoCardsTabData.getCellHorizontalSpacing(), FieldType.DOUBLE),
                        new FieldSpec<>("Label Spacing Bottom:", landscapeBingoCardsTabData.getCellVerticalSpacing(), FieldType.DOUBLE),
                        new FieldSpec<>("Cell Gap:", landscapeBingoCardsTabData.getCellGap(), FieldType.DOUBLE)
                ),
                List.of(
                        new FieldSpec<>("Number of copies:", landscapeBingoCardsTabData.getCopies(), FieldType.INTEGER)
                ),
                List.of(
                        new FieldSpec<>("Generate 2 Per Page", null, FieldType.BUTTON)
                )
        );
    }
}
