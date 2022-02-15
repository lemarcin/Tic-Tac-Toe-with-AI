package tictactoe;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] player = new String[2];
        do {
            char[][] grid = enterCells().clone();
            System.out.print("Input command: ");
            String input = scanner.nextLine();
            if (input.matches("(start\\s(user|easy|medium)\\s(user|easy|medium))|exit")) {
                if (input.matches("exit")){
                    break;
                } else {
                    player[1] = input.split("\\s")[1];
                    player[0] = input.split("\\s")[2];
                    execute(grid, player);
                }
            } else {
                System.out.println("Bad parameters!");
            }
        }while (true);
    }

    public static void execute(char[][] grid, String[] player) {
        char[] nextMove = {'O', 'X'};
        print(grid);
        boolean gameIsOn = true;
        int i = 1;
        do {
            switch (player[i]) {
                case "user" :
                    enterCoordinates(grid, nextMove[i]);
                    break;
                case "easy" :
                    System.out.println("Making move level \"easy\"");
                    easyAI(grid, nextMove[i]);
                    break;
                case "medium" :
                    System.out.println("Making move level \"medium\"");
                    mediumAI(grid, nextMove[i]);
                    break;
            }
            print(grid);
            String stateOfGame = stateOfGame(grid);
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

    public static void easyAI(char[][] grid, char nextMove) {
        int i = 0;
        int j = 0;
        do {
            Random random = new Random();
            i = random.nextInt(3);
            j = random.nextInt(3);
        } while(grid[i][j] != '_');
        grid[i][j] = nextMove;
    }

    public static void mediumAI(char[][] grid, char nextMove) {
        int i = 0;
        int j = 0;
        char lastMove = nextMove == 'X' ? 'O' : 'X';
        if (twoInRow(grid, nextMove)) {
            thirdInRow(grid, nextMove, nextMove);
        } else if (twoInRow(grid, lastMove)) {
            thirdInRow(grid, lastMove, nextMove);
        } else {
            easyAI(grid, nextMove);
        }
    }

    public static boolean twoInRow(char[][] grid, char nextMove) {
        boolean two = false;;
        int sum = nextMove * 2 + '_';
        for (int i = 0; i < grid.length; i++) {
            int row = 0;
            int col = 0;
            int lDiag = 0;
            int rDiag = 0;
            for (int j = 0; j < grid[i].length; j++) {
                row += grid[i][j];
                col += grid[j][i];
                lDiag += grid[j][j];
                rDiag += grid[j][2 - j];
            }
            two = two || row == sum || col == sum || lDiag == sum || rDiag == sum;
        }
        return two;
    }

    public static void thirdInRow(char[][] grid, char lastMove, char nextMove) {
        int sum = lastMove * 2 + '_';
        int row, col, lDiag, rDiag;
        for (int i = 0; i < grid.length; i++) {
            row = 0;
            col = 0;
            lDiag = 0;
            rDiag = 0;
            int[] xy = new int[8];
            for (int j = 0; j < grid[i].length; j++) {
                row += grid[i][j];
                xy[0] = grid[i][j] == '_' ? i : xy[0];
                xy[1] = grid[i][j] == '_' ? j : xy[1];
                col += grid[j][i];
                xy[2] = grid[j][i] == '_' ? j : xy[2];
                xy[3] = grid[j][i] == '_' ? i : xy[3];
                lDiag += grid[j][j];
                xy[4] = grid[j][j] == '_' ? j : xy[4];
                xy[5] = grid[j][j] == '_' ? j : xy[5];
                rDiag += grid[j][2 - j];
                xy[6] = grid[j][2 - j] == '_' ? j : xy[6];
                xy[7] = grid[j][2 - j] == '_' ? 2 - j : xy[7];
            }
            if (row == sum) {
                grid[xy[0]][xy[1]] = nextMove;
                break;
            } else if (col == sum) {
                grid[xy[2]][xy[3]] = nextMove;
                break;
            } else if (lDiag == sum) {
                grid[xy[4]][xy[5]] = nextMove;
                break;
            } else if (rDiag == sum) {
                grid[xy[6]][xy[7]] = nextMove;
                break;
            }
        }
    }

    public static char[][] enterCells() {
        final int n = 3;
        char[][] grid = new char[n][n];
        for (int i = 0, k = 0; i < n; i++) {
            for (int j = 0; j < n; j++, k++) {
                grid[i][j] = '_';
            }
        }
        return grid;
    }

    public static void print(char[][] grid) {
        int n = 3;
        System.out.println("---------");
        for (int i = 0; i < n; i++) {
            System.out.print("| ");
            for (int j = 0; j < n; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    public static void enterCoordinates(char[][] grid, char nextMove) {
        do {

            System.out.print("Enter the coordinates: ");
            String str = new Scanner(System.in).nextLine();
            int x = 0;
            int y = 0;
            if (str.matches("[123] [123]")) {
                y = Integer.parseInt(str.split(" ")[0]) - 1;
                x = Integer.parseInt(str.split(" ")[1]) - 1;
                if (grid[y][x] == 'X' || grid[y][x] == 'O') {
                    System.out.println("This cell is occupied! Choose another one!");
                } else {
                    grid[y][x] = nextMove;
                    break;
                }
            } else if (str.matches("\\d+ \\d+")) {
                System.out.println("Coordinates should be from 1 to 3!");
            } else {
                System.out.println("You should enter numbers!");
            }
        } while (true);
    }

    public static String stateOfGame(char[][] grid) {
        boolean xWin = false;
        boolean oWin = false;
        int xs = 0;
        int os = 0;
        for (int i = 0; i < grid.length; i++) {
            int row = 0;
            int col = 0;
            int lDiag = 0;
            int rDiag = 0;
            for (int j = 0; j < grid[i].length; j++) {
                xs += grid[i][j] == 'X' ? 1 : 0;
                os += grid[i][j] == 'O' ? 1 : 0;
                row += grid[i][j];
                col += grid[j][i];
                lDiag += grid[j][j];
                rDiag += grid[j][2 - j];
            }
            xWin = xWin || row == 264 || col == 264 || lDiag == 264 || rDiag == 264;
            oWin = oWin || row == 237 || col == 237 || lDiag == 237 || rDiag == 237;
        }
        return xWin ? "X wins" : oWin ? "O wins" : xs + os == 9 ? "Draw": "Game not finished";
    }
}