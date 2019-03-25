package IA;

import java.util.Arrays;
import java.util.Map;

public class FloydWarshall {
    //On regarde en se déplacant si la distance à la case à côté par rapport à ma case de cible de départ diminue

    /*
    N = nb de sommets = nb de cases
    s et s' paires de cases
    */

    //36 x 31

    private int maze[][] = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,1,1,1,1},
            {1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1},
            {1,1,1,1,1,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    };

    private int nbCol = 36;
    private int nbRow = 31;

    private int nbSommets = 306;

    private int distances[][] = new int[nbSommets][nbSommets];
    private int adjencyMatrix[][] = new int[nbSommets][nbSommets];

    private int d[][][][] = new int[nbRow][nbCol][nbRow][nbCol]; //si sj s2i s2j

    public void initD(){
        // fills d with infinity
        for (int[][][] r : d)
            for(int[][] c : r)
                for(int[] rc : c)
                    Arrays.fill(rc, 1000000);    //simulates the infinity

        // changes infinity to 1 for adjency vertices
        for(int i=0; i<nbRow; i++){
            for(int j=0; j<nbCol; j++){
                if(maze[i][j] == 0){
                    if(maze[i-1][j] == 0)
                        d[i][j][i-1][j] = 1;
                    if(maze[i][j-1] == 0)
                        d[i][j][i][j-1] = 1;
                    if(maze[i+1][j] == 0)
                        d[i][j][i+1][j] = 1;
                    if(maze[i][j+1] == 0)
                        d[i][j][i][j+1] = 1;
                }
            }
        }

        //makes the link between the 2 parts of the tunnel
        d[14][1][14][34] = 1;
        d[14][34][14][1] = 1;

    }

    public void algoFW(){
        // Init distances with infinity in each case
        initD();
        System.out.println("Init done");

        // Calculus of matrix distances
        for(int ki=0; ki<nbRow; ki++){
            for(int kj=0; kj<nbCol; kj++) {
                if(maze[ki][kj] != 0) continue;
                for (int si = 0; si < nbRow; si++) {
                    for (int sj = 0; sj < nbCol; sj++) {
                        if(maze[si][sj] != 0) continue;
                        for (int si2 = 0; si2 < nbRow; si2++) {
                            for (int sj2 = 0; sj2 < nbCol; sj2++) {
                                if(maze[si2][sj2] != 0) continue;
                                d[si][sj][si2][sj2] = Math.min(d[si][sj][si2][sj2], d[si][sj][ki][kj] + d[ki][kj][si2][sj2]);
                            }
                        }
                    }
                }
            }
        }
    }

    private void calculateNbSommets (){
        int res = 0;
        for(int i=0; i<nbRow;i++){
            for(int j=0; j<nbCol; j++){
                if(maze[i][j] == 0) res++;
            }
        }
        this.nbSommets = res;
    }



    /**
    public void algo(){
        //Init distances with infinity in each case
        for (int[] row : distances)
            Arrays.fill(row, 100000);    //simulates the infinity

        // Changes the distance from infinity to 1 for the vertices
        calcAdj();

        // Calculus of matrix distances
        for(int k=0; k<nbSommets; k++){
            for(int s = 0; s<nbSommets; s++){
                for(int s2 = 0; s2<nbSommets; s2++){
                    distances[s][s2] = Math.min(distances[s][s2] , distances[s][k]+distances[k][s2]);
                }
            }
        }
    }

    */

    public void printGrid(int a, int b){
        for (int i = 0;i<nbRow;i++){
            for(int j = 0;j<nbCol;j++){
                System.out.print(d[a][b][i][j]+",");
            }
            System.out.println(" ");
        }
    }

    public static void main(String[] args){
        FloydWarshall fw = new FloydWarshall();
        //fw.calculateNbSommets();
        //System.out.println(fw.nbSommets);
        fw.algoFW();
        fw.printGrid(2,5);
    }



}
