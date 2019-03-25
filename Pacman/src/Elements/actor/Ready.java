package Elements.actor;

import Elements.PacmanActor;
import Elements.PacmanGame;
import Elements.PacmanGame.State;
import main.Main;

/**
 * Ready class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Ready extends PacmanActor {

    private boolean visibleGame;

    public Ready(PacmanGame game, boolean visi) {
        super(game);
        this.visibleGame = visi;
    }

    @Override
    public void init() {
        x = 11 * 8;
        y = 20 * 8;
        if(visibleGame)
            loadFrames("/res/ready.png");
    }

    @Override
    public void updateReady() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    game.restoreCurrentFoodCount();
                    //game.sounds.get("start").play();
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if(visibleGame) {
                       if (System.currentTimeMillis() - waitTime < 2000) { // || game.sounds.get("start").isPlaying()) {
                            break yield;
                       }
                    }
                    game.setState(State.READY2);
                    break yield;
            }
        }
    }
    
    @Override
    public void updateReady2() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    if(visibleGame) {
                        game.broadcastMessage("showAll");
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if(visibleGame) {
                        if (System.currentTimeMillis() - waitTime < 2000) { // || game.sounds.get("start").isPlaying()) {
                            break yield;
                        }
                    }
                    game.setState(State.PLAYING);
                    break yield;
            }
        }
    }

    // broadcast messages

    @Override
    public void stateChanged() {
        visible = false;
        if (game.getState() == PacmanGame.State.READY 
                || game.getState() == PacmanGame.State.READY2) {
            
            visible = true;
            instructionPointer = 0;
        }
    }
    
}
