package IA;

import Elements.PacmanGame;
import Elements.actor.Pacman;
import Elements.infra.Game;
import main.Main;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class IA {

    private final int[] uTurn = new int[]{2, 3, 0, 1};
    private int lastInput;
    private boolean verbose = Main.verbose;

    public IA(){
        this.lastInput=3;
    }

    /**
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

    public int askDirectionEatmaxTreeSearch(PacmanGame game){
        GameState state = new GameState(game,lastInput);
        int res = state.searchBestGamestate(8); // TODO Apparently
        if(verbose) System.out.println("\n");
        lastInput=res; //Records the input before sending it
        return res;
    }


}
