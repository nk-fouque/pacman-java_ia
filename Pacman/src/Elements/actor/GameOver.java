package Elements.actor;


import Elements.PacmanActor;
import Elements.PacmanGame;
import Elements.PacmanGame.State;
import main.Main;

/**
 * GameOver class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class GameOver extends PacmanActor {
    
    public GameOver(PacmanGame game) {
        super(game);
    }

    /**
     * Initialisation of the Game Over
     *  + set up of the coordinates of the square
     */
    @Override
    public void init() {
        x = 77;
        y = 160;
        if(Main.visibleGame)
            loadFrames("/res/gameover.png");
    }
    
    /**
     * Update of the attributes depending on the time
     */
    @Override
    public void updateGameOver() {
        if(Main.visibleGame){
            yield:
            while (true) {
                switch (instructionPointer) {
                    case 0:
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 1;
                    case 1:
                        if (System.currentTimeMillis() - waitTime < 3000) {
                            break yield;
                        }
                        game.returnToTitle();
                        break yield;
                }
            }
        }else game.returnToReady(); //FIXME : here, makes it play infinitly, change to "returnToTitle" to pause, or change return function for state eval
    }

    // broadcast messages
    
    @Override
    public void stateChanged() {
        visible = false;
        if (game.state == State.GAME_OVER) {
            visible = true;
            instructionPointer = 0;
        }
    }
        
}
