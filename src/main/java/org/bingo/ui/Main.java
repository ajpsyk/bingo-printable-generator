package org.bingo.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bingo PDF Builder");

        // Root layout
        BorderPane root = new BorderPane();

        // TabPane
        TabPane tabPane = new TabPane();

        // Tabs
        Tab generalTab = new Tab("Assets");

        // Main container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);

        int row = 0;

        // Group 1: Theme Name
        grid.add(new Label("Theme Name:"), 0, row);
        grid.add(new TextField(), 1, row);
        GridPane.setMargin(grid.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid.getChildren().get(grid.getChildren().size() - 2), new Insets(0,0,20,0));
        row += 1;

        grid.add(new Label("Grid Color:"), 0, row);
        grid.add(new TextField(), 1, row);
        row += 1;
        grid.add(new Label("Label Color:"), 0, row);
        grid.add(new TextField(), 1, row);
        GridPane.setMargin(grid.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid.getChildren().get(grid.getChildren().size() - 2), new Insets(0,0,20,0));
        row += 1;

        // Group 2: Bingo images -> output path
        String bingoImages = "Bingo Images:";
        grid.add(new Label(bingoImages), 0, row);
        grid.add(new TextField(), 1, row);
        grid.add(new Button("Browse..."), 2, row);
        row += 1;
        String[] imageFields = {
                "Bingo Header Image:", "Frame Image:",
                "Free Space Image:", "Calling Cards Header:", "Token Image:"
        };
        for (String field : imageFields) {
            grid.add(new Label(field), 0, row);
            grid.add(new TextField(), 1, row);
            grid.add(new Button("Browse..."), 2, row);
            row += 1;
        }

        String outputPath = "Output Path:";
        grid.add(new Label(outputPath), 0, row);
        grid.add(new TextField(), 1, row);
        grid.add(new Button("Browse..."), 2, row);
        row += 1;

        GridPane.setMargin(grid.getChildren().getLast(), new Insets(0,0,20,0));
        GridPane.setMargin(grid.getChildren().get(grid.getChildren().size() - 2), new Insets(0,0,20,0));
        GridPane.setMargin(grid.getChildren().get(grid.getChildren().size() - 3), new Insets(0,0,20,0));

        // Group 3: Instructions, Font, Scissors
        String[] lastFields = {"Instructions Path:", "Font Path:", "Scissors Image Path:"};
        for (String field : lastFields) {
            grid.add(new Label(field), 0, row);
            grid.add(new TextField(), 1, row);
            grid.add(new Button("Browse..."), 2, row);
            row += 1;
        }
        generalTab.setContent(grid);
        generalTab.setClosable(false);

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

        Tab ccTokensRules = new Tab("C.C. Tokens and Rules");
        ccTokensRules.setClosable(false);

        tabPane.getTabs().addAll(generalTab, bingoCards, ccTokensRules);

        root.setCenter(tabPane);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
