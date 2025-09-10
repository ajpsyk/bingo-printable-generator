package org.bingo.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

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
        HBox row = new HBox();
        row.setPadding(new Insets(15));
        row.setSpacing(10); // gap between label column and field column

// Column 1: labels
        VBox labelsColumn = new VBox(10); // vertical spacing
        labelsColumn.setAlignment(Pos.TOP_RIGHT);
        labelsColumn.setPrefWidth(150); // fixed width so all fields start aligned
        labelsColumn.getChildren().addAll(
                new Label("Theme Name:"),
                new Label("Output Directory:"),
                new Label("Bingo Images:"),
                new Label("Bingo Header Image:"),
                new Label("Frame Image:"),
                new Label("Free Space Image:"),
                new Label("Calling Cards Header:"),
                new Label("Token Image:"),
                new Label("Instructions Path:"),
                new Label("Font Path:"),
                new Label("Scissors Image Path:")
        );

// Column 2: fields
        VBox fieldsColumn = new VBox(10);
        fieldsColumn.getChildren().addAll(
                new TextField(), // Theme Name
                new TextField(), // Output Directory
                new TextField(), // Bingo Images
                new TextField(), // Bingo Header Image
                new TextField(), // Frame Image
                new TextField(), // Free Space Image
                new TextField(), // Calling Cards Header
                new TextField(), // Token Image
                new TextField(), // Instructions Path
                new TextField(), // Font Path
                new TextField()  // Scissors Image Path
        );

// Add columns to HBox
        row.getChildren().addAll(labelsColumn, fieldsColumn);
        generalTab.setContent(row);

        // field for frame path
        // field for header path
        // filed for icons path
        // field for freespace path
        // field for token path
        // field for font color

        // fields for page margins

        // field for instructions
        // field of scissors
        // field for font path
        generalTab.setClosable(false);

        Tab bingoCards = new Tab("Bingo Cards");
        bingoCards.setClosable(false);

        Tab ccTokensRules = new Tab("C.C. Tokens and Rules");
        ccTokensRules.setClosable(false);

        tabPane.getTabs().addAll(generalTab, bingoCards, ccTokensRules);

        root.setCenter(tabPane);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
