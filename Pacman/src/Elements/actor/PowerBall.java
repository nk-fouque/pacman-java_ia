package Elements.actor;

import Elements.PacmanActor;
import Elements.PacmanGame;
import Elements.PacmanGame.State;
import java.awt.Rectangle;

/**
 * Food class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class PowerBall extends PacmanActor {
    
    private int col;
    private int row;
    private boolean eaten;

    /**
     *
     * @param game
     * @param col x position of the powerball
     * @param row y position of the powerball
     *
     */
    
    public PowerBall(PacmanGame game, int col, int row) {
        super(game);
        this.col = col;
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Initializes one powerball (position on the board, collider), but doesn't display it
     * At the initialization the powerball is considered as eaten, the boolean eaten being set to false when the game is in the state READY (at the very beginning of the game)
     */

    @Override
    public void init() {
        loadFrames("/res/powerBall.png");
        x = col * 8 + 1 - 32;
        y = (row + 3) * 8 + 1;
        collider = new Rectangle(0, 0, 4, 4);
        eaten = true;
    }

    /**
     * Updates the ball
     * The ball is visible if it's not eaten, and flickers
     * The ball stops being updated after being eaten, or when PacMan is dead
     * If PacMan collides with the ball's collider, eaten is set to true, it's not visible anymore, score+50, the ghosts become vulnerable for a time
     */

    @Override
    public void update() {
        visible = !eaten && (int) (System.nanoTime() * 0.0000000075) % 2 == 0;
        if (eaten || game.getState() == State.PACMAN_DIED) {
            return;
        }
        if (game.checkCollision(this, Pacman.class) != null) {
            eaten = true;
            visible = false;
            game.addScore(50);
            game.startGhostVulnerableMode();
        }
    }
    
    // broadcast messages

    /**
     * Manages the display of the powerballs
     * If state = TITLE||LEVEL_CLEARED||GAME_OVER eaten is set to true
     * If state = READY, the powerballs are displayed and eaten is set to false
     */

    @Override
    public void stateChanged() {
        if (game.getState() == PacmanGame.State.TITLE 
                || game.getState() == State.LEVEL_CLEARED 
                || game.getState() == State.GAME_OVER) {
            eaten = true;;
        }
        else if (game.getState() == PacmanGame.State.READY) {
            eaten = false;
            visible = true;
        }
    }

    public void hideAll() {
        visible = false;
    }
    
}
