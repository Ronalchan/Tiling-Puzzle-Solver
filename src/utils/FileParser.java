package utils;

import puzzle.Tile;

import java.util.*;
import java.io.*;
import java.lang.Integer;

public class FileParser {

    public static List<String> readTxtFileIntoStringArrList(String filePath)
    {
        List<String> list = new ArrayList<>();
        try{
            String encoding = "ASCII";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // Judge whether exists the file
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) // Read the file by line
                {
                    list.add(lineTxt);
                }
                bufferedReader.close();
                read.close();
            }
            else {
                System.out.println("could not find the file");
            }
        }
        catch (Exception e) {
            System.out.println("errors in read file");
            e.printStackTrace();
        }
        return list;
    }

    // design the recurrence
    private static void helper(int[][] array, int[][] visited, int x, int y,List<Integer> value) {
        if (x < 0 || x >= array.length || y < 0 || y >= array[0].length || array[x][y] == 0 || visited[x][y] == 1) return;
        visited[x][y] = 1;
        if(array[x][y]>=1){
            for(int i=0;i<array[x][y];i++){
                value.add(x+array.length*array[0].length*i);
                value.add(y+array.length*array[0].length*i);
            }
        } // based on the colors, add the value n times
        helper(array, visited, x - 1, y,value);
        helper(array, visited, x + 1, y,value);
        helper(array, visited, x, y - 1,value);
        helper(array, visited, x, y + 1,value); // up,down,right,left directions
    }
    private static int get_x_max(int[] a){
        int max = Integer.MIN_VALUE;
        for(int i=0;i<a.length-1;i=i+2){
            max = Math.max(max,a[i]);
        }
        return max;
    }
    private static int get_x_min(int[] a){
        int min = Integer.MAX_VALUE;
        for(int i=0;i<a.length-1;i=i+2){
            min = Math.min(min,a[i]);
        }
        return min;
    }
    private static int get_y_max(int[] a){
        int max = Integer.MIN_VALUE;
        for(int i=1;i<a.length;i=i+2){
            max = Math.max(max,a[i]);
        }
        return max;
    }
    private static int get_y_min(int[] a){
        int min = Integer.MAX_VALUE;
        for(int i=1;i<a.length;i=i+2){
            min = Math.min(min,a[i]);
        }
        return min;
    }

    private static List<Tile> gettiles(int[][] array, int colors) {
        if (array.length==0 || array[0].length==0) return null;
        int m = array.length, n = array[0].length, len = 0;
        int[][] visited = new int[m][n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                visited[i][j]=0;
            }
        }

        List<Integer> index = new ArrayList<>(); // spilt index of each tile
        List<Integer> list = new ArrayList<>(); // all coordinate of each tile
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (array[i][j] == 0 || visited[i][j] == 1) continue;
                index.add(i);
                index.add(j); // add the index of each tiles
                helper(array, visited, i, j, list);
                ++len;
            }
        }

        int[] idx = new int[index.size()];
        for(int i = 0; i< index.size();i++){
            idx[i] = index.get(i);
        }
        int[] coordinate = new int[list.size()];
        for(int i = 0; i< list.size();i++){
            coordinate[i] = list.get(i);
        }

        int[] temp = new int[len+1];
        for (int i = 0; i < coordinate.length-1; i++) {
            for (int j = 0; j< idx.length;j=j+2){
                if(i%2==0 && coordinate[i]==idx[j] && coordinate[i+1]==idx[j+1]) {
                    temp[j / 2] = i;
                    break;
                }
            }
        }

        temp[len] = coordinate.length;
        for(int i = 0; i<len;i++){
            int len_y = temp[i+1]-temp[i]; // get the numbers of each tile
            int[][] tile_value = new int[len][len_y];
            for(int j = temp[i]; j<temp[i+1];j++){
                for(int k=colors;k>0;k--){
                    if(coordinate[j]>=array.length*array[0].length*k){
                        coordinate[j]-=array.length*array[0].length*k;
                    }
                }
                tile_value[i][j-temp[i]]=coordinate[j];
            } // get the coordinate of point in the tile

            int x = get_x_max(tile_value[i]) - get_x_min(tile_value[i]) + 1; // get the number of tiles
            int y = get_y_max(tile_value[i]) - get_y_min(tile_value[i]) + 1; // get the length of the tile
            int[][] single_tile = new int[(tile_value[i].length/2)][2];
            int[][] input = new int[x][y];

            // start the tile from array[0][0]
            for (int j = 0; j < len_y; j++) {
                if (j % 2 == 0) {
                    single_tile[j / 2][0] = tile_value[i][j] - get_x_min(tile_value[i]);
                } else {
                    single_tile[j / 2][1] = tile_value[i][j] - get_y_min(tile_value[i]);
                }
            }

            for (int l = 0; l < single_tile.length; l++) {
                input[single_tile[l][0]][single_tile[l][1]] += 1;
            }
            Tile tile = new Tile(input);
            tiles.add(tile); // get each tiles
        }

        // put the target at the end of all tiles
        int maxIndex = 0;
        int max_len = 0;
        for (int i = 0; i < len; i++) {
            if(temp[i+1]-temp[i]>max_len){
                max_len = temp[i+1]-temp[i];
                maxIndex = i;
            }
        }
        if(maxIndex!=tiles.size()-1){
            Collections.swap(tiles,maxIndex,tiles.size()-1);
        }
        return tiles;
    }

    public static List<Tile> getAlltiles(String filePath) {

        List<String> list = readTxtFileIntoStringArrList(filePath);
        int col = 0;
        int row = list.size();
        for (int i = 0; i < list.size(); i++) {
            col = Math.max(col, list.get(i).length());
        }

        int[][] array = new int[row][col];
        List<Character> S = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).length(); j++) {
                if (list.get(i).charAt(j) != ' ') {
                    if (!S.contains(list.get(i).charAt(j))){
                        S.add(list.get(i).charAt(j));
                    }
                }
            }
        }

        int colors = S.size();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).length(); j++) {
                if (list.get(i).charAt(j) != ' ') {
                    for(int k = 0; k<S.size();k++){
                        if (list.get(i).charAt(j) == S.get(k)) {
                            array[i][j] = k+1;
                        }
                    }
                } else {
                    array[i][j] = 0;
                }
            }
        }
        return gettiles(array,colors);
    }
}