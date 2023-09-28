package com.example.minesweeper_project1;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class Board {
    int[][] mines = new int[12][10];
    boolean[][] revealed = new boolean[12][10];
    boolean[][] flagged = new boolean[12][10];
    Random rand = new Random();

    public Board(){
        // set everything to 0/false
        for(int i = 0; i < 12; i++) {
            for (int j = 0; j < 10; j++) {
                mines[i][j] = 0;
                revealed[i][j] = false;
                flagged[i][j] = false;
            }
        }

        //assigns bombs at random
        randBombs();
    }

    public void randBombs(){
        for(int i= 0; i < 4; i++){
            int x_bomb = rand.nextInt(12);
            int y_bomb = rand.nextInt(10);
            mines[x_bomb][y_bomb] = -1;

            //# 1 top left | in bounds
            if(checkBounds(x_bomb - 1, y_bomb - 1)){
                // if our square is a bomb, set that count to 1
                    mines[x_bomb - 1][y_bomb -1] += 1;
            }

            //# 2 top middle | in bounds
            if(checkBounds(x_bomb - 1, y_bomb)){
                // if our square is a bomb, set that count to 1
                mines[x_bomb - 1][y_bomb] += 1;
            }

            //# 3 top right | in bounds
            if(checkBounds(x_bomb - 1, y_bomb + 1)){
                // if our square is a bomb, set that count to 1
                mines[x_bomb - 1][y_bomb + 1] += 1;
            }

            //# 4 left | in bounds
            if(checkBounds(x_bomb , y_bomb - 1)){
                // if our square is a bomb, set that count to 1
                mines[x_bomb][y_bomb - 1] += 1;
            }

            //# 5 right | in bounds
            if(checkBounds(x_bomb , y_bomb + 1)){
                // if our square is a bomb, set that count to 1
                mines[x_bomb][y_bomb + 1] += 1;
            }

            //# 6 bottom-left | in bounds
            if(checkBounds(x_bomb + 1 , y_bomb -1)){
                // if our square is a bomb, set that count to 1
                mines[x_bomb + 1][y_bomb - 1] += 1;
            }

            //# 7 bottom | in bounds
            if(checkBounds(x_bomb + 1 , y_bomb)){
                // if our square is a bomb, set that count to 1
                mines[x_bomb + 1][y_bomb] += 1;
            }

            //# 8 bottom-right | in bounds
            if(checkBounds(x_bomb + 1 , y_bomb + 1)){
                // if our square is a bomb, set that count to 1
                mines[x_bomb + 1][y_bomb + 1] += 1;
            }

        }
    }

    public boolean checkBounds(int row, int col){

        // Check if i and j are within the bounds of the array AND not a mine
        if(row >= 0 && row < 12 && col >= 0 && col < 10 && mines[row][col] != -1) {
            return true;
        } else {
            return false;
        }

    }

    public void revealADJ(int i, int j) {
        // Define the directions (up, down, left, right)
        int[][] DIRECTIONS = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        int[] start = new int[2];
        start[0] = i;
        start[1] = j;

//        boolean[][] visited = new boolean[12][10];

        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(start);
        revealed[start[0]][start[1]] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

            // Process the current cell (you can add your own logic here)
            System.out.println("Visiting cell (" + row + ", " + col + ")");

            // Explore adjacent cells
            for (int[] direction : DIRECTIONS) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                // Check if the adjacent cell is within bounds and not visited
                if(checkBounds(newRow, newCol) && !revealed[newRow][newCol]) {
                    // Add the adjacent cell to the queue and mark it as visited
                    if (mines[newRow][newCol] == 0)
                        queue.offer(new int[]{newRow, newCol});
                    revealed[newRow][newCol] = true;
                }
            }
        }

    }







}
