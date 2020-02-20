package puzzle;

import java.util.ArrayList;
import java.util.List;

/*
* 拼图类
*
* */
public class Tile {
    private int[][] data;
    private int id;

    public Tile(int[][] data) {
        this.data = data;
    }

    public Tile(int[][] data, int id) {
        this.data = data;
        this.id = id;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public void setData(int[][] data) { this.data = data; }


    public int[][] getData() { return data; }


    public void setDataById() {
        int r = this.data.length;
        int c = this.data[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (data[i][j] != 0) {
                    data[i][j] = this.id;
                }
            }
        }
    }

}
