package tictactoe;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        char[][] grid = enterCells().clone();
        print(grid);
        String[] player = { "easy", "user"};
        char[] nextMove = {'O', 'X'};
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