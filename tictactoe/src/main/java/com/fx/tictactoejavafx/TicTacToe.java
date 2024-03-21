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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



public class TicTacToe extends Application {
    private final GridPane gridPane = new GridPane();
    private final BorderPane borderPane = new BorderPane();
    private final Label title = new Label("Tic Tac Toe");
    private final Button[] btns = new Button[9];
    private final Button restartButton = new Button("Restart");
    private boolean gameOver = false;
    private final int AI = -1;
    private final int USER = 1;
    private final HashMap<Integer, String> players = new HashMap<Integer, String>() {{
        put(USER, "O");
        put(AI, "X");
    }};
    private final int[] gameStates = new int[9];
    private final Font font = Font.font("Arial", FontWeight.BOLD, 40);
    private List<Integer> rows = new ArrayList<Integer>(Arrays.asList(0, 0, 0));
    private List<Integer> cols = new ArrayList<Integer>(Arrays.asList(0, 0, 0));
    private int diagonal = 0;
    private int antidiagonal = 0;
    private int total_moves = 0;

    public static void main(String[] args) {
        launch(args);
    }


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
        borderPane.setPadding(new Insets(15, 15, 15, 15));

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

    public void handleEvent() {
        restartButton.setOnAction(actionEvent -> {
            gameOver = false;
            for (Button button : btns) {
                button.setGraphic(null);
            }
            rows = new ArrayList<Integer>(Arrays.asList(0, 0, 0));
            cols = new ArrayList<Integer>(Arrays.asList(0, 0, 0));
            diagonal = 0;
            antidiagonal = 0;
            total_moves = 0;
            Arrays.fill(gameStates, 0);
            restartButton.setDisable(true);
            System.out.println("Restart button clicked");
        });
        for (Button button : btns) {
            button.setOnAction(actionEvent -> {
                Button btn = (Button) actionEvent.getSource();
                int position = Integer.parseInt(btn.getId());
                if (gameOver) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Game Over");
                    alert.setContentText("Game Over! Try to restart the game");
                    alert.show();
                } else {
                    if (gameStates[position] == 0) {
                        btn.setGraphic(new ImageView(
                                new Image("file:src/main/resources/assets/circle.png", 100, 100, false, false)
                        ));
                        gameStates[position] = USER;
                        storeMove(position, USER);
                        checkForWinner();
                        if (!gameOver) {
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                            pause.setOnFinished(e -> makeMoveWithAI());
                            pause.play();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Notification");
                        alert.setContentText("This place has already been marked");
                        alert.show();
                    }
                }
            });
        }
    }

    public void storeMove(int position, int activePlayer) {
        int row = position / 3, col = position % 3;

        rows.set(row, rows.get(row)+activePlayer);
        cols.set(col, cols.get(col)+activePlayer);

        if (row == col)
            diagonal += activePlayer;
        if (row + col == 2)
            antidiagonal += activePlayer;

        total_moves += 1;
    }

    public void removeMove(int position, int activePlayer) {
        storeMove(position, -1 * activePlayer);
        total_moves -= 2;
    }

    public boolean isWinner(int player) {
        for (int row : rows)
            if (row * player == rows.size())
                return true;
        for (int col : cols)
            if (col * player == cols.size())
                return true;
        if (diagonal * player == rows.size() ||
        antidiagonal * player == rows.size())
            return true;
        else
            return false;
    }

    public boolean isBoardFull() {
            return total_moves == rows.size() * cols.size();
    }

    public void checkForWinner() {
        if (gameOver)
            return;
        if (isWinner(USER) || isWinner(AI) ) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            int winner = isWinner(USER) ? USER:AI;
            alert.setContentText("Player " + players.get(winner) + " won");
            alert.show();
            gameOver = true;
            restartButton.setDisable(false);
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

    public void makeMoveWithAI() {
        int bestMove = -1;
        int bestScore = Integer.MAX_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            if (gameStates[i] == 0) {
                gameStates[i] = AI;
                storeMove(i, AI);
                int score = minimaxWithAlphaBeta(0, USER, alpha, beta);
                removeMove(i, AI);
                gameStates[i] = 0;

                if (score < bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        if (bestMove != -1) {
            btns[bestMove].setGraphic(new ImageView(
                    new Image("file:src/main/resources/assets/cross.png", 100, 100, false, false)
            ));
            gameStates[bestMove] = AI;
            storeMove(bestMove, AI);
            checkForWinner();
        }
    }

    public int minimaxWithAlphaBeta(int depth, int player, int alpha, int beta) {
        if (isWinner(USER)) return 20 ;
        if (isWinner(AI)) return -20;
        if (isBoardFull()) return 0;

        if (player == USER) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (gameStates[i] == 0) {
                    gameStates[i] = USER;
                    storeMove(i, USER);
                    int score = minimaxWithAlphaBeta(depth + 1, AI, alpha, beta);
                    removeMove(i, USER);
                    gameStates[i] = 0;
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
                if (gameStates[i] == 0) {
                    gameStates[i] = AI;
                    storeMove(i, AI);
                    int score = minimaxWithAlphaBeta(depth + 1, USER, alpha, beta);
                    removeMove(i, AI);
                    gameStates[i] = 0;
                    bestScore = Math.min(score, bestScore);
                    beta = Math.min(beta, bestScore);
                    if (beta <= alpha)
                        break;
                }
            }
            return bestScore;
        }
    }
}