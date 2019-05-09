package Elements.actor;

import Elements.PacmanActor;
import Elements.PacmanGame;
import Elements.PacmanGame.State;
import java.awt.Graphics2D;

/**
 * HUD class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class HUD extends PacmanActor {

    public HUD(PacmanGame game) {
        super(game);
    }

    @Override
    public void init() {
        loadFrames("/res/pacman_life.png");    
    }

    @Override
    public void draw(Graphics2D g) {
        if (!visible) {
            return;
        }
        game.drawText(g, "SCORE", 10, 1);
        game.drawText(g, game.getScore(), 10, 10);
        game.drawText(g, "HIGH SCORE ", 78, 1);
        game.drawText(g, game.getHiscore(), 90, 10);
        game.drawText(g, "LIVES: ", 10, 274);
        for (int lives = 0; lives < game.lives; lives++) {
            g.drawImage(frame, 60 + 20 * lives, 272, null);
        }
        game.drawText(g, "MODE",180,1);
        game.drawText(g,game.getIAMode().toUpperCase(),170,10);
    }

    // broadcast messages
    
    @Override
    public void stateChanged() {
        visible = (game.state != State.INITIALIZING)
                && (game.state !=State.OL_PRESENTS);
    }
    
}
