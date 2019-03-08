package br.ol.pacman.actor;

import br.ol.pacman.PacmanActor;
import br.ol.pacman.PacmanGame;
import br.ol.pacman.PacmanGame.State;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Background class.
 * 
 * 28 x 31 cells
 * 
 * @ClassUtility Gestion du background
 */
public class Background extends PacmanActor {
    
	/* Gère les carrés "solides" du background, ceux sur lesquels on peut avancer ou non */
    private boolean showBlockedCellColor = false;
    private Color blockedCellColor = new Color(255, 0, 0, 128);
    private int frameCount;
    
    public Background(PacmanGame game) {
        super(game);
    }

    @Override
    public void init() {
        loadFrames("/res/background_0.png", "/res/background_1.png");
    }

    /**
     * Met à jour les attributs de Actor en fonction du temps écoulé (currentTimeMillis())
     * Je ne sais pas pourquoi, à suivre...
     * Si on passe par le case 4, on met à jour framecount
     */
    @Override
    public void updateLevelCleared() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    frameCount = 0;
                    waitTime = System.currentTimeMillis();	//long waitTime from Actor
                    instructionPointer = 1;					// int instructionPointer from Actor
                case 1:
                    if (System.currentTimeMillis() - waitTime < 1500) {
                        break yield;
                    }
                    instructionPointer = 2;
                case 2:
                    frame = frames[1];		// BufferedImage frame et BufferedImage frames[] from Actor
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 3;
                case 3:
                    if (System.currentTimeMillis() - waitTime < 200) {
                        break yield;
                    }
                    frame = frames[0];
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 4;
                case 4:
                    if (System.currentTimeMillis() - waitTime < 200) {
                        break yield;
                    }
                    frameCount++;
                    if (frameCount < 5) {
                        instructionPointer = 2;
                        continue yield;
                    }
                    game.broadcastMessage("hideAll");
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 5;
                
                /* Fin de la partie */
                case 5:
                    if (System.currentTimeMillis() - waitTime < 500) {
                        break yield;
                    }
                    visible = true;
                    game.nextLevel();
                    break yield;
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        if (showBlockedCellColor) {
            g.setColor(blockedCellColor);
            for (int row=0; row<31; row++) {
                for (int col=0; col<36; col++) {
                    if (game.maze[row][col] == 1) {
                        g.fillRect(col * 8 - 32, (row + 3) * 8, 8, 8);
                    }
                }
            }
        }
    }

    // broadcast messages

    @Override
    public void stateChanged() {
        if (game.getState() == State.TITLE) {
            visible = false;
        }
        else if (game.getState() == State.READY) {
            visible = true;
        }
        else if (game.getState() == State.LEVEL_CLEARED) {
            instructionPointer = 0;
        }
    }

    public void hideAll() {
        visible = false;
    }
    
}
