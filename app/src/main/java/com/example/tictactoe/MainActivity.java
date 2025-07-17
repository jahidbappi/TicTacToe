package com.example.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private boolean playerXTurn = true;
    private int[][] boardState = new int[3][3]; // 0=empty, 1=X, 2=O
    private GridLayout gridLayout;
    private TextView playerTurnText;

    // Colors
    private final int COLOR_X = Color.RED;
    private final int COLOR_O = Color.GREEN;
    private final int COLOR_TEXT_WHITE = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views with null checks
        gridLayout = findViewById(R.id.gridLayout);
        if (gridLayout == null) throw new RuntimeException("GridLayout not found!");

        playerTurnText = findViewById(R.id.playerTurn);
        if (playerTurnText == null) throw new RuntimeException("playerTurn TextView not found!");

        Button resetButton = findViewById(R.id.resetButton);
        if (resetButton == null) throw new RuntimeException("resetButton not found!");

        // Create game board
        createGameBoard();
        resetButton.setOnClickListener(v -> resetGame());
    }

    private void createGameBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button(this);
                GridLayout.Spec rowSpec = GridLayout.spec(i, 1f);
                GridLayout.Spec colSpec = GridLayout.spec(j, 1f);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.width = 0;
                params.height = 0;
                params.setMargins(2, 2, 2, 2);

                button.setLayoutParams(params);
                button.setTextSize(40);
                button.setTextColor(COLOR_TEXT_WHITE);
                button.setBackgroundResource(R.drawable.cell_border);
                button.setTag(new int[]{i, j});
                button.setOnClickListener(this::onCellClick);
                gridLayout.addView(button);
            }
        }
    }

    private void onCellClick(View view) {
        if (!(view instanceof Button)) return;

        Button button = (Button) view;
        int[] position = (int[]) button.getTag();
        if (position == null || position.length < 2) return;

        int row = position[0];
        int col = position[1];

        if (row < 0 || row >= 3 || col < 0 || col >= 3 || boardState[row][col] != 0) {
            return;
        }

        if (playerXTurn) {
            button.setText("X");
            button.setTextColor(COLOR_X);
            boardState[row][col] = 1;
        } else {
            button.setText("O");
            button.setTextColor(COLOR_O);
            boardState[row][col] = 2;
        }

        if (checkForWinner()) {
            String winner = playerXTurn ? "X" : "O";
            Toast.makeText(this, "Player " + winner + " wins!", Toast.LENGTH_SHORT).show();
            disableAllButtons();
            return;
        }

        if (isBoardFull()) {
            Toast.makeText(this, "It's a draw!", Toast.LENGTH_SHORT).show();
            return;
        }

        playerXTurn = !playerXTurn;
        updatePlayerTurnText();
    }

    private boolean checkForWinner() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (boardState[i][0] != 0 &&
                    boardState[i][0] == boardState[i][1] &&
                    boardState[i][0] == boardState[i][2]) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (boardState[0][j] != 0 &&
                    boardState[0][j] == boardState[1][j] &&
                    boardState[0][j] == boardState[2][j]) {
                return true;
            }
        }

        // Check diagonals
        if (boardState[0][0] != 0 &&
                boardState[0][0] == boardState[1][1] &&
                boardState[0][0] == boardState[2][2]) {
            return true;
        }
        if (boardState[0][2] != 0 &&
                boardState[0][2] == boardState[1][1] &&
                boardState[0][2] == boardState[2][0]) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardState[i][j] == 0) return false;
            }
        }
        return true;
    }

    private void disableAllButtons() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof Button) {
                child.setEnabled(false);
            }
        }
    }

    private void updatePlayerTurnText() {
        playerTurnText.setText(playerXTurn ? "Player X's Turn" : "Player O's Turn");
    }

    private void resetGame() {
        // Clear board state
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardState[i][j] = 0;
            }
        }

        // Reset buttons
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                button.setText("");
                button.setEnabled(true);
            }
        }

        // Reset game state
        playerXTurn = true;
        updatePlayerTurnText();
    }
}