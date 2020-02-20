import puzzle.PuzzleSolver;
import puzzle.Tile;
import utils.FileParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


public class gui {
    private JPanel panelMain;
    private JPanel panelImport;
    private JPanel panelSolve;
    private JPanel panelTargettile;
    private JPanel panelTile_1;
    private JPanel panelTile_2;
    private JPanel panelTile_3;
    private JPanel panelTile_4;
    private JPanel panelTile_5;
    private JPanel panelTile_6;
    private JPanel panelTile_7;
    private JPanel panelTile_8;
    private JPanel panelTile_9;
    private JPanel panelTile_10;
    private JPanel panelTile_11;
    private JPanel panelTile_12;
    private JPanel panelTile_13;
    private JPanel panelTile_14;
    private JPanel panelTile_15;
    private JButton buttonImport;
    private JTextField textFieldPath;
    private JButton buttonSolve;
    private JCheckBox Rotate;
    private JCheckBox Rollback;
    private JPanel targetTile;
    private JPanel Solution_6;
    private JPanel Solution_1;
    private JPanel Solution_2;
    private JPanel Solution_3;
    private JPanel Solution_4;
    private JPanel Solution_5;
    private JPanel Solution_7;
    private JPanel Solution_8;
    private JTextField numbersRecord;
    private JTextField firstTimeRecord;
    private JTextField allTimeRecord;
    private JButton buttonReset;
    private boolean isRotate = false;
    private boolean isRollback = false;
    private String filePath = "";
    private int tile_colors = 0;

    private static JFrame frameMain = new JFrame("Tiling Puzzle Solver");



