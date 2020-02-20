package utils;
import puzzle.Tile;

import java.util.ArrayList;
import java.util.List;

public class TileDataTransformer {
    private Tile tile;
    private Tile board;
    private int tileNums; // tile的数量
    private int row;
    private int col;
    private boolean toRotate; // 是否旋转的开关
    private boolean toReverse; // 是否翻转的开关


    public TileDataTransformer(Tile tile, int tileNums, Tile board, boolean toRotate, boolean toReverse) {
        this.tile = tile;
        this.board = board;
        this.tileNums = tileNums;
        this.row = board.getData().length;
        this.col = board.getData()[0].length;
        this.toRotate = toRotate;
        this.toReverse = toReverse;
    }

    // 返回该拼图经过旋转、翻转之后的所有可能的数据行
    // 核心方法
    public List<List<Integer>> getAllDataRows() {
        List<List<Integer>> res = new ArrayList<>();

        // 求出原始拼图所有可能的data rows
        res.addAll(getDataRows(tile.getData()));

        // 如果翻转
        if (toReverse) {
            int[][] reversedMatrix = reverseMatrix(tile.getData());
            int[][] matrixR1 = rotateMatrix90(reversedMatrix);
            int[][] matrixR2 = rotateMatrix180(reversedMatrix);
            int[][] matrixR3 = rotateMatrix270(reversedMatrix);
            if (!compareMatrix(reversedMatrix, tile.getData()) && !compareMatrix(matrixR1, tile.getData()) && !compareMatrix(matrixR2, tile.getData()) && !compareMatrix(matrixR3, tile.getData())) {
                res.addAll(getDataRows(reversedMatrix));
                // 如果旋转
                if (toRotate) {
                    if (!compareMatrix(matrixR1, reversedMatrix)) {
                        res.addAll(getDataRows(matrixR1));
                    }
                    if (!compareMatrix(matrixR2, reversedMatrix) && !compareMatrix(matrixR2, matrixR1)) {
                        res.addAll(getDataRows(matrixR2));
                    }
                    if (!compareMatrix(matrixR3, reversedMatrix) && !compareMatrix(matrixR3, matrixR1) && !compareMatrix(matrixR3, matrixR2)) {
                        res.addAll(getDataRows(matrixR3));
                    }
                }
            }
        }
        // 如果旋转
        if (toRotate) {
            int[][] matrixR1 = rotateMatrix90(tile.getData());
            int[][] matrixR2 = rotateMatrix180(tile.getData());
            int[][] matrixR3 = rotateMatrix270(tile.getData());
            if (!compareMatrix(matrixR1, tile.getData())) {
                res.addAll(getDataRows(matrixR1));
            }
            if (!compareMatrix(matrixR2, tile.getData()) && !compareMatrix(matrixR2, matrixR1)) {
                res.addAll(getDataRows(matrixR2));
            }
            if (!compareMatrix(matrixR3, tile.getData()) && !compareMatrix(matrixR3, matrixR1) && !compareMatrix(matrixR3, matrixR2)) {
                res.addAll(getDataRows(matrixR3));
            }
        }

        return res;
    }

    //比较两个矩阵是否完全一样，完全一样返回true
    private boolean compareMatrix(int[][] matrix1, int[][] matrix2) {
        if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length)
            return false;
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                if (matrix1[i][j] != matrix2[i][j])
                    return false;
            }
        }
        return true;
    }

    //比较两个矩阵是否完全一样，完全一样返回true
    private boolean compareMatrix(int[][] matrix1, int[][] matrix2, int rowOffset, int colOffset) {
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                if (matrix2[i+rowOffset][j+colOffset] == 0) {
                    if (matrix1[i][j] != 0)
                        return false;
                } else {
                    if (matrix1[i][j] != 0 && matrix1[i][j] != matrix2[i+rowOffset][j+colOffset])
                        return false;
                }
            }
        }
        return true;
    }


    // 垂直地翻转矩阵
    private int[][] reverseMatrix (int[][] matrix) {
        int r = matrix.length;
        int c = matrix[0].length;
        int[][] res = new int[r][c];
        for (int i = 0; i < r; i++) {
            res[r-i-1] = matrix[i];
        }
        return res;
    }

    // 获得一个拼图所有可能的dataRows
    public List<List<Integer>> getDataRows(int[][] data) {
        List<List<Integer>> res = new ArrayList<>();
        int[][] matrix;
        List<Integer> dataRow;
        // 遍历一个拼图可以放置的所有位置，(i, j)为拼图的左上顶点
        for (int i = 0; i <= this.row - data.length; i++) {
            for (int j = 0; j <= this.col - data[0].length; j++) {
                // 要除去那些无法匹配的放置方法，比如：超出边界，以及对应位置的data[i][j]不同
                if (compareMatrix(data, this.board.getData(), i, j)) {
                    matrix = getMatrixData(data, i, j);
                    dataRow = matrixToListWithId(matrix);
                    res.add(new ArrayList<>(dataRow));
                }
            }
        }
        return res;
    }

    // 顺时针旋转矩阵270度
    private int[][] rotateMatrix270(int[][] matrix) {
        int w = matrix.length;
        int l = matrix[0].length;
        int result[][] = new int[l][w];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < l; j++)
                result[l - 1 - j][i] = matrix[i][j];
        }
        return result;
    }

    // 顺时针旋转矩阵180度
    private int[][] rotateMatrix180(int[][] matrix) {
        int w = matrix.length;
        int l = matrix[0].length;
        int result[][] = new int[w][l];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < l; j++)
                result[w - i - 1][l - j - 1] = matrix[i][j];
        }
        return result;
    }

    // 顺时针旋转矩阵90度
    private int[][] rotateMatrix90(int[][] matrix) {
        int w = matrix.length;
        int l = matrix[0].length;
        int result[][] = new int[l][w];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < l; j++)
                result[j][w - i - 1] = matrix[i][j];
        }
        return result;
    }

    // 2D矩阵转list<Integer>
    private List<Integer> matrixToList(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                res.add(matrix[i][j]);
            }
        }
        return res;
    }

    // 2D矩阵转list<Integer>, 并且将tile的id作为dataRow的一部分
    private List<Integer> matrixToListWithId(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                res.add(matrix[i][j]);
            }
        }
        int id = this.tile.getId();
        for (int i = 1; i <= tileNums; i++) {
            if (i == id) {
                res.add(1);
            } else {
                res.add(0);
            }
        }
        return res;
    }

    // 将小矩阵铺到大矩阵(size = this.row * this.col), 返回铺完后的大矩阵
    public int[][] getMatrixData(int[][] data, int rowIdx, int colIdx) {
        int[][] res = new int[this.row][this.col];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                res[i+rowIdx][j+colIdx] = data[i][j];
            }
        }
        return res;
    }
}
