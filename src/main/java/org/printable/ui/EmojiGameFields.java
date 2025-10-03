package org.printable.ui;

import java.util.List;

public class EmojiGameFields {
    public static List<List<FieldSpec<?>>> emojiGameGroups(EmojiGameTabData emojiGameTabData) {
        return List.of(
                List.of(
                        new FieldSpec<>("Theme Name:", emojiGameTabData.getTheme(), FieldType.TEXT)
                ),
                List.of(
                        new FieldSpec<>("Frame Path:", emojiGameTabData.getFrame(), FieldType.FILE),
                        new FieldSpec<>("Game Path:", emojiGameTabData.getEmojiGame(), FieldType.FILE),
                        new FieldSpec<>("Answer Key Path:", emojiGameTabData.getAnswerKey(), FieldType.FILE),
                        new FieldSpec<>("Output Path:", emojiGameTabData.getOutput(), FieldType.DIRECTORY)
                ),
                List.of(
                        new FieldSpec<>("Spacing Top:", emojiGameTabData.getSpacingTop(), FieldType.DOUBLE),
                        new FieldSpec<>("Spacing Bottom:", emojiGameTabData.getSpacingBottom(), FieldType.DOUBLE),
                        new FieldSpec<>("Spacing Left:", emojiGameTabData.getSpacingLeft(), FieldType.DOUBLE),
                        new FieldSpec<>("Spacing Right:", emojiGameTabData.getSpacingRight(), FieldType.DOUBLE)
                ),
                List.of(
                        new FieldSpec<>("2PP Spacing Top:", emojiGameTabData.getTwoPPSpacingTop(), FieldType.DOUBLE),
                        new FieldSpec<>("2PP Spacing Bottom:", emojiGameTabData.getTwoPPSpacingBottom(), FieldType.DOUBLE),
                        new FieldSpec<>("2PP Spacing Left:", emojiGameTabData.getTwoPPSpacingLeft(), FieldType.DOUBLE),
                        new FieldSpec<>("2PP Spacing Right:", emojiGameTabData.getTwoPPSpacingRight(), FieldType.DOUBLE)
                ),
                List.of(
                        new FieldSpec<>("Generate Emoji Game", null, FieldType.BUTTON)
                )
        );
    }
}