    public gui() {

        Map<Integer, JPanel> map = new HashMap<Integer, JPanel>();
        map.put(0,panelTile_1);
        map.put(1,panelTile_2);
        map.put(2,panelTile_3);
        map.put(3,panelTile_4);
        map.put(4,panelTile_5);
        map.put(5,panelTile_6);
        map.put(6,panelTile_7);
        map.put(7,panelTile_8);
        map.put(8,panelTile_9);
        map.put(9,panelTile_10);
        map.put(10,panelTile_11);
        map.put(11,panelTile_12);
        map.put(12,panelTile_13);
        map.put(13,panelTile_14);
        map.put(14,panelTile_15);

        Map<Integer, JPanel> solutionMap = new HashMap<Integer, JPanel>();
        solutionMap.put(0,Solution_1);
        solutionMap.put(1,Solution_2);
        solutionMap.put(2,Solution_3);
        solutionMap.put(3,Solution_4);
        solutionMap.put(4,Solution_5);
        solutionMap.put(5,Solution_6);
        solutionMap.put(6,Solution_7);
        solutionMap.put(7,Solution_8);

        Map<Integer, Color> colors = new HashMap<Integer, Color>();
        colors.put(0,new Color(255,182,193));
        colors.put(1,new Color(220,20,60));
        colors.put(2,new Color(255,105,180));
        colors.put(3,new Color(	255,0,255));
        colors.put(4,new Color(	139,0,139));
        colors.put(5,new Color(123,104,238));
        colors.put(6,new Color(75,0,130));
        colors.put(7,new Color(0,0,255));
        colors.put(8,new Color(65,105,225));
        colors.put(9,new Color(30,144,255));
        colors.put(10,new Color(95,158,160));
        colors.put(11,new Color(0,255,255));
        colors.put(12,new Color(127,255,170));
        colors.put(13,new Color(	0,100,0));
        colors.put(14,new Color(255,165,0));

        buttonImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc=new JFileChooser("/Users/pengyinuo/Desktop/inputs");
                int val=fc.showOpenDialog(null);    //文件打开对话框
                if(val==fc.APPROVE_OPTION)
                {
                    //正常选择文件
                    textFieldPath.setText(fc.getSelectedFile().toString());
                    filePath = fc.getSelectedFile().toString();

                    List<Tile> tiles;
                    FileParser fp = new FileParser();
                    tiles = fp.getAlltiles(filePath);
                    int tile_r = 0;
                    int tile_c = 0;
                    for(int m = 0; m < tiles.size()-1;m++) {
                        tile_r = Math.max(tile_r,tiles.get(m).getData().length);
                        tile_c = Math.max(tile_c,tiles.get(m).getData()[0].length);
                    }

                    for(int m = 0; m < tiles.size()-1;m++){

                        int[][] temp = new int[tile_r][tile_c];
                        for(int i = 0; i < tiles.get(m).getData().length; i++){
                            for(int j = 0; j < tiles.get(m).getData()[0].length; j++) {
                                temp[i][j]=tiles.get(m).getData()[i][j];
                                tile_colors = Math.max(tile_colors,temp[i][j]);
                            }
                        }

                        JPanel[][] grids = new JPanel[tile_r][tile_c];
                        map.get(m).setLayout(new GridLayout(tile_r,tile_c));

                        if(tile_colors == 1){
                            for(int i = 0; i < grids.length; i++){
                                for(int j = 0; j < grids[0].length; j++){
                                    grids[i][j] = new JPanel();
                                    if(temp[i][j]==0){
                                        grids[i][j].setBackground(Color.WHITE);
                                    }else if(temp[i][j]==1){
                                        grids[i][j].setBackground(colors.get(m));
                                        grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                    }
                                    grids[i][j].setPreferredSize(new Dimension(30,30));
                                    map.get(m).add(grids[i][j]);
                                }
                            }
                        }else if (tile_colors == 2){
                            for(int i = 0; i < grids.length; i++){
                                for(int j = 0; j < grids[0].length; j++){
                                    grids[i][j] = new JPanel();
                                    if(temp[i][j]==0){
                                        grids[i][j].setBackground(Color.WHITE);
                                    }else if(temp[i][j]==1){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                        grids[i][j].add(new JLabel(String.valueOf(m+1)));
                                        grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                    }else if(temp[i][j]==2){
                                        grids[i][j].setBackground(new Color(175,238,238));
                                        grids[i][j].add(new JLabel(String.valueOf(m+1)));
                                        grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                    }
                                    grids[i][j].setPreferredSize(new Dimension(30,30));

                                    map.get(m).add(grids[i][j]);
                                }
                            }
                        }


                        JInternalFrame frame = new JInternalFrame();
                        if(m==0){frame.setContentPane(new gui().panelTile_1);}
                        else if (m==1){frame.setContentPane(new gui().panelTile_2);}
                        else if (m==2){frame.setContentPane(new gui().panelTile_3);}
                        else if (m==3){frame.setContentPane(new gui().panelTile_4);}
                        else if (m==4){frame.setContentPane(new gui().panelTile_5);}
                        else if (m==5){frame.setContentPane(new gui().panelTile_6);}
                        else if (m==6){frame.setContentPane(new gui().panelTile_7);}
                        else if (m==7){frame.setContentPane(new gui().panelTile_8);}
                        else if (m==8){frame.setContentPane(new gui().panelTile_9);}
                        else if (m==9){frame.setContentPane(new gui().panelTile_10);}
                        else if (m==10){frame.setContentPane(new gui().panelTile_11);}
                        else if (m==11){frame.setContentPane(new gui().panelTile_12);}
                        else if (m==12){frame.setContentPane(new gui().panelTile_13);}
                        else if (m==13){frame.setContentPane(new gui().panelTile_14);}
                        else if (m==14){frame.setContentPane(new gui().panelTile_15);}
                        frame.pack();
                        frameMain.setVisible(false);
                        frameMain.setVisible(true);
                        frame.setVisible(true);
                    }




                    int[][] temp = tiles.get(tiles.size()-1).getData();
                    JPanel[][] grids = new JPanel[temp.length][temp[0].length];

                    targetTile.setLayout(new GridLayout(temp.length,temp[0].length));
                    for(int i = 0; i < grids.length; i++){
                        for(int j = 0; j < grids[0].length; j++){
                            grids[i][j] = new JPanel();
                            //grids[i][j].setSize(10,10);
                            if(temp[i][j]==0){
                                grids[i][j].setBackground(Color.WHITE);
                            }else if(temp[i][j]==1){
                                grids[i][j].setBackground(new Color(175,238,238));
                            }else if(temp[i][j]==2){
                                grids[i][j].setBackground(new Color(245,222,179));
                            }
                            grids[i][j].setPreferredSize(new Dimension(15,15));
                            grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                            targetTile.add(grids[i][j]);
                        }
                    }
                    JInternalFrame frame = new JInternalFrame();
                    frame.setContentPane(new gui().targetTile);
                    frame.pack();
                    frameMain.setVisible(false);
                    frameMain.setVisible(true);
                    frame.setVisible(true);
                }
                else
                {
                    //未正常选择文件，如选择取消按钮
                    textFieldPath.setText("File is not selected");
                }


            }
        });
        buttonSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 读取文件中所有的tile
                FileParser fileParser = new FileParser();
                List<Tile> tiles = fileParser.getAlltiles(filePath);
                PuzzleSolver solver = new PuzzleSolver(tiles, isRotate, isRollback);
                List<int[][]> solutions = solver.showSolutions();
                long firstSolutionTime = solver.firstSolutionTime;
                long allSolutionTime = solver.allSolutionTime;
                int solutionNums = solutions.size();
                firstTimeRecord.setText(String.valueOf(firstSolutionTime)+"ms");
                allTimeRecord.setText(String.valueOf(allSolutionTime)+"ms");
                numbersRecord.setText(String.valueOf(solutionNums));


                int nums = 0;
                if(solutions.size()<=8){
                    nums = solutions.size();
                }else{
                    nums = 8;
                }

                for(int m = 0; m < nums;m++){

                    int[][] temp = solutions.get(m);

                    JPanel[][] grids = new JPanel[temp.length][temp[0].length];
                    solutionMap.get(m).setLayout(new GridLayout(temp.length,temp[0].length));

                    if(tile_colors == 1){
                        for(int i = 0; i < grids.length; i++){
                            for(int j = 0; j < grids[0].length; j++){
                                grids[i][j] = new JPanel();
                                if(temp[i][j]==0){
                                    grids[i][j].setBackground(Color.WHITE);
                                }else if(temp[i][j]==1){
                                    grids[i][j].setBackground(colors.get(0));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==2){
                                    grids[i][j].setBackground(colors.get(1));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==3){
                                    grids[i][j].setBackground(colors.get(2));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==4){
                                    grids[i][j].setBackground(colors.get(3));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==5){
                                    grids[i][j].setBackground(colors.get(4));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==6){
                                    grids[i][j].setBackground(colors.get(5));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==7){
                                    grids[i][j].setBackground(colors.get(6));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==8){
                                    grids[i][j].setBackground(colors.get(7));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==9){
                                    grids[i][j].setBackground(colors.get(8));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==10){
                                    grids[i][j].setBackground(colors.get(9));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==11){
                                    grids[i][j].setBackground(colors.get(10));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==12){
                                    grids[i][j].setBackground(colors.get(11));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==13){
                                    grids[i][j].setBackground(colors.get(12));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==14){
                                    grids[i][j].setBackground(colors.get(13));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==15){
                                    grids[i][j].setBackground(colors.get(14));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }
                                grids[i][j].setPreferredSize(new Dimension(15,15));
                                solutionMap.get(m).add(grids[i][j]);
                            }
                        }
                    }else if (tile_colors == 2){

                        for(int i = 0; i < grids.length; i++){
                            for(int j = 0; j < grids[0].length; j++){
                                grids[i][j] = new JPanel();
                                if(temp[i][j]==1){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(1)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==2){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(2)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==3){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(3)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==4){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(4)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==5){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(5)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==6){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(6)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==7){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(7)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==8){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(8)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==9){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(9)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==10){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(10)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==11){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(11)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==12){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(12)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==13){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(13)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==14){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(14)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }else if(temp[i][j]==15){
                                    if((i+j)%2==0){
                                        grids[i][j].setBackground(new Color(245,222,179));
                                    }else{
                                        grids[i][j].setBackground(new Color(175,238,238));
                                    }
                                    grids[i][j].add(new JLabel(String.valueOf(15)));
                                    grids[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
                                }
                                grids[i][j].setPreferredSize(new Dimension(15,15));
                                solutionMap.get(m).add(grids[i][j]);
                            }
                        }
                    }


                    JInternalFrame frame = new JInternalFrame();
                    if(m==0){frame.setContentPane(new gui().Solution_1);}
                    else if (m==1){frame.setContentPane(new gui().Solution_2);}
                    else if (m==2){frame.setContentPane(new gui().Solution_3);}
                    else if (m==3){frame.setContentPane(new gui().Solution_4);}
                    else if (m==4){frame.setContentPane(new gui().Solution_5);}
                    else if (m==5){frame.setContentPane(new gui().Solution_6);}
                    else if (m==6){frame.setContentPane(new gui().Solution_7);}
                    else if (m==7){frame.setContentPane(new gui().Solution_8);}
                    frame.pack();
                    frameMain.setVisible(false);
                    frameMain.setVisible(true);
                    frame.setVisible(true);
                }




            }
        });
        Rotate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isRotate = true;
            }
        });
        Rollback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isRollback = true;
            }
        });
        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameMain.dispose();
                frameMain.setContentPane(new gui().panelMain);
                frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameMain.setSize(1920,1080);
                frameMain.pack();
                frameMain.setVisible(true);
            }
        });
    }

    public static void main(String[] args){
        frameMain.setContentPane(new gui().panelMain);
        frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMain.pack();
        frameMain.setSize(1920,1080);
        frameMain.setVisible(true);
    }
}
