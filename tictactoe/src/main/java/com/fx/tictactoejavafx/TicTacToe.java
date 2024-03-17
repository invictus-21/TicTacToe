package com.fx.tictactoejavafx;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Arrays;



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
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };
    private final Font font = Font.font("Arial", FontWeight.BOLD, 40);


    @Override
    public void start(Stage stage) {
        this.createGUI();
        this.handleEvent();
        Scene scene = new Scene(borderPane, 600, 600);
        stage.setTitle("Tic Tac Toe");
        stage.setScene(scene);
        stage.show();
    }

    public void createGUI() {
        title.setFont(font);
        restartButton.setFont(font);
        restartButton.setDisable(!gameOver);

        borderPane.setTop(title);
        borderPane.setBottom(restartButton);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(restartButton, Pos.CENTER);
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

    public int minimaxWithAlphaBeta(int depth, int player, int alpha, int beta) {
        if (isWinner(winningPositions[0])) return -1;
        if (isWinner(winningPositions[1])) return 1;
        if (isWinner(winningPositions[2])) return -1;
        if (isWinner(winningPositions[3])) return 1;
        if (isWinner(winningPositions[4])) return -1;
        if (isWinner(winningPositions[5])) return 1;
        if (isWinner(winningPositions[6])) return -1;
        if (isWinner(winningPositions[7])) return 1;
        if (isBoardFull()) return 0;

        if (player == 0) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (gameStates[i] == -1) {
                    gameStates[i] = player;
                    int score = minimaxWithAlphaBeta(depth + 1, 1 - player, alpha, beta);
                    gameStates[i] = -1;
                    bestScore = Math.max(score, bestScore);
                    alpha = Math.max(alpha, bestScore);
                    if (alpha >= beta)
                        break;
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (gameStates[i] == -1) {
                    gameStates[i] = player;
                    int score = minimaxWithAlphaBeta(depth + 1, 1 - player, alpha, beta);
                    gameStates[i] = -1;
                    bestScore = Math.min(score, bestScore);
                    beta = Math.min(beta, bestScore);
                    if (beta <= alpha)
                        break;
                }
            }
            return bestScore;
        }
    }

    public void makeMoveWithAI() {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            if (gameStates[i] == -1) {
                gameStates[i] = activePlayer;
                int score = minimaxWithAlphaBeta(0, 1 - activePlayer, alpha, beta);
                gameStates[i] = -1;

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        if (bestMove != -1) {
            btns[bestMove].setGraphic(new ImageView(
                    new Image("file:src/main/resources/assets/cross.png", 100, 100, false, false)
            ));
            gameStates[bestMove] = activePlayer;
            checkForWinner();
            activePlayer = 0;
        }
    }

    public void handleEvent() {
        restartButton.setOnAction(actionEvent -> {
            gameOver = false;
            activePlayer = 0;
            for (Button button : btns) {
                button.setGraphic(null);
            }
            Arrays.fill(gameStates, -1);
            restartButton.setDisable(true);
            System.out.println("Restart button clicked");
        });
        for (Button button : btns) {
            button.setOnAction(actionEvent -> {
                Button btn = (Button) actionEvent.getSource();
                int idS = Integer.parseInt(btn.getId());
                if (gameOver) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Game Over");
                    alert.setContentText("Game Over! Try to restart the game");
                    alert.show();
                } else {
                    if (gameStates[idS] == -1) {
                        btn.setGraphic(new ImageView(
                                new Image("file:src/main/resources/assets/circle.png", 100, 100, false, false)
                        ));
                        gameStates[idS] = activePlayer;
                        checkForWinner();
                        activePlayer = 1;
                        if (!gameOver) {
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                            pause.setOnFinished(e -> makeMoveWithAI());
                            pause.play();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Notification");
                        alert.setContentText("Player " + players[activePlayer] + " has already marked this place");
                        alert.show();
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}