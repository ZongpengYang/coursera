import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] tiles;
    private final int size;
    private final int hammingP;
    private final int manhattanP;
    private int zeroRow;
    private int zeroCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles){
        this.tiles = tiles;
        this.size = tiles.length;
        this.hammingP = computeHamming();
        this.manhattanP = computeManhattan();
    }

    // string representation of this board
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(tiles.length); sb.append("\n");
        for ( int[] tileRow : tiles ){
            for ( int tile : tileRow ) {
                sb.append(" " + tile);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension(){
        return size;
    }

    // number of tiles out of place
    public int hamming(){
        return hammingP;
    }

    private int computeHamming(){
        int sum = 0;
        int tile = 0;
        for ( int i = 0; i < size; i++ ){
            for ( int j = 0; j < size; j++ ){
                tile = tiles[i][j];
                if ( tile == 0 ) {
                    zeroRow = i;
                    zeroCol = j;
                    continue;
                }
                if ( ! ( tile == i * size + 1 + j ) ) sum++;
            }
        }
        return sum;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        return manhattanP;
    }

    private int computeManhattan(){
        int sum = 0;
        int tile = 0;
        for ( int i = 0; i < size; i++ ){
            for ( int j = 0; j < size; j++ ){
                tile = tiles[i][j];
                if ( tile != 0 )
                    sum += Math.abs( ( tile - 1 ) / size - i ) + Math.abs( (tile - 1) % size - j );
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal(){
        int tile = 0;
        for ( int i = 0; i < size; i++ ){
            for ( int j = 0; j < size; j++ ){
                tile = tiles[i][j];
                if ( ! ( tile == i * size + 1 + j ) && tile != 0 )
                    return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y){
        if ( y == null ) return false;
        if ( y.getClass() != Board.class ) return false;
        Board castedY = (Board) y;
        if ( this.dimension() != castedY.dimension() ) return false;
        // iterables
        for ( int i = 0; i < size; i++ ){
            for ( int j = 0; j < size; j++ ){
                if ( this.tiles[i][j] != castedY.tiles[i][j] )
                    return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors(){
        List<Board> neighbors = new ArrayList<>();
        // switch with top tile
        if ( zeroRow != 0 ) neighbors.add( new Board( switchTiles(tiles, zeroRow, zeroCol, zeroRow - 1, zeroCol)) );
        // switch with left tile
        if ( zeroCol != 0 ) neighbors.add( new Board( switchTiles(tiles, zeroRow, zeroCol, zeroRow, zeroCol - 1)) );
        // switch with bottom tile
        if ( zeroRow != size - 1 ) neighbors.add( new Board( switchTiles(tiles, zeroRow, zeroCol, zeroRow + 1, zeroCol)) );
        // switch with right tile
        if ( zeroCol != size - 1 ) neighbors.add( new Board( switchTiles(tiles, zeroRow, zeroCol, zeroRow, zeroCol + 1)) );

        return neighbors;
    }

    //switch tiles at row1,col1 and row2,col2
    private int[][] switchTiles ( int[][] tiles, int row1, int col1, int row2, int col2 ){
        int[][] newTiles = new int [size][size];
        copyTiles( tiles, newTiles);
        int temp = newTiles[row2][col2];
        newTiles[row2][col2] = newTiles[row1][col1];
        newTiles[row1][col1] = temp;
        return newTiles;
    }

    private void copyTiles( int[][] source, int[][] target ) {
        for ( int i = 0; i < size; i++ ){
            for ( int j = 0; j < size; j++ ){
                target[i][j] = source[i][j];
            }
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        int row1, col1, row2;

        if ( zeroRow > 0 ) row1 = zeroRow - 1;
        else row1 = zeroRow + 1;
        if ( zeroCol > 0  ) col1 = zeroCol - 1;
        else col1 = zeroCol + 1;

        if ( row1 != 0 ) row2 = row1 - 1;
        else row2 = row1 + 1;
        return new Board( switchTiles( tiles, row1, col1, row2, col1 ));
    }

    // unit testing (not graded)
    public static void main(String[] args){

    }

}