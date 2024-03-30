import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public class Solver {

    private final boolean isSolvable;
    private LinkedList<Board> solution;
    private Node goalNode;
    private int moves;

    private class Node {
        Board board;
        int moves;
        Node prev;
        int manhattan;
        boolean isGoal;

        public Node( Board b, Node n){
            this.board = b;
            if ( n != null ){
                this.prev = n;
                this.moves = prev.moves + 1;
            } else {
                this.prev = null;
                this.moves = 0;
            }
            this.isGoal = board.isGoal();
            this.manhattan = board.manhattan() + this.moves;
        }

        public int manhattan(){
            return this.manhattan;
        }

        public boolean isGoal(){
            return this.isGoal;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){

        if ( initial == null ) throw new IllegalArgumentException("Null board passed into Solver!");

        // use twin board to determine if a board is solvable
        MinPQ<Node> original = new MinPQ<>( Comparator.comparingInt( Node::manhattan ) );
        original.insert( new Node( initial, null) );
        MinPQ<Node> twin = new MinPQ<>( Comparator.comparingInt( Node::manhattan ) );
        twin.insert( new Node( initial.twin(), null) );

        while ( true ) {
            Node originalNode = original.delMin();
            Node twinNode = twin.delMin();
            if ( originalNode.isGoal() ){
                isSolvable = true;
                goalNode = originalNode;
                break;
            }
            if ( twinNode.isGoal() ){
                isSolvable = false;
                break;
            }
            for ( Board b : originalNode.board.neighbors() ){
                if ( originalNode.prev != null && b.equals( originalNode.prev.board ) ) continue;
                original.insert( new Node( b, originalNode) );
            }
            for ( Board b : twinNode.board.neighbors() ){
                if ( twinNode.prev != null && b.equals( twinNode.prev.board ) ) continue;
                twin.insert( new Node( b, twinNode) );
            }
        }
        if ( isSolvable ){
            solution = new LinkedList<>();
            Node temp = goalNode;
            for ( int i = goalNode.moves; i >= 0; i--){
                solution.addFirst( temp.board );
                temp = temp.prev;
            }
            moves = goalNode.moves;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
        if ( isSolvable ) return moves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution(){
        if ( !isSolvable ) return null;
        return solution;
    }

    // test client (see below)
    public static void main(String[] args){

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
