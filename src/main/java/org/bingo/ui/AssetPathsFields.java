package org.bingo.ui;

import java.util.List;

public class AssetPathsFields {

    public static List<List<FieldSpec<?>>> assetPathsGroups(AssetPathsTabData assetPathsTabData) {
        return List.of(
                List.of(
                        new FieldSpec<>("Theme Name:", assetPathsTabData.getTheme(), FieldType.TEXT)
                ),
                List.of(
                        new FieldSpec<>("Grid Color:", assetPathsTabData.getGridColor(), FieldType.COLOR),
                        new FieldSpec<>("Label Color:", assetPathsTabData.getFontColor(), FieldType.COLOR)
                ),
                List.of(new FieldSpec<>("Image Directory:", assetPathsTabData.getBingoIcons(), FieldType.DIRECTORY),
                        new FieldSpec<>("Header Path:", assetPathsTabData.getHeader(), FieldType.FILE),
                        new FieldSpec<>("Frame Path:", assetPathsTabData.getFrame(), FieldType.FILE),
                        new FieldSpec<>("Free Space Path:", assetPathsTabData.getFreeSpace(), FieldType.FILE),
                        new FieldSpec<>("C.C. Header Path:", assetPathsTabData.getCcHeader(), FieldType.FILE),
                        new FieldSpec<>("Token Path:", assetPathsTabData.getToken(), FieldType.FILE),
                        new FieldSpec<>("Output Path:", assetPathsTabData.getOutput(), FieldType.DIRECTORY)
                ),
                List.of(
                        new FieldSpec<>("Instructions Path:", assetPathsTabData.getInstructions(), FieldType.FILE),
                        new FieldSpec<>("Font Path:", assetPathsTabData.getFont(), FieldType.FILE),
                        new FieldSpec<>("Scissors Icon Path:", assetPathsTabData.getScissors(), FieldType.FILE)
                ),
                List.of(
                        new FieldSpec<>("Generate C.C., Tokens, and Instructions", null, FieldType.BUTTON)
                ),
                List.of(
                        new FieldSpec<>("Restore Defaults", null, FieldType.RESET)
                )
        );
    }
}
