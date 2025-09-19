package org.bingo.ui;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.bingo.config.AssetPaths;
import org.bingo.config.DocumentConfig;
import org.bingo.config.PageConfig;
import org.bingo.services.DocumentBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            String THEME_NAME = "Baby Shower";
            Path output = Paths.get("output");
            Path resources = Paths.get("resources");
            Path ONE_PER_PAGE_OUTPUT_FILE = output.resolve(THEME_NAME + "_1PerPage.pdf");
            Path TWO_PER_PAGE_OUTPUT_FILE = output.resolve(THEME_NAME + "_2PerPage.pdf");
            Path CALLING_CARDS_TOKENS_RULES_OUTPUT_FILE = output.resolve(
                    THEME_NAME + "_Calling Cards_Tokens_Rules.pdf"
            );

            AssetPaths onePerPageBingoPaths = AssetPaths.builder()
                    .framePath(resources.resolve("frame.png"))
                    .headerPath(resources.resolve("header.png"))
                    .iconsPath(resources.resolve("icons"))
                    .fontPath(resources.resolve("Dekko-Regular.ttf"))
                    .freeSpacePath(resources.resolve("Free Space.png"))
                    .instructionsPath(resources.resolve("How To Play.pdf"))
                    .scissorsIconPath(resources.resolve("Scissors.png"))
                    .tokenPath(resources.resolve("Token.png"))
                    .callingCardsHeaderPath(resources.resolve("calling_card_header.png"))
                    .build();

            DocumentConfig onePerPageBingoCards = DocumentConfig.builder()
                    .assets(onePerPageBingoPaths)
                    .outputPath(ONE_PER_PAGE_OUTPUT_FILE)
                    .fontColor(new DeviceRgb(68, 68, 68))
                    .marginTopInches(0.25f)
                    .marginBottomInches(0.25f)
                    .marginLeftInches(0.25f)
                    .marginRightInches(0.25f)
                    .build();

            DocumentConfig twoPerPageBingoCards = onePerPageBingoCards.toBuilder()
                    .outputPath(TWO_PER_PAGE_OUTPUT_FILE)
                    .pageSize(PageSize.LETTER.rotate())
                    .marginTopInches(0.625f)
                    .marginBottomInches(0.625f)
                    .seed(new Random(1))
                    .build();

            DocumentConfig instructionsTokensCallingCards = onePerPageBingoCards.toBuilder()
                    .outputPath(CALLING_CARDS_TOKENS_RULES_OUTPUT_FILE)
                    .build();

            PageConfig portraitBingo = PageConfig.builder()
                    .headerSpacingTopInches(0.25f)
                    .headerSpacingRightInches(0.35f)
                    .headerSpacingBottomInches(0.05f)
                    .headerSpacingLeftInches(0.35f)
                    .gridLineColor(new DeviceRgb(71, 69, 67))
                    .gridRowAmount(5)
                    .gridColumnAmount(5)
                    .gridLineThicknessInches(0.014f)
                    .gridSpacingRightInches(0.35f)
                    .gridSpacingLeftInches(0.35f)
                    .gridSpacingBottomInches(0.375f)
                    .labelHeightRatio(0.125f)
                    .cellSpacingXRatio(0.05f)
                    .cellSpacingYRatio(0.05f)
                    .cellGapRatio(0.05f)
                    .copies(100)
                    .build();

            PageConfig landscapeBingo = portraitBingo.toBuilder()
                    .labelHeightRatio(0.11f)
                    .headerSpacingTopInches(0.2f)
                    .headerSpacingLeftInches(0.3f)
                    .headerSpacingRightInches(0.3f)
                    .headerSpacingBottomInches(0.025f)
                    .gridSpacingRightInches(0.225f)
                    .gridSpacingLeftInches(0.225f)
                    .gridSpacingBottomInches(0.27f)
                    .gridLineThicknessInches(0.007f)
                    .cellSpacingXRatio(0.10f)
                    .build();

            PageConfig tokens = portraitBingo.toBuilder()
                    .gridLineColor(new DeviceRgb(115, 115, 115))
                    .gridRowAmount(9)
                    .gridColumnAmount(9)
                    .copies(1)
                    .headerSpacingTopInches(0f)
                    .headerSpacingLeftInches(0f)
                    .headerSpacingBottomInches(0.2f)
                    .gridSpacingBottomInches(0f)
                    .gridSpacingRightInches(0f)
                    .gridSpacingLeftInches(0f)
                    .headerSpacingRightInches(6.8f)
                    .labelHeightRatio(0)
                    .cellSpacingXRatio(0.15f)
                    .build();

            PageConfig callingCardsSinglePage = portraitBingo.toBuilder()
                    .gridRowAmount(6)
                    .gridColumnAmount(5)
                    .copies(1)
                    .gridLineThicknessInches(0.014f)
                    .headerSpacingTopInches(0f)
                    .headerSpacingBottomInches(0.1f)
                    .gridSpacingRightInches(0f)
                    .gridSpacingLeftInches(0f)
                    .gridSpacingBottomInches(0f)
                    .build();

            PageConfig multiCallingCardsConfig = callingCardsSinglePage.toBuilder()
                    .gridRowAmount(2)
                    .gridColumnAmount(2)
                    .headerSpacingBottomInches(0.2f)
                    .labelHeightRatio(0.11f)
                    .gridLineThicknessInches(0.01f)
                    .build();

            PageConfig multiCallingLastCardsConfig = multiCallingCardsConfig.toBuilder()
                    .gridRowAmount(1)
                    .gridSpacingBottomInches(4.7f)
                    .build();

            DocumentBuilder.buildOnePerPageBingoCards(onePerPageBingoCards, portraitBingo);
            DocumentBuilder.buildTwoPerPageBingoCards(twoPerPageBingoCards, landscapeBingo);
            DocumentBuilder.buildInstructionsTokensCallingCards(instructionsTokensCallingCards,
                    tokens, callingCardsSinglePage,
                    multiCallingCardsConfig, multiCallingLastCardsConfig
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Bingo PDF Builder");
        BorderPane root = new BorderPane();
        TabPane tabPane = new TabPane();

        Tab assetsPathsTab = new Tab("Assets & Paths");
        AssetPathsTabData assetPathsTabData = new AssetPathsTabData();
        List<List<FieldSpec<?>>> assetsAndPathsTabGroups = List.of(
                List.of(
                        new FieldSpec<>("Theme Name:", assetPathsTabData.getTheme(), FieldType.TEXT)
                ),
                List.of(
                        new FieldSpec<>("Grid Color:", assetPathsTabData.getGridColor(), FieldType.COLOR),
                        new FieldSpec<>("Label Color:", assetPathsTabData.getFontColor(), FieldType.COLOR)
                ),
                List.of(
                        new FieldSpec<>("Image Directory:", assetPathsTabData.getBingoIcons(), FieldType.DIRECTORY),
                        new FieldSpec<>("Header Path:", assetPathsTabData.getHeader(), FieldType.FILE),
                        new FieldSpec<>("Frame Path:", assetPathsTabData.getFrame(), FieldType.FILE),
                        new FieldSpec<>("Free Space Path:", assetPathsTabData.getFreeSpace(), FieldType.FILE),
                        new FieldSpec<>("C.C. Header Path:", assetPathsTabData.getCcHeader(), FieldType.FILE),
                        new FieldSpec<>("Token Path:", assetPathsTabData.getToken(), FieldType.FILE)
                ),
                List.of(
                        new FieldSpec<>("Instructions Path:", assetPathsTabData.getInstructions(), FieldType.FILE),
                        new FieldSpec<>("Font Path:", assetPathsTabData.getFont(), FieldType.FILE),
                        new FieldSpec<>("Scissors Icon Path:", assetPathsTabData.getScissors(), FieldType.FILE)
                )
        );

        GridPane outerGrid = new GridPane();
        outerGrid.setPadding(new Insets(15));

        int outerRow = 0;

        for (List<FieldSpec<?>> group : assetsAndPathsTabGroups)  {
            GridPane innerGrid = new GridPane();
            innerGrid.setPadding(new Insets(15));
            innerGrid.setHgap(10);
            innerGrid.setVgap(10);

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setMinWidth(100);
            col1.setHgrow(Priority.NEVER);
            ColumnConstraints col2 = new ColumnConstraints();
            innerGrid.getColumnConstraints().addAll(col1, col2);

            addGroupToTab(innerGrid, group);

            outerGrid.add(innerGrid, 0, outerRow);
            outerRow++;
        }

        assetsPathsTab.setContent(outerGrid);
        assetsPathsTab.setClosable(false);


        Tab bingoCardsTab = new Tab("Bingo Cards");
        BingoCardsTabData bingoCardsTabData = new BingoCardsTabData();
        List<List<FieldSpec<?>>> bingoCardsTabGroups = List.of(
                List.of(
                        new FieldSpec<>("Theme Name:", assetPathsTabData.getTheme(), FieldType.TEXT)
                ),
                List.of(
                        new FieldSpec<>("Grid Color:", assetPathsTabData.getGridColor(), FieldType.COLOR),
                        new FieldSpec<>("Label Color:", assetPathsTabData.getFontColor(), FieldType.COLOR)
                ),
                List.of(
                        new FieldSpec<>("Image Directory:", assetPathsTabData.getBingoIcons(), FieldType.DIRECTORY),
                        new FieldSpec<>("Header Path:", assetPathsTabData.getHeader(), FieldType.FILE),
                        new FieldSpec<>("Frame Path:", assetPathsTabData.getFrame(), FieldType.FILE),
                        new FieldSpec<>("Free Space Path:", assetPathsTabData.getFreeSpace(), FieldType.FILE),
                        new FieldSpec<>("C.C. Header Path:", assetPathsTabData.getCcHeader(), FieldType.FILE),
                        new FieldSpec<>("Token Path:", assetPathsTabData.getToken(), FieldType.FILE)
                ),
                List.of(
                        new FieldSpec<>("Instructions Path:", assetPathsTabData.getInstructions(), FieldType.FILE),
                        new FieldSpec<>("Font Path:", assetPathsTabData.getFont(), FieldType.FILE),
                        new FieldSpec<>("Scissors Icon Path:", assetPathsTabData.getScissors(), FieldType.FILE)
                )
        );
        /*
        GridPane outerGrid = new GridPane();
        outerGrid.setPadding(new Insets(15));

        int outerRow = 0;

        for (List<FieldSpec<?>> group : assetsAndPathsTabGroups)  {
            GridPane innerGrid = new GridPane();
            innerGrid.setPadding(new Insets(15));
            innerGrid.setHgap(10);
            innerGrid.setVgap(10);

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setMinWidth(100);
            col1.setHgrow(Priority.NEVER);
            ColumnConstraints col2 = new ColumnConstraints();
            innerGrid.getColumnConstraints().addAll(col1, col2);

            addGroupToTab(innerGrid, group);

            outerGrid.add(innerGrid, 0, outerRow);
            outerRow++;
        }

        assetsPathsTab.setContent(outerGrid);
        assetsPathsTab.setClosable(false);
        */
        // Tab 2
        Tab bingoCards = new Tab("Bingo Cards");
        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(15));
        grid2.setHgap(10);
        grid2.setVgap(10);

        int row2 = 0;
        Label onePerPageHeader = new Label("1 Per Page");
        onePerPageHeader.setFont(Font.font("System", FontWeight.BOLD, 18));
        grid2.add(onePerPageHeader, 0, row2);
        row2 += 1;

        String[] headerFields = {
                "Header Spacing Top:", "Header Spacing Right:",
                "Header Spacing Bottom:", "Header Spacing Left:"
        };

        for (String field : headerFields) {
            grid2.add(new Label(field), 0, row2);
            grid2.add(new TextField(), 1, row2);
            row2 += 1;
        }

        GridPane.setMargin(grid2.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid2.getChildren().get(grid2.getChildren().size() - 2), new Insets(0,0,20,0));

        String[] gridFields = {
                "Grid Line Thickness:",
                "Grid Spacing Right:", "Grid Spacing Bottom:", "Grid Spacing Left:"
        };

        for (String field : gridFields) {
            grid2.add(new Label(field), 0, row2);
            grid2.add(new TextField(), 1, row2);
            row2 += 1;
        }

        GridPane.setMargin(grid2.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid2.getChildren().get(grid2.getChildren().size() - 2), new Insets(0,0,20,0));

        String[] iconsAndLabelsFields = {
            "Label Size:", "Cell Horizontal Spacing:", "Cell Vertical Spacing:", "Cell Gap:"
        };

        for (String field : iconsAndLabelsFields) {
            grid2.add(new Label(field), 0, row2);
            grid2.add(new TextField(), 1, row2);
            row2 += 1;
        }

        GridPane.setMargin(grid2.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid2.getChildren().get(grid2.getChildren().size() - 2), new Insets(0,0,20,0));

        grid2.add(new Label("Number of copies:"), 0, row2);
        grid2.add(new TextField(), 1, row2);
        row2 += 1;
        GridPane.setMargin(grid2.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid2.getChildren().get(grid2.getChildren().size() - 2), new Insets(0,0,20,0));


        grid2.add(new Button("Generate 1 Per Page"), 1, row2);

        row2 = 0;
        Label twoPerPageHeader = new Label("2 Per Page");
        twoPerPageHeader.setFont(Font.font("System", FontWeight.BOLD, 18));
        grid2.add(twoPerPageHeader, 4, row2);
        row2 += 1;

        String[] headerFieldsTwo = {
                "Header Spacing Top:", "Header Spacing Right:",
                "Header Spacing Bottom:", "Header Spacing Left:"
        };

        for (String field : headerFieldsTwo) {
            grid2.add(new Label(field), 4, row2);
            grid2.add(new TextField(), 5, row2);
            row2 += 1;
        }

        GridPane.setMargin(grid2.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid2.getChildren().get(grid2.getChildren().size() - 2), new Insets(0,0,20,0));

        String[] gridFieldsTwo = {
                "Grid Line Thickness:",
                "Grid Spacing Right:", "Grid Spacing Bottom:", "Grid Spacing Left:"
        };

        for (String field : gridFieldsTwo) {
            grid2.add(new Label(field), 4, row2);
            grid2.add(new TextField(), 5, row2);
            row2 += 1;
        }

        GridPane.setMargin(grid2.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid2.getChildren().get(grid2.getChildren().size() - 2), new Insets(0,0,20,0));

        String[] iconsAndLabelsFieldsTwo = {
                "Label Size:", "Cell Horizontal Spacing:", "Cell Vertical Spacing:", "Cell Gap:"
        };

        for (String field : iconsAndLabelsFieldsTwo) {
            grid2.add(new Label(field), 4, row2);
            grid2.add(new TextField(), 5, row2);
            row2 += 1;
        }

        GridPane.setMargin(grid2.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid2.getChildren().get(grid2.getChildren().size() - 2), new Insets(0,0,20,0));

        grid2.add(new Label("Number of copies:"), 4, row2);
        grid2.add(new TextField(), 5, row2);
        row2 += 1;
        GridPane.setMargin(grid2.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid2.getChildren().get(grid2.getChildren().size() - 2), new Insets(0,0,20,0));


        grid2.add(new Button("Generate 2 Per Page"), 5, row2);
        bingoCards.setContent(grid2);
        bingoCards.setClosable(false);

        Tab ccTokensInstructions = new Tab("C.C, Tokens, Instructions");
        GridPane grid3 = new GridPane();
        grid3.setPadding(new Insets(15));
        grid3.setHgap(10);
        grid3.setVgap(10);

        int row3 = 0;

        String[] ccGridFields = {
                "Grid Line Thickness:"
        };

        for (String field : ccGridFields) {
            grid3.add(new Label(field), 0, row3);
            grid3.add(new TextField(), 1, row3);
            row3 += 1;
        }

        GridPane.setMargin(grid3.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid3.getChildren().get(grid3.getChildren().size() - 2), new Insets(0,0,20,0));


        grid3.add(new Button("Generate C.C., Tokens, and Instructions"), 1, row3);
        ccTokensInstructions.setContent(grid3);
        ccTokensInstructions.setClosable(false);

        tabPane.getTabs().addAll(assetsPathsTab, bingoCards, ccTokensInstructions);

        root.setCenter(tabPane);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void addGroupToTab(GridPane grid, List<FieldSpec<?>> group) {
        int row = grid.getRowCount();
        for  (FieldSpec<?> fieldSpec : group) {
            grid.add(new Label(fieldSpec.getLabel()), 0, row);

            switch (fieldSpec.type) {
                case TEXT -> {
                    TextField textField = new TextField();
                    textField.textProperty().bindBidirectional((StringProperty) fieldSpec.getProperty());
                    grid.add(textField, 1, row);
                }
                case COLOR -> {
                    ColorPicker colorPicker = new ColorPicker();
                    colorPicker.valueProperty().bindBidirectional((ObjectProperty<Color>) fieldSpec.getProperty());
                    grid.add(colorPicker, 1, row);
                }
                case FILE -> {
                    TextField pathField = new TextField();
                    pathField.textProperty().bindBidirectional((StringProperty) fieldSpec.getProperty());
                    grid.add(pathField, 1, row);

                    Button browse = new Button("Browse...");
                    browse.setOnAction(e -> {
                        FileChooser chooser = new FileChooser();
                        File selected = chooser.showOpenDialog(grid.getScene().getWindow());
                        if (selected != null) ((StringProperty) fieldSpec.getProperty()).set(selected.toString());
                    });
                    grid.add(browse, 2, row);
                }
                case DIRECTORY -> {
                    TextField dirField = new TextField();
                    dirField.textProperty().bindBidirectional((StringProperty) fieldSpec.getProperty());
                    grid.add(dirField, 1, row);

                    Button browse = new Button("Browse...");
                    browse.setOnAction(e -> {
                        DirectoryChooser chooser = new DirectoryChooser();
                        File selected = chooser.showDialog(grid.getScene().getWindow());
                        if (selected != null) ((StringProperty) fieldSpec.getProperty()).set(selected.toString());
                    });
                    grid.add(browse, 2, row);
                }
            }

            row++;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
