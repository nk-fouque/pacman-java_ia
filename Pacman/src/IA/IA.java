package IA;

import br.ol.pacman.infra.Game;

import java.util.concurrent.ThreadLocalRandom;

public class IA {

    public IA(){

    }

    /**
     *
     * @param game, on verra ce qu'on en fait, pour l'instant je le mets là
     * @return 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     */
    public int askDirection(Game game){
        int res = ThreadLocalRandom.current().nextInt(0, 4);
        return res;
    }
}
