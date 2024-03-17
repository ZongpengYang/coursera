import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
    private int size;
    private int topIndex;
    private int botIndex;
    private boolean[] state;
    private WeightedQuickUnionUF grid;
    private int openStates = 0;

    private void checkLimit(int i, int j) {
        if (i <= 0 || i > size || j <= 0 || j > size) throw new IllegalArgumentException();
    }

    private int indexToPosition(int i, int j) {
        int position = size * (i - 1) + j - 1;
        return position;
    }
    public Percolation(int n) {
        size = n;
        int elements = size * size;
        state = new boolean[elements];
        topIndex = elements;
        botIndex = elements + 1;
        grid = new WeightedQuickUnionUF(elements + 2);
    }

    public void open(int i, int j) {
        checkLimit(i, j);
        if (isOpen(i, j)) return;
        openStates++;

        int currentPos = indexToPosition(i, j);
        this.state[currentPos] = true;

        // union with top indicating element
        if (i == 1 && !grid.connected(currentPos, topIndex)) {
            grid.union(currentPos, topIndex);
        }
        // union with bottom indicating element
        if (i == size) grid.union(currentPos, botIndex);
        // union with bottom cell
        if (i < size && isOpen(i+1, j)) grid.union(currentPos, indexToPosition(i+1, j));
        // union with upper cell
        if (i > 1 && isOpen(i-1, j)) grid.union(currentPos, indexToPosition(i-1, j));
        // union with left cell
        if (j > 1 && isOpen(i, j-1)) grid.union(currentPos, indexToPosition(i, j-1));
        // union with left cell
        if (j < size && isOpen(i, j+1)) grid.union(currentPos, indexToPosition(i, j+1));
    }

    public boolean isOpen(int i, int j) {
        checkLimit(i, j);
        return state[indexToPosition(i, j)];
    }

    public boolean isFull(int i, int j) {
        checkLimit(i, j);
        if (!isOpen(i, j))
            return false;
        int currentSite = indexToPosition(i, j);
        if (grid.connected(topIndex, currentSite))
            return true;
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return openStates;
    }

    public boolean percolates() {
        if (grid.connected(topIndex, botIndex))
            return true;
        return false;
    }
}