import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame {
    private char currentPlayer = 'X'; // Current player, 'X' or 'O'
    private JButton[] buttons = new JButton[9]; // Array of buttons representing the game board
    private char[][] board = new char[3][3]; // 2D array representing the game board

    public TicTacToe() {
        setLayout(new GridLayout(3, 3)); // Set the layout of the JFrame to a 3x3 grid
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Set the default close operation
        setSize(300, 300); // Set the size of the JFrame
        setLocationRelativeTo(null); // Center the JFrame on the screen

        // Create and configure the buttons
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setText("");
            buttons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton buttonClicked = (JButton) e.getSource();
                    buttonClicked.setText(String.valueOf(currentPlayer));
                    buttonClicked.setEnabled(false);
                    int index = Integer.parseInt(buttonClicked.getActionCommand());
                    board[index / 3][index % 3] = currentPlayer;

                    // Check if the current player has won
                    if (checkWin()) {
                        JOptionPane.showConfirmDialog(null, "Player " + currentPlayer + " wins!", "Winner",
                                JOptionPane.DEFAULT_OPTION);
                        resetGame();
                    }
                    // Check if the game is a draw
                    else if (isBoardFull()) {
                        JOptionPane.showConfirmDialog(null, "Draw!", "Draw", JOptionPane.DEFAULT_OPTION);
                        resetGame();
                    }
                    // Switch to the next player
                    else {
                        switchPlayer();
                        // If the current player is 'O', make a move using the minimax algorithm
                        if (currentPlayer == 'O') {
                            int[] move = findBestMove();
                            board[move[0]][move[1]] = currentPlayer;
                            buttons[move[0] * 3 + move[1]].setText(String.valueOf(currentPlayer));
                            buttons[move[0] * 3 + move[1]].setEnabled(false);

                            // Check if the current player has won after making a move
                            if (checkWin()) {
                                JOptionPane.showConfirmDialog(null, "Player " + currentPlayer + " wins!", "Winner",
                                        JOptionPane.DEFAULT_OPTION);
                                resetGame();
                            }
                            // Check if the game is a draw after making a move
                            else if (isBoardFull()) {
                                JOptionPane.showConfirmDialog(null, "Draw!", "Draw", JOptionPane.DEFAULT_OPTION);
                                resetGame();
                            }
                            // Switch to the next player
                            switchPlayer();
                        }
                    }
                }
            });
            buttons[i].setActionCommand(String.valueOf(i));
            add(buttons[i]);
        }

        setVisible(true); // Make the JFrame visible
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; // Switch the current player between 'X' and 'O'
    }

    private boolean checkWin() {
        // Check rows for a win
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0' && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return true;
            }
        }

        // Check columns for a win
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != '\0' && board[0][j] == board[1][j] && board[0][j] == board[2][j]) {
                return true;
            }
        }

        // Check diagonals for a win
        if (board[0][0] != '\0' && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            return true;
        }
        if (board[0][2] != '\0' && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            return true;
        }

        return false; // No win
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
        return true; // Board is full
    }

    private int[] findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] move = new int[2];
        // Find the best move using the minimax algorithm
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    board[i][j] = 'O';
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
        return move; // Return the best move
    }

    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        // Base cases: check if the game is over
        if (checkWin()) {
            return isMaximizing ? -1 : 1; // If maximizing player wins, return -1; if minimizing player wins, return 1
        } else if (isBoardFull()) {
            return 0; // Game is a draw
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            // Maximizing player's turn
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = 'O';
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = '\0';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore; // Return the best score for the maximizing player
        } else {
            int bestScore = Integer.MAX_VALUE;
            // Minimizing player's turn
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = 'X';
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = '\0';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore; // Return the best score for the minimizing player
        }
    }

    private void resetGame() {
        // Reset the game by clearing the board and enabling all buttons
        currentPlayer = 'X';
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");
            buttons[i].setEnabled(true);
        }
        board = new char[3][3];
    }

    public static void main(String[] args) {
        new TicTacToe(); // Create an instance of the TicTacToe class
    }
}