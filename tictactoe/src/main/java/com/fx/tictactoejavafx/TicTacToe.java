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
import javafx.scene.control.Alert;


public class TicTacToe extends Application {
    private final GridPane gridPane = new GridPane();
    private final BorderPane borderPane = new BorderPane();
    private final Label title = new Label("Tic Tac Toe");
    private final Button[] btns = new Button[9];
    private final Button restartButton = new Button("Restart");
    private boolean gameOver = false;
    private int activePlayer = 0;
    private final String[] players = {"O", "X"};
    private final int[] gameStates = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
    private final int[][] winningPositions = {
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 4, 8},
            {2, 4, 6}
    };

    @Override
    public void start(Stage stage) {
        this.createGUI();
        Scene scene = new Scene(borderPane, 550, 650);
        stage.setTitle("Tic Tac Toe");
        stage.setScene(scene);
        stage.show();
    }

    public void createGUI() {
        Font font = Font.font("Arial", FontWeight.BOLD, 40);
        title.setFont(font);

        borderPane.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        borderPane.setPadding(new Insets(25, 25, 25, 25));
        int label = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setId(String.valueOf(label));
                button.setFont(font);
                button.setPrefHeight(150);
                button.setPrefWidth(150);
                gridPane.add(button, j, i);
                gridPane.setAlignment(Pos.CENTER);
                btns[label++] = button;
            }
        }
        borderPane.setCenter(gridPane);
    }

    public boolean isWinner(int[] pos) {
        if (gameStates[pos[0]] == -1) {
            return false;
        }
        return (gameStates[pos[0]] == gameStates[pos[1]])
                && (gameStates[pos[0]] == gameStates[pos[2]]);
    }

    public boolean isBoardFull() {
        for (int state : gameStates) {
            if (state == -1) {
                return false;
            }
        }
        return true;
    }

    public void checkForWinner() {
        if (gameOver)
            return;

        for (int i = 0; i < 8; i++) {
            if (isWinner(winningPositions[i])) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setContentText("Player " + players[activePlayer] + " won");
                alert.show();
                gameOver = true;
                restartButton.setDisable(false);
                break;
            }
        }
        if (!gameOver && isBoardFull()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setContentText("It's a draw!");
            alert.show();
            gameOver = true;
            restartButton.setDisable(false);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}