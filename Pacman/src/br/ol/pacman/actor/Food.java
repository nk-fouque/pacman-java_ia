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
    
	/* Coordonnées des boules de food */
    private int col;
    private int row;
    
    public Food(PacmanGame game, int col, int row) {
        super(game);
        this.col = col;
        this.row = row;
    }

    /**
     * Initialisation des boules de nourriture et de leur collider
     */
    @Override
    public void init() {
        loadFrames("/res/food.png");			// Vue
        /* x et y sont les coordonnées d'un collider */
        Actor.x = col * 8 + 3 - 32;				// cf le système de coordonnées du collider ? 
        Actor.y = (row + 3) * 8 + 3;			// same
        collider = new Rectangle(0, 0, 2, 2);	// collider associé
    }

    /**
     * Gestion de la collision avec la boule de food ->
     * - la boule disparait
     * - le compteur de nourriture décrémente
     * - le score augmente de 10
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
     * Gestion de l'affichage sur la vue 2D des bouboules
     */
    @Override
    public void draw(Graphics2D g) {
        if (!visible) {
            return;
        }
        g.setColor(Color.WHITE);
        g.fillRect((int) (x), (int) (y), 2, 2);
    }
    
    /* Probable debug mais je sais pas trop */
    // broadcast messages

    /**
     * Gestion de l'affichage des bouboules en fonction de l'état du jeu
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
