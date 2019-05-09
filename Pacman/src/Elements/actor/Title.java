package Elements.actor;


import Elements.infra.Keyboard;
import Elements.PacmanActor;
import Elements.PacmanGame;
import Elements.PacmanGame.State;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 * Title class.
 *
 * Displays the title card
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Title extends PacmanActor {
    
    private boolean pushSpaceToStartVisible;

    public Title(PacmanGame game) {
        super(game);
    }

    /**
     * 
     */

    @Override
    public void init() {
        loadFrames("/res/title.png");
        x = 21;
        y = 100;
    }

    /**
     * Update of the attributes of the class depending on the time (currentTimeMillis)
     * case 4 : start the game if the space key is pressed
     */

    @Override
    public void updateTitle() {
        yield:
        while (true) {

            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 500) {
                        break yield;
                    }
                    instructionPointer = 2;
                case 2:
                    double dy = 100 - y;
                    y = y + dy * 0.1;
                    if (Math.abs(dy) < 1) {
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 3;
                    }
                    break yield;
                case 3:
                    if (System.currentTimeMillis() - waitTime < 200) {
                        break yield;
                    }
                    instructionPointer = 4;
                case 4:
                    pushSpaceToStartVisible = ((int) (System.nanoTime() * 0.0000000075) % 3) > 0;
                    if (Keyboard.keyPressed[KeyEvent.VK_M]) {
                        game.setIAMode("manual");
                    } else if (Keyboard.keyPressed[KeyEvent.VK_L]) {
                        game.setIAMode("minmax");
                    }
                    if (Keyboard.keyPressed[KeyEvent.VK_SPACE]) {
                        game.startGame();
                    }
                    break yield;
            }
        }
    }

    /**
     * Displays the credits when the game is launched
     * @param g
     */
    @Override
    public void draw(Graphics2D g) {
        if (visible) {
            super.draw(g);
            if (pushSpaceToStartVisible) {
                game.drawText(g, "PUSH SPACE TO START", 37, 170);
            }
            game.drawText(g, "PUSH M/L TO CHANGE AI MODE",6,50);
            game.drawText(g, "AI BY TEAM PIACMAN 2019", 19,225);
            game.drawText(g, "PROGRAMMED BY O.L. 2017", 20, 240);
            game.drawText(g, "ORIGINAL GAME BY NAMCO 1980", 5, 255);
        }

    }

    
    // broadcast messages
    
    @Override
    public void stateChanged() {
        visible = false;
        if (game.state == State.TITLE) {
            y = -150;
            visible = true;
            pushSpaceToStartVisible = false;
            instructionPointer = 0;
        }
    }
        
}
