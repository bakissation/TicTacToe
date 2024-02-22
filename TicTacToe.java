import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame {
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';

    private char currentPlayer = PLAYER_X;
    private JButton[] buttons = new JButton[9];
    private char[][] board = new char[3][3];

    public TicTacToe() {
        // Set up the JFrame
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);

        // Create and add buttons to the JFrame
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setText("");
            buttons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton buttonClicked = (JButton) e.getSource();
                    int index = Integer.parseInt(buttonClicked.getActionCommand());
                    makeMove(index / 3, index % 3, buttonClicked);
                }
            });
            buttons[i].setActionCommand(String.valueOf(i));
            add(buttons[i]);
        }

        setVisible(true);
    }

    private void makeMove(int row, int col, JButton button) {
        // Update the button text and disable it
        button.setText(String.valueOf(currentPlayer));
        button.setEnabled(false);
        // Update the board with the current player's move
        board[row][col] = currentPlayer;

        if (checkWin()) {
            // If there is a win, end the game
            endGame("Player " + currentPlayer + " wins!");
        } else if (isBoardFull()) {
            // If the board is full and there is no win, end the game with a draw
            endGame("Draw!");
        } else {
            // Switch to the other player's turn
            switchPlayer();
            if (currentPlayer == PLAYER_O) {
                // If it's the computer's turn, find the best move and make it
                int[] move = findBestMove();
                makeMove(move[0], move[1], buttons[move[0] * 3 + move[1]]);
            }
        }
    }

    private void switchPlayer() {
        // Switch the current player
        currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    private boolean checkWin() {
        // Check rows for a win
        for (int i = 0; i < 3; i++) {
            if (checkLine(board[i][0], board[i][1], board[i][2])) {
                return true;
            }
        }

        // Check columns for a win
        for (int j = 0; j < 3; j++) {
            if (checkLine(board[0][j], board[1][j], board[2][j])) {
                return true;
            }
        }

        // Check diagonals for a win
        return checkLine(board[0][0], board[1][1], board[2][2]) || checkLine(board[0][2], board[1][1], board[2][0]);
    }

    private boolean checkLine(char c1, char c2, char c3) {
        // Check if all characters in the line are the same and not empty
        return (c1 != '\0' && c1 == c2 && c1 == c3);
    }

    private boolean isBoardFull() {
        // Check if the board is full
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

    private int[] findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] move = new int[2];
        // Iterate through all possible moves and find the one with the highest score
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    board[i][j] = PLAYER_O;
                    int score = minimax(board, 0, false);
                    board[i][j] = '\0';
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        return move;
    }

    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        // Base cases: check for win or draw
        if (checkWin()) {
            return isMaximizing ? -1 : 1;
        } else if (isBoardFull()) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            // Iterate through all possible moves and find the one with the highest score
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = PLAYER_O;
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = '\0';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            // Iterate through all possible moves and find the one with the lowest score
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = PLAYER_X;
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = '\0';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private void endGame(String message) {
        // Show a dialog with the game result and reset the game
        JOptionPane.showConfirmDialog(null, message, "Game Over", JOptionPane.DEFAULT_OPTION);
        resetGame();
    }

    private void resetGame() {
        // Reset the game state
        currentPlayer = PLAYER_X;
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");
            buttons[i].setEnabled(true);
        }
        board = new char[3][3];
    }

    public static void main(String[] args) {
        // Start the game
        new TicTacToe();
    }
}