package IA;

import Elements.PacmanGame;
import Elements.infra.Game;
import main.Main;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class IA {

    private final int[] uTurn = new int[]{2, 3, 0, 1};
    private int lastInput;
    private boolean verbose = Main.verbose;
    private FloydWarshall fw;
    private ArrayList<Integer>[][] possibleMoves;

    public IA(){
        this.lastInput=3;
        this.fw = new FloydWarshall();
        fw.algoFW();
        System.out.println("Floyd Warshall Ready");
        possibleMoves=fw.possibleMoves();
    }



    /**
     * Return a random direction for Pacman
     * @return 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     */
    public int randDirection(int actualDirectionPacman, Game game){
        int res = ThreadLocalRandom.current().nextInt(0, 4);
        while(res == uTurn[actualDirectionPacman]){
            //System.out.println("Res is "+res+" and Pacman goes "+actualDirectionPacman);
            res = ThreadLocalRandom.current().nextInt(0, 4);
        }
        //System.out.println("Now he goes :"+res);
        return res;
    }

    /**
     * Use a treesearch {@link PathfindTree} to find the best gamestate
     * @param game the current game
     * @return the new direction of Pacman
     */
    public int askDirectionEatmaxTreeSearch(PacmanGame game){
        GameState state = new GameState(game,lastInput);
        int res = state.searchBestGamestate(8);
        if(verbose) System.out.println("\n");
        lastInput=res; //Records the input before sending it
        return res;
    }

    public int askDirectionMinMaxTreeSearch(PacmanGame game){
        GameStatePlus state = new GameStatePlus(game,lastInput,possibleMoves);
        int res = state.searchBestGamestate(8,fw);
        if(verbose) System.out.println("\n");
        lastInput=res; //Records the input before sending it
        return res;
    }
    

    
    
    
    
    
    

}
