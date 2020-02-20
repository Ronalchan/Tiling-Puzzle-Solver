package puzzle;

import utils.FileParser;

import java.util.List;

public class PuzzleSolverMain {
    public static void main(String[] args) {
        boolean toRotate = true;
        boolean toReverse = true;
        // 读取文件中所有的tile
        FileParser fileParser = new FileParser();
        List<Tile> tiles = fileParser.getAlltiles("/Users/pengyinuo/Desktop/Algorithm/Final Project/src/inputs/partial_cross.txt");
        PuzzleSolver solver = new PuzzleSolver(tiles, toRotate, toReverse);
        solver.validateSolutions();
        return;
    }
}
