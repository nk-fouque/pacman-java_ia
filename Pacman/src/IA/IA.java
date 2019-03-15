package IA;

import Elements.infra.Game;

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

    /**
     *
     * @param game, on verra ce qu'on en fait, pour l'instant je le mets là
     * @return 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     */
    public int askDirection(Game game){

        return 0;
    }

    public void searchGame(Game game){

    }

}
