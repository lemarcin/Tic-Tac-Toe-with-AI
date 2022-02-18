package tictactoe;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] player = new String[2];
        do {
            char[][] board = enterCells().clone();
            System.out.print("Input command: ");
            String input = scanner.nextLine();
            if (input.matches("(start\\s(user|easy|medium|hard)\\s(user|easy|medium|hard))|exit")) {
                if (input.matches("exit")){
                    break;
                } else {
                    player[1] = input.split("\\s")[1];
                    player[0] = input.split("\\s")[2];
                    execute(board, player);
                }
            } else {
                System.out.println("Bad parameters!");
            }
        }while (true);
    }

    public static void execute(char[][] board, String[] player) {
        print(board);
        char[] nextMove = {'O', 'X'};
        boolean gameIsOn = true;
        int i = 1;
        do {
            switch (player[i]) {
                case "user" :
                    enterCoordinates(board, nextMove[i]);
                    break;
                case "easy" :
                    System.out.println("Making move level \"easy\"");
                    easyAI(board, nextMove[i]);
                    break;
                case "medium" :
                    System.out.println("Making move level \"medium\"");
                    mediumAI(board, nextMove[i]);
                    break;
                case "hard" :
                    System.out.println("Making move level \"hard\"");
                    hardAI(board, nextMove[i]);
                    break;
            }
            print(board);
            String stateOfGame = stateOfGame(board);
            switch (stateOfGame) {
                case "X wins" :
                case "O wins" :
                case "Draw" :
                    System.out.println(stateOfGame);
                    gameIsOn = false;
                    break;
                default:
                    i = i == 1 ? 0 : 1;
                    break;
            }
        } while (gameIsOn);
    }

    public static void easyAI(char[][] board, char nextMove) {
        int i, j;
        do {
            Random random = new Random();
            i = random.nextInt(3);
            j = random.nextInt(3);
        } while(board[i][j] != '_');
        board[i][j] = nextMove;
    }

    public static void mediumAI(char[][] board, char nextMove) {
        char lastMove = nextMove == 'X' ? 'O' : 'X';
        if (twoInRow(board, nextMove)) {
            thirdInRow(board, nextMove, nextMove);
        } else if (twoInRow(board, lastMove)) {
            thirdInRow(board, lastMove, nextMove);
        } else {
            easyAI(board, nextMove);
        }
    }

    public static void hardAI(char[][] board, char nextMove) {
        int bestScore = Integer.MIN_VALUE;
        int[] move = {0, 0};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '_') {
                    board[i][j] = nextMove;
                    int score = minimax(board, 0, false, nextMove);
                    board[i][j] = '_';
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        board[move[0]][move[1]] = nextMove;
    }

    static int minimax(char[][] board, int depth, boolean isMaximizing, char nextMove) {
        String result = stateOfGame(board);
        switch (result) {
            case "X wins":
                return nextMove == 'X' ? 10 : -10;
            case "O wins":
                return nextMove == 'X' ? -10 : 10;
            case "Draw":
                return 0;
        }
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '_') {
                        board[i][j] = nextMove;
                        int score = minimax(board, depth + 1, false, nextMove);
                        board[i][j] = '_';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '_') {
                        board[i][j] = nextMove == 'X' ? 'O' : 'X';;
                        int score = minimax(board, depth + 1, true, nextMove);
                        board[i][j] = '_';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    public static boolean twoInRow(char[][] board, char nextMove) {
        boolean two = false;;
        int sum = nextMove * 2 + '_';
        for (int i = 0; i < board.length; i++) {
            int row = 0;
            int col = 0;
            int lDiag = 0;
            int rDiag = 0;
            for (int j = 0; j < board[i].length; j++) {
                row += board[i][j];
                col += board[j][i];
                lDiag += board[j][j];
                rDiag += board[j][2 - j];
            }
            two = two || row == sum || col == sum || lDiag == sum || rDiag == sum;
        }
        return two;
    }

    public static void thirdInRow(char[][] board, char lastMove, char nextMove) {
        int sum = lastMove * 2 + '_';
        int row, col, lDiag, rDiag;
        for (int i = 0; i < board.length; i++) {
            row = 0;
            col = 0;
            lDiag = 0;
            rDiag = 0;
            int[] xy = new int[8];
            for (int j = 0; j < board[i].length; j++) {
                row += board[i][j];
                xy[0] = board[i][j] == '_' ? i : xy[0];
                xy[1] = board[i][j] == '_' ? j : xy[1];
                col += board[j][i];
                xy[2] = board[j][i] == '_' ? j : xy[2];
                xy[3] = board[j][i] == '_' ? i : xy[3];
                lDiag += board[j][j];
                xy[4] = board[j][j] == '_' ? j : xy[4];
                xy[5] = board[j][j] == '_' ? j : xy[5];
                rDiag += board[j][2 - j];
                xy[6] = board[j][2 - j] == '_' ? j : xy[6];
                xy[7] = board[j][2 - j] == '_' ? 2 - j : xy[7];
            }
            if (row == sum) {
                board[xy[0]][xy[1]] = nextMove;
                break;
            } else if (col == sum) {
                board[xy[2]][xy[3]] = nextMove;
                break;
            } else if (lDiag == sum) {
                board[xy[4]][xy[5]] = nextMove;
                break;
            } else if (rDiag == sum) {
                board[xy[6]][xy[7]] = nextMove;
                break;
            }
        }
    }

    public static char[][] enterCells() {
        final int n = 3;
        char[][] board = new char[n][n];
        for (int i = 0, k = 0; i < n; i++) {
            for (int j = 0; j < n; j++, k++) {
                board[i][j] = '_';
            }
        }
        return board;
    }

    public static void print(char[][] board) {
        int n = 3;
        System.out.println("---------");
        for (int i = 0; i < n; i++) {
            System.out.print("| ");
            for (int j = 0; j < n; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    public static void enterCoordinates(char[][] board, char nextMove) {
        do {
            System.out.print("Enter the coordinates: ");
            String str = new Scanner(System.in).nextLine();
            int i, j;
            if (str.matches("[123] [123]")) {
                i = Integer.parseInt(str.split(" ")[0]) - 1;
                j = Integer.parseInt(str.split(" ")[1]) - 1;
                if (board[i][j] == 'X' || board[i][j] == 'O') {
                    System.out.println("This cell is occupied! Choose another one!");
                } else {
                    board[i][j] = nextMove;
                    break;
                }
            } else if (str.matches("\\d+ \\d+")) {
                System.out.println("Coordinates should be from 1 to 3!");
            } else {
                System.out.println("You should enter numbers!");
            }
        } while (true);
    }

    public static String stateOfGame(char[][] board) {
        boolean xWin = false;
        boolean oWin = false;
        int xs = 0;
        int os = 0;
        for (int i = 0; i < board.length; i++) {
            int row = 0;
            int col = 0;
            int lDiag = 0;
            int rDiag = 0;
            for (int j = 0; j < board[i].length; j++) {
                xs += board[i][j] == 'X' ? 1 : 0;
                os += board[i][j] == 'O' ? 1 : 0;
                row += board[i][j];
                col += board[j][i];
                lDiag += board[j][j];
                rDiag += board[j][2 - j];
            }
            xWin = xWin || row == 264 || col == 264 || lDiag == 264 || rDiag == 264;
            oWin = oWin || row == 237 || col == 237 || lDiag == 237 || rDiag == 237;
        }
        return xWin ? "X wins" : oWin ? "O wins" : xs + os == 9 ? "Draw": "Game not finished";
    }
}