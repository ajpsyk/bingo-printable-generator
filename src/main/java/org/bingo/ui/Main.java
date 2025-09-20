package org.bingo.ui;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
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
    private final AssetPathsTabData assetPathsTabData = new AssetPathsTabData();
    private final BingoCardsTabData bingoCardsTabData = new BingoCardsTabData();
    private final BingoCardsTabData landscapeBingoCardsTabData = new BingoCardsTabData();

    @Override
    public void start(Stage primaryStage) {
        List<List<FieldSpec<?>>> assetsAndPathsTabGroups = List.of(
                List.of(
                        new FieldSpec<>(
                                "Theme Name:",
                                assetPathsTabData.getTheme(),
                                FieldType.TEXT
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Grid Color:",
                                assetPathsTabData.getGridColor(),
                                FieldType.COLOR
                        ),
                        new FieldSpec<>(
                                "Label Color:",
                                assetPathsTabData.getFontColor(),
                                FieldType.COLOR
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Image Directory:",
                                assetPathsTabData.getBingoIcons(),
                                FieldType.DIRECTORY
                        ),
                        new FieldSpec<>(
                                "Header Path:",
                                assetPathsTabData.getHeader(),
                                FieldType.FILE
                        ),
                        new FieldSpec<>(
                                "Frame Path:",
                                assetPathsTabData.getFrame(),
                                FieldType.FILE),
                        new FieldSpec<>(
                                "Free Space Path:",
                                assetPathsTabData.getFreeSpace(),
                                FieldType.FILE
                        ),
                        new FieldSpec<>(
                                "C.C. Header Path:",
                                assetPathsTabData.getCcHeader(),
                                FieldType.FILE
                        ),
                        new FieldSpec<>(
                                "Token Path:",
                                assetPathsTabData.getToken(),
                                FieldType.FILE
                        ),
                        new FieldSpec<>(
                                "Output Path:",
                                assetPathsTabData.getOutput(),
                                FieldType.DIRECTORY
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Instructions Path:",
                                assetPathsTabData.getInstructions(),
                                FieldType.FILE
                        ),
                        new FieldSpec<>(
                                "Font Path:",
                                assetPathsTabData.getFont(),
                                FieldType.FILE
                        ),
                        new FieldSpec<>(
                                "Scissors Icon Path:",
                                assetPathsTabData.getScissors(),
                                FieldType.FILE
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Generate C.C., Tokens, and Instructions",
                                null,
                                FieldType.BUTTON
                        )
                )
        );


        List<List<FieldSpec<?>>> bingoCardsTabGroups = List.of(
                List.of(
                        new FieldSpec<>(
                                "1 Per Page",
                                null,
                                FieldType.HEADER
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Header Spacing Top:",
                                bingoCardsTabData.getHeaderSpacingTop(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Header Spacing Right:",
                                bingoCardsTabData.getHeaderSpacingRight(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Header Spacing Bottom:",
                                bingoCardsTabData.getHeaderSpacingBottom(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Header Spacing Left:",
                                bingoCardsTabData.getHeaderSpacingLeft(),
                                FieldType.DOUBLE
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Grid Line Thickness:",
                                bingoCardsTabData.getGridLineThickness(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Grid Spacing Right:",
                                bingoCardsTabData.getGridSpacingRight(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Grid Spacing Bottom:",
                                bingoCardsTabData.getGridSpacingBottom(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Grid Spacing Left:",
                                bingoCardsTabData.getGridSpacingLeft(),
                                FieldType.DOUBLE
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Label Size:",
                                bingoCardsTabData.getLabelSize(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Cell Padding:",
                                bingoCardsTabData.getCellHorizontalSpacing(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Label Spacing Bottom:",
                                bingoCardsTabData.getCellVerticalSpacing(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Cell Gap:",
                                bingoCardsTabData.getCellGap(),
                                FieldType.DOUBLE
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Number of copies:",
                                bingoCardsTabData.getCopies(),
                                FieldType.INTEGER
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Generate 1 Per Page",
                                null,
                                FieldType.BUTTON
                        )
                )
        );

        List<List<FieldSpec<?>>> landscapeBingoCardsTabGroups = List.of(
                List.of(
                        new FieldSpec<>(
                                "2 Per Page",
                                null,
                                FieldType.HEADER
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Header Spacing Top:",
                                landscapeBingoCardsTabData.getHeaderSpacingTop(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Header Spacing Right:",
                                landscapeBingoCardsTabData.getHeaderSpacingRight(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Header Spacing Bottom:",
                                landscapeBingoCardsTabData.getHeaderSpacingBottom(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Header Spacing Left:",
                                landscapeBingoCardsTabData.getHeaderSpacingLeft(),
                                FieldType.DOUBLE
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Grid Line Thickness:",
                                landscapeBingoCardsTabData.getGridLineThickness(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Grid Spacing Right:",
                                landscapeBingoCardsTabData.getGridSpacingRight(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Grid Spacing Bottom:",
                                landscapeBingoCardsTabData.getGridSpacingBottom(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Grid Spacing Left:",
                                landscapeBingoCardsTabData.getGridSpacingLeft(),
                                FieldType.DOUBLE
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Label Size:",
                                landscapeBingoCardsTabData.getLabelSize(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Cell Padding:",
                                landscapeBingoCardsTabData.getCellHorizontalSpacing(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Label Spacing Bottom:",
                                landscapeBingoCardsTabData.getCellVerticalSpacing(),
                                FieldType.DOUBLE
                        ),
                        new FieldSpec<>(
                                "Cell Gap:",
                                landscapeBingoCardsTabData.getCellGap(),
                                FieldType.DOUBLE
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Number of copies:",
                                landscapeBingoCardsTabData.getCopies(),
                                FieldType.INTEGER
                        )
                ),
                List.of(
                        new FieldSpec<>(
                                "Generate 2 Per Page",
                                null,
                                FieldType.BUTTON
                        )
                )
        );

        primaryStage.setTitle("Bingo PDF Builder");
        BorderPane root = new BorderPane();
        TabPane tabPane = new TabPane();

        Tab assetsPaths = new Tab("Assets & Paths");
        assetsPaths.setClosable(false);
        Tab bingoCards = new Tab("Bingo Cards");
        bingoCards.setClosable(false);

        GridPane assetsGrid = new GridPane();
        GridPane bingoCardsGrid = new GridPane();

        ColumnConstraints outerConstraint = new ColumnConstraints();
        outerConstraint.setMinWidth(200);
        outerConstraint.setHgrow(Priority.NEVER);
        bingoCardsGrid.getColumnConstraints().addAll(outerConstraint);

        addContentToTab(assetsGrid, assetsPaths, assetsAndPathsTabGroups, 100, 0);
        addContentToTab(bingoCardsGrid, bingoCards, bingoCardsTabGroups, 140, 0);
        addContentToTab(bingoCardsGrid, bingoCards, landscapeBingoCardsTabGroups, 140, 1);


        tabPane.getTabs().addAll(assetsPaths, bingoCards);

        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1000, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void addContentToTab(GridPane outerGrid, Tab tab, List<List<FieldSpec<?>>> content, int colWidth, int startColumn) {
        int outerRow = 0;

        for (List<FieldSpec<?>> group : content)  {
            GridPane innerGrid = new GridPane();
            innerGrid.setPadding(new Insets(15));
            innerGrid.setHgap(10);
            innerGrid.setVgap(10);

            ColumnConstraints innerConstraint = new ColumnConstraints();
            innerConstraint.setMinWidth(colWidth);
            innerConstraint.setHgrow(Priority.NEVER);
            innerGrid.getColumnConstraints().addAll(innerConstraint);

            addGroupToTab(innerGrid, group);

            outerGrid.add(innerGrid, startColumn, outerRow);
            outerRow++;
        }

        tab.setContent(outerGrid);
    }

    public void addGroupToTab(GridPane grid, List<FieldSpec<?>> group) {
        int row = grid.getRowCount();
        for  (FieldSpec<?> fieldSpec : group) {
            Label label = new Label(fieldSpec.getLabel());

            switch (fieldSpec.type) {
                case TEXT -> {
                    grid.add(new Label(fieldSpec.getLabel()), 0, row);
                    TextField textField = new TextField();
                    textField.textProperty().bindBidirectional((StringProperty) fieldSpec.getProperty());
                    grid.add(textField, 1, row);
                }
                case COLOR -> {
                    grid.add(new Label(fieldSpec.getLabel()), 0, row);
                    ColorPicker colorPicker = new ColorPicker();
                    colorPicker.valueProperty().bindBidirectional((Property<Color>) fieldSpec.getProperty());
                    grid.add(colorPicker, 1, row);
                }
                case FILE -> {
                    grid.add(new Label(fieldSpec.getLabel()), 0, row);
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
                    grid.add(new Label(fieldSpec.getLabel()), 0, row);
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
                case DOUBLE -> {
                    grid.add(new Label(fieldSpec.getLabel()), 0, row);
                    TextField textField = new TextField();
                    Bindings.bindBidirectional(
                            textField.textProperty(),
                            (DoubleProperty) fieldSpec.getProperty(),
                            new NumberStringConverter()
                    );
                    grid.add(textField, 1, row);
                }
                case INTEGER -> {
                    grid.add(new Label(fieldSpec.getLabel()), 0, row);
                    TextField textField = new TextField();
                    Bindings.bindBidirectional(
                            textField.textProperty(),
                            (IntegerProperty) fieldSpec.getProperty(),
                            new NumberStringConverter()
                    );
                    grid.add(textField, 1, row);
                }
                case HEADER -> {
                    label.setFont(Font.font("System", FontWeight.BOLD, 18));
                    grid.add(label, 0, row);
                }
                case BUTTON -> {
                    Button btn = new Button(label.getText());
                    btn.setOnAction(e -> handleButtonAction(fieldSpec.getLabel()));
                    grid.add(btn, 1, row);
                }
                default -> {}
            }

            row++;
        }
    }

    public void handleButtonAction(String label) {
        if (assetPathsTabData.getTheme().getValue() == null || assetPathsTabData.getTheme().getValue().isBlank()) {
            showError("Theme Name is required.");
            return;
        }

        if (assetPathsTabData.getBingoIcons().getValue() == null || assetPathsTabData.getBingoIcons().getValue().isBlank()) {
            showError("Image Directory is required.");
            return;
        }

        if (assetPathsTabData.getHeader().getValue() == null || assetPathsTabData.getHeader().getValue().isBlank()) {
            showError("Header Path is required.");
            return;
        }

        if (assetPathsTabData.getFrame().getValue() == null || assetPathsTabData.getFrame().getValue().isBlank()) {
            showError("Frame Path is required.");
            return;
        }

        if (assetPathsTabData.getFreeSpace().getValue() == null || assetPathsTabData.getFreeSpace().getValue().isBlank()) {
            showError("Free Space Path is required.");
            return;
        }

        if (assetPathsTabData.getCcHeader().getValue() == null || assetPathsTabData.getCcHeader().getValue().isBlank()) {
            showError("C.C. Header Path is required.");
            return;
        }

        if (assetPathsTabData.getToken().getValue() == null || assetPathsTabData.getToken().getValue().isBlank()) {
            showError("Token Path is required.");
            return;
        }

        if (assetPathsTabData.getOutput().getValue() == null || assetPathsTabData.getOutput().getValue().isBlank()) {
            showError("Output Path is required.");
            return;
        }

        if (assetPathsTabData.getInstructions().getValue() == null || assetPathsTabData.getInstructions().getValue().isBlank()) {
            showError("Instructions Path is required.");
            return;
        }

        if (assetPathsTabData.getFont().getValue() == null || assetPathsTabData.getFont().getValue().isBlank()) {
            showError("Font Path is required.");
            return;
        }

        if (assetPathsTabData.getScissors().getValue() == null || assetPathsTabData.getScissors().getValue().isBlank()) {
            showError("Scissors Path is required.");
            return;
        }
        String THEME_NAME = assetPathsTabData.getTheme().getValue();
        Path output = Paths.get(assetPathsTabData.getOutput().getValue());
        Path ONE_PER_PAGE_OUTPUT_FILE = output.resolve(THEME_NAME + "_1PerPage.pdf");
        Path TWO_PER_PAGE_OUTPUT_FILE = output.resolve(THEME_NAME + "_2PerPage.pdf");
        Path CALLING_CARDS_TOKENS_RULES_OUTPUT_FILE = output.resolve(
                THEME_NAME + "_Calling Cards_Tokens_Rules.pdf"
        );


        AssetPaths paths = AssetPaths.builder()
                .framePath(Paths.get(assetPathsTabData.getFrame().getValue()))
                .headerPath(Paths.get(assetPathsTabData.getHeader().getValue()))
                .iconsPath(Paths.get(assetPathsTabData.getBingoIcons().getValue()))
                .fontPath(Paths.get(assetPathsTabData.getFont().getValue()))
                .freeSpacePath(Paths.get(assetPathsTabData.getFreeSpace().getValue()))
                .instructionsPath(Paths.get(assetPathsTabData.getInstructions().getValue()))
                .scissorsIconPath(Paths.get(assetPathsTabData.getScissors().getValue()))
                .tokenPath(Paths.get(assetPathsTabData.getToken().getValue()))
                .callingCardsHeaderPath(Paths.get(assetPathsTabData.getCcHeader().getValue()))
                .build();

        Color fontColor = assetPathsTabData.getFontColor().getValue();
        Color gridColor = assetPathsTabData.getGridColor().getValue();
        DeviceRgb pdfFontColor = new DeviceRgb(
                (int) (fontColor.getRed() * 255),
                (int) (fontColor.getGreen() * 255),
                (int) (fontColor.getBlue() * 255)
        );
        DeviceRgb pdfGridColor = new DeviceRgb(
                (int) (gridColor.getRed() * 255),
                (int) (gridColor.getGreen() * 255),
                (int) (gridColor.getBlue() * 255)
        );

        switch (label) {
            case "Generate 1 Per Page" -> {
                DocumentConfig onePerPageBingoCards = DocumentConfig.builder()
                        .assets(paths)
                        .outputPath(ONE_PER_PAGE_OUTPUT_FILE)
                        .fontColor(pdfFontColor)
                        .marginTopInches(0.25f)
                        .marginBottomInches(0.25f)
                        .marginLeftInches(0.25f)
                        .marginRightInches(0.25f)
                        .build();

                PageConfig portraitBingo = PageConfig.builder()
                        .headerSpacingTopInches(bingoCardsTabData.getHeaderSpacingTop().getValue().floatValue())
                        .headerSpacingRightInches(bingoCardsTabData.getHeaderSpacingRight().getValue().floatValue())
                        .headerSpacingBottomInches(bingoCardsTabData.getHeaderSpacingBottom().getValue().floatValue())
                        .headerSpacingLeftInches(bingoCardsTabData.getHeaderSpacingLeft().getValue().floatValue())
                        .gridLineColor(pdfGridColor)
                        .gridRowAmount(5)
                        .gridColumnAmount(5)
                        .gridLineThicknessInches(bingoCardsTabData.getGridLineThickness().getValue().floatValue())
                        .gridSpacingRightInches(bingoCardsTabData.getGridSpacingRight().getValue().floatValue())
                        .gridSpacingLeftInches(bingoCardsTabData.getGridSpacingLeft().getValue().floatValue())
                        .gridSpacingBottomInches(bingoCardsTabData.getGridSpacingBottom().getValue().floatValue())
                        .labelHeightRatio(bingoCardsTabData.getLabelSize().getValue().floatValue())
                        .cellSpacingXRatio(bingoCardsTabData.getCellHorizontalSpacing().getValue().floatValue())
                        .cellSpacingYRatio(bingoCardsTabData.getCellVerticalSpacing().getValue().floatValue())
                        .cellGapRatio(bingoCardsTabData.getCellGap().getValue().floatValue())
                        .copies(bingoCardsTabData.getCopies().getValue())
                        .build();
                try {
                    DocumentBuilder.buildOnePerPageBingoCards(onePerPageBingoCards, portraitBingo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            case "Generate 2 Per Page" -> {
                DocumentConfig twoPerPageBingoCards = DocumentConfig.builder()
                        .assets(paths)
                        .fontColor(pdfFontColor)
                        .marginLeftInches(0.25f)
                        .marginRightInches(0.25f)
                        .outputPath(TWO_PER_PAGE_OUTPUT_FILE)
                        .pageSize(PageSize.LETTER.rotate())
                        .marginTopInches(0.625f)
                        .marginBottomInches(0.625f)
                        .seed(new Random(1))
                        .build();

                PageConfig landscapeBingo = PageConfig.builder()
                        .headerSpacingTopInches(landscapeBingoCardsTabData.getHeaderSpacingTop().getValue().floatValue())
                        .headerSpacingRightInches(landscapeBingoCardsTabData.getHeaderSpacingRight().getValue().floatValue())
                        .headerSpacingBottomInches(landscapeBingoCardsTabData.getHeaderSpacingBottom().getValue().floatValue())
                        .headerSpacingLeftInches(landscapeBingoCardsTabData.getHeaderSpacingLeft().getValue().floatValue())
                        .gridLineColor(pdfGridColor)
                        .gridRowAmount(5)
                        .gridColumnAmount(5)
                        .gridLineThicknessInches(landscapeBingoCardsTabData.getGridLineThickness().getValue().floatValue())
                        .gridSpacingRightInches(landscapeBingoCardsTabData.getGridSpacingRight().getValue().floatValue())
                        .gridSpacingLeftInches(landscapeBingoCardsTabData.getGridSpacingLeft().getValue().floatValue())
                        .gridSpacingBottomInches(landscapeBingoCardsTabData.getGridSpacingBottom().getValue().floatValue())
                        .labelHeightRatio(landscapeBingoCardsTabData.getLabelSize().getValue().floatValue())
                        .cellSpacingXRatio(landscapeBingoCardsTabData.getCellHorizontalSpacing().getValue().floatValue())
                        .cellSpacingYRatio(landscapeBingoCardsTabData.getCellVerticalSpacing().getValue().floatValue())
                        .cellGapRatio(landscapeBingoCardsTabData.getCellGap().getValue().floatValue())
                        .copies(landscapeBingoCardsTabData.getCopies().getValue())
                        .build();
                try {
                    DocumentBuilder.buildTwoPerPageBingoCards(twoPerPageBingoCards, landscapeBingo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case "Generate C.C., Tokens, and Instructions" -> {

                DocumentConfig instructionsTokensCallingCards = DocumentConfig.builder()
                        .assets(paths)
                        .fontColor(pdfFontColor)
                        .marginTopInches(0.25f)
                        .marginBottomInches(0.25f)
                        .marginLeftInches(0.25f)
                        .marginRightInches(0.25f)
                        .outputPath(CALLING_CARDS_TOKENS_RULES_OUTPUT_FILE)
                        .build();

                PageConfig tokens = PageConfig.builder()
                        .gridLineThicknessInches(bingoCardsTabData.getGridLineThickness().getValue().floatValue())
                        .cellSpacingYRatio(bingoCardsTabData.getCellVerticalSpacing().getValue().floatValue())
                        .gridLineColor(new DeviceRgb(115, 115, 115))
                        .gridRowAmount(9)
                        .gridColumnAmount(9)
                        .copies(1)
                        .headerSpacingBottomInches(0.2f)
                        .headerSpacingRightInches(6.8f)
                        .cellSpacingXRatio(0.15f)
                        .build();

                PageConfig callingCardsSinglePage = PageConfig.builder()
                        .gridLineColor(pdfGridColor)
                        .gridSpacingRightInches(bingoCardsTabData.getGridSpacingRight().getValue().floatValue())
                        .gridSpacingLeftInches(bingoCardsTabData.getGridSpacingLeft().getValue().floatValue())
                        .labelHeightRatio(bingoCardsTabData.getLabelSize().getValue().floatValue())
                        .cellSpacingXRatio(bingoCardsTabData.getCellHorizontalSpacing().getValue().floatValue())
                        .cellSpacingYRatio(bingoCardsTabData.getCellVerticalSpacing().getValue().floatValue())
                        .cellGapRatio(bingoCardsTabData.getCellGap().getValue().floatValue())
                        .gridRowAmount(6)
                        .gridColumnAmount(5)
                        .copies(1)
                        .gridLineThicknessInches(0.014f)
                        .headerSpacingBottomInches(0.1f)
                        .build();

                PageConfig multiCallingCardsConfig = callingCardsSinglePage.toBuilder()
                        .gridLineColor(new DeviceRgb(115, 115, 115))
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
                try {
                    DocumentBuilder.buildInstructionsTokensCallingCards(instructionsTokensCallingCards,
                            tokens, callingCardsSinglePage,
                            multiCallingCardsConfig, multiCallingLastCardsConfig
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void showError(String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Error");

        VBox box = new VBox(10);
        box.setPrefSize(210, 150);
        box.setPadding(new Insets(15));
        Label label = new Label(message);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        Button ok = new Button("OK");
        ok.setOnAction(e -> dialog.close());

        box.getChildren().addAll(label, ok);
        box.setAlignment(Pos.CENTER);

        Scene scene = new Scene(box);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
