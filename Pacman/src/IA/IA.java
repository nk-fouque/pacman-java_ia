package IA;

import Elements.PacmanGame;
import Elements.actor.Pacman;
import Elements.infra.Game;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class IA {

    private final int[] uTurn = new int[]{2, 3, 0, 1};

    public IA(){
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

    /* TODO JE SAIS PAS SI ON GARDE CA, L'AUTRE FAIT LA MEME CHOSE QUAND IL EST EN DEPTH = 0
    /**
     *
     * @param game
     * @return 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP

    public int askDirectionEatmaxOneStep(PacmanGame game){
        int res = 0;
        GameState state = new GameState(game);
        GameState[] states = state.possibleFollowingStates();
        int[] evaluateDirs = new int[4];
        for(int i = 0;i<4;i++){
            if(!Objects.isNull(states[i])) {
                evaluateDirs[i] = states[i].newScore();
            }
            if (evaluateDirs[i]>evaluateDirs[res]){
                res = i;
            }
        }

//        System.out.println("Right "+evaluateDirs[0]+" Down "+evaluateDirs[1]+" Left "+evaluateDirs[2]+" Up "+evaluateDirs[3]+" Res "+res);
        return res;
    }
    */

    public int askDirectionEatmaxTreeSearch(PacmanGame game){
        int res = 0;
        GameState state = new GameState(game);
        res = state.searchBestGamestate(0);

        return res;
    }


}
