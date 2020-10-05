/*---------------------------------------------------------------------------------------------------------
 *  By Irene Storozhko. August 11, 2018. Week 1 assignment for Algorithms I course by Wayne and Sedgewick.
 *
 *  Percolation data type to model a percolation system.
 *  IMPORTANT: Coordinates: arow, acol belong to [0; n - 1] - these are for the internal use;
 *                          row, col belong to [1, n] - these for the exposed API.
 *--------------------------------------------------------------------------------------------------------*/
public class Percolation {
    private boolean[][] grid; // Models the grid
    private final WeightedQuickUnionUF percolationTree; // Models the percolation of the system
    private final WeightedQuickUnionUF fullnessTree;
    private final int virtualTop; // Supposedly, helps quickly determine whether the system percolates
    private final int virtualBottom; // Supposedly, helps quickly determine whether the system percolates
    private int numberOfOpen; // Number of open sites in the system

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive integer greater than 0");

        this.grid = new boolean[n][n];

        this.numberOfOpen = 0;

        this.percolationTree = new WeightedQuickUnionUF(n * n + 2);
        this.virtualTop = n * n;
        this.virtualBottom = n * n + 1;

        this.fullnessTree = new WeightedQuickUnionUF(n * n + 1);

        for (int i = 0; i < n; i++) {
            this.percolationTree.union(i, this.virtualTop);
            this.fullnessTree.union(i, this.virtualTop);
            this.percolationTree.union(this.treeCoordinate(n - 1, i), this.virtualBottom);
        }
    }

    private int treeCoordinate(int arow, int acol) {
        return arow * grid.length + acol;
    }

    private void assertWithinGrid(int acoord) {
        if (acoord < 0 || acoord > grid.length - 1) {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    public void open(int row, int col) {
        if (isOpen(row, col)) return;

        int arow = row - 1;
        int acol = col - 1;
        int n = grid.length;
        assertWithinGrid(arow);
        assertWithinGrid(acol);

        grid[arow][acol] = true;
        numberOfOpen += 1;

        int currentSite = treeCoordinate(arow, acol);

        for (int i = Math.max(0, arow - 1); i <= Math.min(n - 1, arow + 1); i++) {
            if (isOpen(i + 1, col)) {
                percolationTree.union(treeCoordinate(i, acol), currentSite);
                fullnessTree.union(treeCoordinate(i, acol), currentSite);
            }
        }
        for (int j = Math.max(0, acol - 1); j <= Math.min(n - 1, acol + 1); j++) {
            if (isOpen(row, j + 1)) {
                percolationTree.union(treeCoordinate(arow, j), currentSite);
                fullnessTree.union(treeCoordinate(arow, j), currentSite);
            }
        }
    }

    public boolean isOpen(int row, int col) { // C
        int arow = row - 1;
        int acol = col - 1;
        assertWithinGrid(arow);
        assertWithinGrid(acol);
        return grid[arow][acol];
    }

    public boolean isFull(int row, int col) {
        int arow = row - 1;
        int acol = col - 1;
        assertWithinGrid(arow);
        assertWithinGrid(acol);

        if (!isOpen(row, col)) return false;

        int currentPoint = treeCoordinate(arow, acol);
        return fullnessTree.connected(currentPoint, virtualTop);
    }

    public int numberOfOpenSites() {
        return numberOfOpen;
    }

    public boolean percolates() {

        if (grid.length == 1) return grid[0][0];
        return percolationTree.connected(virtualTop, virtualBottom);
    }

    private static class WeightedQuickUnionUF {
        private final int[] id;
        private int components;
        private final int[] sz;

        public WeightedQuickUnionUF(int n) {
            id = new int[n];
            sz = new int[n];
            for (int i = 0; i < n; i++) {
                id[i] = i;
                sz[i] = 1;
            }
            components = n;
        }

        public void union(int p, int q) {
            int pid = find(p);
            int qid = find(q);
            if (pid == qid) return;

            if (sz[pid] < sz[qid]) {
                id[pid] = qid;
                sz[qid] += sz[pid];
            } else {
                id[qid] = pid;
                sz[pid] += sz[qid];
            }

            if (components > 1) components--;
        }

        public int count() {
            return components;
        }

        public int find(int p) {
            while (p != id[p]) {
                p = id[p];
            }
            return p;
        }

        public boolean connected(int p, int q) {
            return find(p) == find(q);
        };
    }
}
