package org.bingo.ui;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Main extends Application {
    private final AssetPathsTabData assetPathsTabData = new AssetPathsTabData();
    private final BingoCardsTabData bingoCardsTabData = new BingoCardsTabData();
    private final BingoCardsTabData landscapeBingoCardsTabData = new BingoCardsTabData();

    @Override
    public void start(Stage primaryStage) {
        loadData();

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

        List<List<FieldSpec<?>>> assetsAndPathsTabGroups = AssetPathsFields.assetPathsGroups(assetPathsTabData);
        List<List<FieldSpec<?>>> bingoCardsTabGroups = BingoCardsFields.bingoCardsGroups(bingoCardsTabData);
        List<List<FieldSpec<?>>> landscapeBingoCardsTabGroups = BingoCardsFields.landscapeBingoCardsGroups(landscapeBingoCardsTabData);

        addContentToTab(assetsGrid, assetsPaths, assetsAndPathsTabGroups, 100, 0);
        addContentToTab(bingoCardsGrid, bingoCards, bingoCardsTabGroups, 140, 0);
        addContentToTab(bingoCardsGrid, bingoCards, landscapeBingoCardsTabGroups, 140, 1);

        tabPane.getTabs().addAll(assetsPaths, bingoCards);

        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1000, 750);

        primaryStage.setOnCloseRequest(_ -> saveData());
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
                        chooser.setTitle("Select " + fieldSpec.getLabel());
                        File initialFile = new File(((StringProperty) fieldSpec.getProperty()).get());
                        if (initialFile.exists()) {
                            chooser.setInitialDirectory(initialFile.getParentFile());
                        }
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
                        chooser.setTitle("Select " + fieldSpec.getLabel());
                        File initialDir = new File(((StringProperty) fieldSpec.getProperty()).get());
                        if (initialDir.exists() && initialDir.isDirectory()) {
                            chooser.setInitialDirectory(initialDir);
                        }
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
                case RESET -> {
                    Button btn = new Button(label.getText());
                    btn.setOnAction(e -> showConfirmation(
                            "Are you sure you want to restore all settings to their default values?"
                            ));
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
        Path ONE_PER_PAGE_OUTPUT_FILE = output.resolve(THEME_NAME + "_1PerPage_" + bingoCardsTabData.getCopies().getValue() + "Set.pdf");
        Path TWO_PER_PAGE_OUTPUT_FILE = output.resolve(THEME_NAME + "_2PerPage_" + landscapeBingoCardsTabData.getCopies().getValue() + "Set.pdf");
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
                        .seed(new Random(1))
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

    private void handleResetAction (Stage dialog) {
        assetPathsTabData.reset();
        bingoCardsTabData.reset();
        landscapeBingoCardsTabData.reset();
        dialog.close();
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

    private void showConfirmation(String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Confirm Reset");

        VBox box = new VBox(10);
        box.setPrefSize(500, 150);
        box.setPadding(new Insets(15));
        Label label = new Label(message);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        Button yes = new Button("Yes");
        Button cancel = new Button("Cancel");
        yes.setOnAction(e -> handleResetAction(dialog));
        yes.setFocusTraversable(false);
        cancel.setOnAction(e -> dialog.close());

        buttons.getChildren().addAll(yes, cancel);
        box.getChildren().addAll(label, buttons);
        box.setAlignment(Pos.CENTER);

        Scene scene = new Scene(box);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public void saveData() {
        try {
            ConfigData configData = new ConfigData(
                    getAssetPathsMap(),
                    getBingoCardsMap(bingoCardsTabData),
                    getBingoCardsMap(landscapeBingoCardsTabData)
            );

            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("config.json"), configData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        File file = new File("config.json");
        if (!file.exists()) return;

        try {
            ObjectMapper mapper = new ObjectMapper();
            ConfigData config = mapper.readValue(file, ConfigData.class);

            config.assetPaths().forEach((k, v) -> {
                switch (k) {
                    case "theme" -> assetPathsTabData.getTheme().set(v);
                    case "gridColor" -> assetPathsTabData.getGridColor().set(Color.web(v));
                    case "fontColor" -> assetPathsTabData.getFontColor().set(Color.web(v));
                    case "bingoIcons" -> assetPathsTabData.getBingoIcons().set(v);
                    case "header" -> assetPathsTabData.getHeader().set(v);
                    case "frame" -> assetPathsTabData.getFrame().set(v);
                    case "freeSpace" -> assetPathsTabData.getFreeSpace().set(v);
                    case "ccHeader" -> assetPathsTabData.getCcHeader().set(v);
                    case "token" -> assetPathsTabData.getToken().set(v);
                    case "output" -> assetPathsTabData.getOutput().set(v);
                    case "instructions" -> assetPathsTabData.getInstructions().set(v);
                    case "font" -> assetPathsTabData.getFont().set(v);
                    case "scissors" -> assetPathsTabData.getScissors().set(v);
                }
            });

            config.bingoCards().forEach((k, v) -> setBingoProperty(bingoCardsTabData, k, v));
            config.landscapeBingoCards().forEach((k, v) -> setBingoProperty(landscapeBingoCardsTabData, k, v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBingoProperty(BingoCardsTabData data, String key, Double value) {
        switch (key) {
            case "headerSpacingTop" -> data.getHeaderSpacingTop().set(value);
            case "headerSpacingRight" -> data.getHeaderSpacingRight().set(value);
            case "headerSpacingBottom" -> data.getHeaderSpacingBottom().set(value);
            case "headerSpacingLeft" -> data.getHeaderSpacingLeft().set(value);
            case "gridLineThickness" -> data.getGridLineThickness().set(value);
            case "gridSpacingRight" -> data.getGridSpacingRight().set(value);
            case "gridSpacingBottom" -> data.getGridSpacingBottom().set(value);
            case "gridSpacingLeft" -> data.getGridSpacingLeft().set(value);
            case "labelSize" -> data.getLabelSize().set(value);
            case "cellHorizontalSpacing" -> data.getCellHorizontalSpacing().set(value);
            case "cellVerticalSpacing" -> data.getCellVerticalSpacing().set(value);
            case "cellGap" -> data.getCellGap().set(value);
            case "copies" -> data.getCopies().set(value.intValue());
        }
    }

    public record ConfigData(
            Map<String, String> assetPaths,
            Map<String, Double> bingoCards,
            Map<String, Double> landscapeBingoCards
    ) {}

    private Map<String, String> getAssetPathsMap() {
        return Map.ofEntries(
                Map.entry("theme", assetPathsTabData.getTheme().getValue()),
                Map.entry("gridColor", colorToHex(assetPathsTabData.getGridColor().getValue())),
                Map.entry("fontColor", colorToHex(assetPathsTabData.getFontColor().getValue())),
                Map.entry("bingoIcons", assetPathsTabData.getBingoIcons().getValue()),
                Map.entry("header", assetPathsTabData.getHeader().getValue()),
                Map.entry("frame", assetPathsTabData.getFrame().getValue()),
                Map.entry("freeSpace", assetPathsTabData.getFreeSpace().getValue()),
                Map.entry("ccHeader", assetPathsTabData.getCcHeader().getValue()),
                Map.entry("token", assetPathsTabData.getToken().getValue()),
                Map.entry("output", assetPathsTabData.getOutput().getValue()),
                Map.entry("instructions", assetPathsTabData.getInstructions().getValue()),
                Map.entry("font", assetPathsTabData.getFont().getValue()),
                Map.entry("scissors", assetPathsTabData.getScissors().getValue())
        );
    }

    private Map<String, Double> getBingoCardsMap(BingoCardsTabData data) {
        return Map.ofEntries(
                Map.entry("headerSpacingTop", data.getHeaderSpacingTop().getValue()),
                Map.entry("headerSpacingRight", data.getHeaderSpacingRight().getValue()),
                Map.entry("headerSpacingBottom", data.getHeaderSpacingBottom().getValue()),
                Map.entry("headerSpacingLeft", data.getHeaderSpacingLeft().getValue()),
                Map.entry("gridLineThickness", data.getGridLineThickness().getValue()),
                Map.entry("gridSpacingRight", data.getGridSpacingRight().getValue()),
                Map.entry("gridSpacingBottom", data.getGridSpacingBottom().getValue()),
                Map.entry("gridSpacingLeft", data.getGridSpacingLeft().getValue()),
                Map.entry("labelSize", data.getLabelSize().getValue()),
                Map.entry("cellHorizontalSpacing", data.getCellHorizontalSpacing().getValue()),
                Map.entry("cellVerticalSpacing", data.getCellVerticalSpacing().getValue()),
                Map.entry("cellGap", data.getCellGap().getValue()),
                Map.entry("copies", (double) data.getCopies().getValue())
        );
    }

    private String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
