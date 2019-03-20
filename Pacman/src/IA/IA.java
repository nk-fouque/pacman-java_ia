package IA;

import Elements.PacmanGame;
import Elements.actor.Pacman;
import Elements.infra.Game;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class IA {

    private final int[] uTurn = new int[]{2, 3, 0, 1};
    private int lastInput;
    private boolean verbose = false;

    public IA(){
        lastInput=3;
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
     * Use a treesearch {@link PathfindNode.java} to find the best gamestate
     * @param game the current game
     * @return the new direction of Pacman
     */
    public int askDirectionEatmaxTreeSearch(PacmanGame game){
        int res = 0;
        GameState state = new GameState(game,lastInput);
        res = state.searchBestGamestate(6);
        if(verbose) System.out.println("\n");
        lastInput=res; //Records the input before sending it
        return res;
    }
    
    
    
    
    
    
    
    

}
