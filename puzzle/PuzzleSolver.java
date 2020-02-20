package puzzle;

import utils.TileDataTransformer;
import dlx.DancingLinks;

import java.util.*;

public class PuzzleSolver {
    private List<Tile> tiles; // 小拼图
    private Tile targetPuzzle; // 目标大拼图
    private boolean toRotate;
    private boolean toReverse;
    private List<List<Integer>> dataRows;
    public List<int[][]> solutionMatrix;
    public long startTime;
    public long firstSolutionTime;
    public long firstSolutionEndTime;
    public long allSolutionTime;


    public PuzzleSolver(List<Tile> tiles, boolean toRotate, boolean toReverse) {
        this.tiles = tiles;
        this.targetPuzzle = tiles.get(tiles.size()-1);
        this.tiles.remove(this.tiles.size()-1);
        this.toRotate = toRotate;
        this.toReverse = toReverse;
        this.dataRows = new ArrayList<>();
        this.solutionMatrix = new ArrayList<>();
        // 设置每个tile的id(start from 1)
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).setId(i + 1);
        }
        // 获取所有拼图的dataRows
        for (Tile tile: tiles) {
            TileDataTransformer transformer = new TileDataTransformer(tile, this.tiles.size(), this.targetPuzzle, toRotate, toReverse);
            this.dataRows.addAll(transformer.getAllDataRows());
        }
    }


    public List<List<Integer>> generateSolutions() {
        int r = this.dataRows.size();
        int c = this.dataRows.get(0).size();
        Set<Integer> deletedCols = new HashSet<>(); // 记录要删除的列，因为这些列在board中是0，所有的dataRow均会满足，即这一列没有意义，不参与dancingLinks算法
        // 清除board有空缺的列
        for (int i = 0; i < this.targetPuzzle.getData().length; i++) {
            for (int j = 0; j < this.targetPuzzle.getData()[0].length; j++) {
                if (this.targetPuzzle.getData()[i][j] == 0) {
                    deletedCols.add(i * this.targetPuzzle.getData()[i].length + j);
                }
            }
        }
        int[][] dataRowsMatrix = new int[r][c-deletedCols.size()]; // 排除那些全为0的列
        // 转换成0/1二维矩阵
        int y = 0;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (deletedCols.contains(j))
                    continue;
                if (dataRows.get(i).get(j) != 0)
                    dataRowsMatrix[i][y++] = 1;
                else
                    dataRowsMatrix[i][y++] = 0;
            }
            y = 0;
        }
        DancingLinks dancingLinks = new DancingLinks(dataRowsMatrix, this.tiles.size());
        List<List<Integer>> res = dancingLinks.runSolver();
        this.firstSolutionEndTime = dancingLinks.firstSolutionEndTime;
        return res;
    }

    public List<int[][]> showSolutions() {
        this.startTime = System.currentTimeMillis();
        List<List<Integer>> res = generateSolutions();
        cleanIsomorphicSolution(res);
        long allEndTime = System.currentTimeMillis();
        this.allSolutionTime = allEndTime - this.startTime < 0? 0: allEndTime - this.startTime;
        this.firstSolutionTime = this.firstSolutionEndTime - this.startTime < 0? 0: this.firstSolutionEndTime - this.startTime;
        return this.solutionMatrix;
//        for (int i = 0; i < solutionMatrix.size(); i++) {
//            System.out.println("--------------- Solution" + i + " ---------------");
//            printMatrix(solutionMatrix.get(i));
//            System.out.println("------------------------------------------");
//        }
//        System.out.println(this.firstSolutionTime + " ms");
//        System.out.println(this.allSolutionTime + " ms");
//        System.out.println(this.solutionMatrix.size() + " solutions");
    }

    public void validateSolutions() {
        this.startTime = System.currentTimeMillis();
        List<List<Integer>> res = generateSolutions();
        cleanIsomorphicSolution(res);
        long allEndTime = System.currentTimeMillis();
        this.allSolutionTime = allEndTime - this.startTime;
        this.firstSolutionTime = this.firstSolutionEndTime - this.startTime;
        for (int i = 0; i < solutionMatrix.size(); i++) {
            System.out.println("--------------- Solution" + i + " ---------------");
            printMatrix(solutionMatrix.get(i));
            System.out.println("------------------------------------------");
        }
        System.out.println(this.firstSolutionTime + " ms");
        System.out.println(this.allSolutionTime + " ms");
        System.out.println(this.solutionMatrix.size() + " solutions");
    }

    private void cleanIsomorphicSolution(List<List<Integer>> res) {
        for (int i = 0; i < res.size(); i++) {
            int[][] matrix = getSolutionMatrix(res.get(i));
            if (!isIsomorphic(matrix)) {
                solutionMatrix.add(matrix);
            }
        }
    }

    private boolean isIsomorphic(int[][] matrix) {
        if (solutionMatrix.isEmpty()) return false;
        int[][] matrix1 = rotateMatrix90(matrix);
        int[][] matrix2 = rotateMatrix180(matrix);
        int[][] matrix3 = rotateMatrix270(matrix);
        int[][] matrix4 = reverseMatrix(matrix);
        int[][] matrix5 = rotateMatrix90(matrix4);
        int[][] matrix6 = rotateMatrix180(matrix4);
        int[][] matrix7 = rotateMatrix270(matrix4);
        for (int[][] answer: solutionMatrix) {
            if (compareMatrix(answer, matrix) || compareMatrix(answer, matrix1) || compareMatrix(answer, matrix2) || compareMatrix(answer, matrix3))
                return true;
            if (compareMatrix(answer, matrix4) || compareMatrix(answer, matrix5) || compareMatrix(answer, matrix6) || compareMatrix(answer, matrix7))
                return true;
        }
        return false;
    }

    private int[][] getSolutionMatrix(List<Integer> list) {
        int r = targetPuzzle.getData().length;
        int c = targetPuzzle.getData()[0].length;
        int[][] res = new int[r][c];
        for (int row: list) {
            int tileId = 0;
            List<Integer> rowData = this.dataRows.get(row);
            // 获得拼图的id
            for (int i = r * c ; i < rowData.size(); i++) {
                if (rowData.get(i) == 1) {
                    tileId = i - r * c + 1;
                    break;
                }
            }
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    if (rowData.get(i * c + j) != 0) {
                        res[i][j] = tileId;
                    }
                }
            }
        }
        return res;
    }

    private void printMatrix(int[][] matrix) {
        int r = matrix.length;
        int c = matrix[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
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

}
