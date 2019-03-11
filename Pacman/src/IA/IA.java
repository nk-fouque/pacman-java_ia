package IA;

import br.ol.pacman.PacmanGame;
import br.ol.pacman.infra.Game;

import java.util.concurrent.ThreadLocalRandom;

public class IA {

    private int[] uTurn;
    private int currentDir;

    public IA(){
        this.uTurn = new int[]{2, 3, 0, 1};
        this.currentDir = 0;
    }

    /**
     * @return 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     */
    public int randDirection(){
        int res = ThreadLocalRandom.current().nextInt(0, 4);
        while(res == uTurn[currentDir]){
            System.out.println(currentDir+" not "+res);
            res = ThreadLocalRandom.current().nextInt(0, 4);
        }
        currentDir=res;
        return res;
    }

    /**
     *
     * @param game, on verra ce qu'on en fait, pour l'instant je le mets là
     * @return 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     */
    public int askDirection(PacmanGame game){

        return 0;
    }
}
