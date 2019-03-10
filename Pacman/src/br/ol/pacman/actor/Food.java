package br.ol.pacman.actor;

import br.ol.pacman.PacmanActor;
import br.ol.pacman.PacmanGame;
import br.ol.pacman.infra.Keyboard;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
 * Food class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Food extends PacmanActor {
    
	/*/
	 * Coordinates of the food on the grid frame
	 */
    private int col;
    private int row;
    
    public Food(PacmanGame game, int col, int row) {
        super(game);
        this.col = col;
        this.row = row;
    }

    /**
     * Initialisation of the food and its collider
     */
    @Override
    public void init() {
        loadFrames("/res/food.png");			// Vue
        /* x et y sont les coordonnées d'un collider */
        x = col * 8 + 3 - 32;				// cf le systeme de coordonnees du collider ? 
        y = (row + 3) * 8 + 3;			// same
        collider = new Rectangle(0, 0, 2, 2);	// collider associé
    }

    /**
     * Collision with the food ->
     * - it disappears
     * - the food counter decrements
     * - the score increases by 10
     */
    @Override
    public void updatePlaying() {
//        // for debug purpose A key clear level
//        if (Keyboard.keyPressed[KeyEvent.VK_A]) {
//            game.currentFoodCount = 0;
//        }
        
        if (game.checkCollision(this, Pacman.class) != null) {
            visible = false;
            game.currentFoodCount--;
            game.addScore(10);
            //System.out.println("current food count: " + game.currentFoodCount);
        }
    }

	/**
	 * Display of the Food on the graphic view
	 * @param g the graphics view in 2D
	 */
    @Override
    public void draw(Graphics2D g) {
        if (!visible) {
            return;
        }
        g.setColor(Color.WHITE);
        g.fillRect((int) (x), (int) (y), 2, 2);
    }
    
    // broadcast messages

    /**
     * Display of the food depending on the state of the game
     */
    @Override
    public void stateChanged() {
        if (game.getState() == PacmanGame.State.TITLE) {
            visible = false;
        }
        else if (game.getState() == PacmanGame.State.READY) {
            visible = true;
        }
    }

    public void hideAll() {
        visible = false;
    }
    
}
