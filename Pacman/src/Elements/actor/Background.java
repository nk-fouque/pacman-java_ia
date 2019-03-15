package Elements.actor;

import Elements.PacmanActor;
import Elements.PacmanGame;
import Elements.PacmanGame.State;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Background class.
 * 
 * 28 x 31 cells
 * 
 */
public class Background extends PacmanActor {
    
	/* Display of the cells depending on if they are blocked */
    private boolean showBlockedCellColor = false;
    private Color blockedCellColor = new Color(255, 0, 0, 128);
    private int frameCount;
    
    public Background(PacmanGame game) {
        super(game);
    }

    /**
     * Initialisation of the type of background
     */
    @Override
    public void init() {
        loadFrames("/res/background_0.png", "/res/background_1.png");
    }
    
    /**
     * Update of the attributes of the class depending on the time (currentTimeMillis)
     * I don't know why, to be continued...
     * If we switch on the case 4 we update framecount
     */
    @Override
    public void updateLevelCleared() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    frameCount = 0;
                    waitTime = System.currentTimeMillis();	//long waitTime
                    instructionPointer = 1;					// int instructionPointer
                case 1:
                    if (System.currentTimeMillis() - waitTime < 1500) {
                        break yield;
                    }
                    instructionPointer = 2;
                case 2:
                    frame = frames[1];		// BufferedImage frame and BufferedImage frames[]
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
                
                /* Game over */
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
    
    
    /**
     * Display of the graphic view
     * @param g the graphic view in 2D
     */
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
    
    /**
     * Display of the background depending on the state of the game
     */
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

    /**
     * Hide the background
     */
    public void hideAll() {
        visible = false;
    }
    
}
