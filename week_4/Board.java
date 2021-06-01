import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private final int[][] tiles;

    private int hamming;
    private int manhattan;
    private boolean isGoal;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException();
        this.tiles = copyBoard(tiles);

        isGoal = true;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (isGoal && tiles[i][j] != getThere(i, j))
                    isGoal = false;
                if (tiles[i][j] == 0) continue;
                if (tiles[i][j] != getThere(i, j)) hamming++;

                double ratio = (double) tiles[i][j] / tiles.length;
                int rowGoal = (int) Math.ceil(ratio) - 1;

                int colGoal = tiles[i][j] % tiles.length - 1;
                if (colGoal == -1)
                    colGoal = tiles.length - 1;
                manhattan += (Math.abs(rowGoal - i) + Math.abs(colGoal - j));
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tiles.length + "\n");
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++)
                stringBuilder.append(String.format("%2d ", tiles[i][j]));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return isGoal;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        if (this.tiles.length != that.tiles.length)
            return false;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int col = 0;
        int row = 0;
        ArrayList<Board> neighbours = new ArrayList<>();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        isNeighbour(neighbours, row + 1, col, row, col);
        isNeighbour(neighbours, row - 1, col, row, col);
        isNeighbour(neighbours, row, col + 1, row, col);
        isNeighbour(neighbours, row, col - 1, row, col);

        return neighbours;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (tiles.length < 2)
            return new Board(tiles);

        int[][] twin = copyBoard(tiles);
        if (tiles[0][0] != 0 && tiles[0][1] != 0) {
            twin[0][0] = tiles[0][1];
            twin[0][1] = tiles[0][0];
        }
        else {
            twin[1][0] = tiles[1][1];
            twin[1][1] = tiles[1][0];
        }

        return new Board(twin);
    }

    private int getThere(int i, int j) {
        if (i == tiles.length - 1 && j == tiles.length - 1)
            return 0;
        return tiles.length * i + j + 1;
    }

    private void isNeighbour(ArrayList<Board> neighbours, int i, int j, int row, int col) {
        if (i < 0 || i >= tiles.length) return;
        if (j < 0 || j >= tiles.length) return;

        int[][] neighbour = copyBoard(tiles);
        neighbour[row][col] = neighbour[i][j];
        neighbour[i][j] = 0;
        neighbours.add(new Board(neighbour));
    }

    private int[][] copyBoard(int[][] array) {
        int[][] tilesCopy = new int[array.length][array.length];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++)
                tilesCopy[i][j] = array[i][j];
        }
        return tilesCopy;
    }

    public static void main(String[] args) {
        int[][] tiles = { {0, 1, 3},
                          {4, 2, 5},
                          {7, 8, 6} };
        Board board = new Board(tiles);

        StdOut.println("dimension: " + board.dimension());
        StdOut.println("hamming: " + board.hamming());
        StdOut.println("manhattan: " + board.manhattan());
        StdOut.print(board);
    }
}