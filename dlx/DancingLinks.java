// Author: Rafal Szymanski <me@rafal.io>

// Implementation of the Dancing Links algorithm for exact cover.

package dlx;

import java.util.*;

public class DancingLinks{

    class DancingNode{
        DancingNode L, R, U, D;
        ColumnNode C;
        int rowId;
        String name;

        // hooks node n1 `below` current node
        DancingNode hookDown(DancingNode n1){
            assert (this.C == n1.C);
            n1.D = this.D;
            n1.D.U = n1;
            n1.U = this;
            this.D = n1;
            return n1;
        }

        // hooke a node n1 to the right of `this` node
        DancingNode hookRight(DancingNode n1){
            n1.R = this.R;
            n1.R.L = n1;
            n1.L = this;
            this.R = n1;
            return n1;
        }

        void unlinkLR(){
            this.L.R = this.R;
            this.R.L = this.L;
        }

        void relinkLR(){
            this.L.R = this.R.L = this;
        }

        void unlinkUD(){
            this.U.D = this.D;
            this.D.U = this.U;
        }

        void relinkUD(){
            this.U.D = this.D.U = this;
        }

        public DancingNode(){
            L = R = U = D = this;
        }

        public DancingNode(ColumnNode c, int rowId, String name){
            this();
            C = c;
            this.rowId = rowId;
            this.name = name;
        }
    }

    class ColumnNode extends DancingNode{
        int size; // number of nodes in current column

        public ColumnNode(String n){
            super();
            size = 0;
            name = n;
            C = this;
        }

        void cover(){
            unlinkLR();
            for(DancingNode i = this.D; i != this; i = i.D){
                for(DancingNode j = i.R; j != i; j = j.R){
                    j.unlinkUD();
                    j.C.size--;
                }
            }
            header.size--; // not part of original
        }

        void uncover(){
            for(DancingNode i = this.U; i != this; i = i.U){
                for(DancingNode j = i.L; j != i; j = j.L){
                    j.C.size++;
                    j.relinkUD();
                }
            }
            relinkLR();
            header.size++; // not part of original
        }
    }

    private ColumnNode header;
    private int count = 0;
    private int idLength = 0;
    private boolean useNaiveMethod = false;
    private SolutionHandler handler;
    private List<DancingNode> answer;
    private List<List<Integer>> res;
    private boolean isFirstSolution = true;
    public long firstSolutionEndTime;
    // Heart of the algorithm
    private void search(int k){
        if (this.count == 0){ // all the data columns removed
            handler.handleSolution(answer, res);
            if (isFirstSolution) {
                this.firstSolutionEndTime = System.currentTimeMillis();
                isFirstSolution = false;
            }
        } else{
            ColumnNode c;
            if (this.useNaiveMethod)
                c = selectColumnNodeNaive();
            else
                c = selectColumnNodeHeuristic();
            c.cover();

            for(DancingNode r = c.D; r != c; r = r.D){
                answer.add(r);
                if ("data".equals(r.name))
                    this.count--;
                for(DancingNode j = r.R; j != r; j = j.R){
                    j.C.cover();
                    if ("data".equals(j.name))
                        this.count--;
                }

                search(k + 1);

                r = answer.remove(answer.size() - 1);
                c = r.C;
                if ("data".equals(r.name))
                    this.count++;
                for(DancingNode j = r.L; j != r; j = j.L){
                    j.C.uncover();
                    if ("data".equals(j.name))
                        this.count++;
                }
            }
            c.uncover();
        }
    }

    private ColumnNode selectColumnNodeNaive(){
        return (ColumnNode) header.R;
    }

    private ColumnNode selectColumnNodeHeuristic(){
        int min = Integer.MAX_VALUE;
        ColumnNode ret = null;
        for(ColumnNode c = (ColumnNode) header.R; c != header; c = (ColumnNode) c.R){
            if (c.size < min){
                min = c.size;
                ret = c;
            }
        }
        return ret;
    }

    // grid is a grid of 0s and 1s to solve the exact cover for
    // returns the root column header node
    private ColumnNode makeDLXBoard(int[][] grid){
        final int COLS = grid[0].length;
        final int ROWS = grid.length;

        ColumnNode headerNode = new ColumnNode("header");
        ArrayList<ColumnNode> columnNodes = new ArrayList<>();

        for(int i = 0; i < COLS; i++){
            ColumnNode n;
            if (i < COLS - this.idLength) {
                n = new ColumnNode("data");
            } else {
                n = new ColumnNode("id");
            }
            columnNodes.add(n);
            headerNode = (ColumnNode) headerNode.hookRight(n);
        }
        headerNode = headerNode.R.C;

        for(int i = 0; i < ROWS; i++){
            DancingNode prev = null;
            for(int j = 0; j < COLS; j++){
                if (grid[i][j] == 1){
                    ColumnNode col = columnNodes.get(j);
                    DancingNode newNode;
                    if (j < COLS - this.idLength) {
                        newNode = new DancingNode(col, i, "data");
                    } else {
                        newNode = new DancingNode(col, i, "id");
                    }
                    if (prev == null)
                        prev = newNode;
                    col.U.hookDown(newNode);
                    prev = prev.hookRight(newNode);
                    col.size++;
                }
            }
        }

        headerNode.size = COLS;

        return headerNode;
    }


    // Grid consists solely of 1s and 0s. Undefined behaviour otherwise
    public DancingLinks(int[][] grid, int idLength){
        this.count = grid[0].length - idLength;
        this.idLength = idLength;
        header = makeDLXBoard(grid);
        handler = new MyHandler();
        res = new ArrayList<>();
    }


    public List<List<Integer>> runSolver(){
        answer = new LinkedList<>();
        search(0);
        if (res.size() == 0)
            this.useNaiveMethod = true;
        search(0);
        return res;
    }

}

