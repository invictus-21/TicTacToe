package com.fx.tictactoejavafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class TicTacToe extends Application {

    private final GridPane gridPane = new GridPane();
    private final BorderPane borderPane = new BorderPane();
    private final Label title = new Label("Tic Tac Toe");

    @Override
    public void start(Stage stage) {

        this.createGUI();
        Scene scene = new Scene(borderPane, 550, 650);
        stage.setTitle("Tic Tac Toe");
        stage.setScene(scene);
        stage.show();
    }

    private void createGUI() {
        Font font = Font.font("Arial", FontWeight.BOLD, 40);
        title.setFont(font);

        borderPane.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        borderPane.setPadding(new Insets(25, 25, 25, 25));
    }

    public static void main(String[] args) {
        launch(args);
    }
}