import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame {
    private char currentPlayer = 'X';
    private JButton[] buttons = new JButton[9];
    private char[][] board = new char[3][3];

    public TicTacToe() {
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);

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
                    if (checkWin()) {
                        JOptionPane.showConfirmDialog(null, "Player " + currentPlayer + " wins!", "Winner",
                                JOptionPane.DEFAULT_OPTION);
                        System.exit(0);
                    } else if (isBoardFull()) {
                        JOptionPane.showConfirmDialog(null, "Draw!", "Draw", JOptionPane.DEFAULT_OPTION);
                        System.exit(0);
                    } else {
                        switchPlayer();
                        if (currentPlayer == 'O') {
                            int[] move = findBestMove();
                            board[move[0]][move[1]] = currentPlayer;
                            buttons[move[0] * 3 + move[1]].setText(String.valueOf(currentPlayer));
                            buttons[move[0] * 3 + move[1]].setEnabled(false);
                            if (checkWin()) {
                                JOptionPane.showConfirmDialog(null, "Player " + currentPlayer + " wins!", "Winner",
                                        JOptionPane.DEFAULT_OPTION);
                                System.exit(0);
                            } else if (isBoardFull()) {
                                JOptionPane.showConfirmDialog(null, "Draw!", "Draw", JOptionPane.DEFAULT_OPTION);
                                System.exit(0);
                            }
                            switchPlayer();
                        }
                    }
                }
            });
            buttons[i].setActionCommand(String.valueOf(i));
            add(buttons[i]);
        }

        setVisible(true);
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0' && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (board[0][j] != '\0' && board[0][j] == board[1][j] && board[0][j] == board[2][j]) {
                return true;
            }
        }

        if (board[0][0] != '\0' && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            return true;
        }
        if (board[0][2] != '\0' && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
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
        return move;
    }

    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        if (checkWin()) {
            return isMaximizing ? -1 : 1;
        } else if (isBoardFull()) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
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
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
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
            return bestScore;
        }
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}