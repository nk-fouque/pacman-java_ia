package Elements.actor;


import Elements.PacmanActor;
import Elements.PacmanGame;
import Elements.PacmanGame.State;
import java.awt.Graphics2D;

/**
 * OLPresents class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class OLPresents extends PacmanActor {
    
    private String text = "YOLO Matthieu \n j'ai deja trouve \n ou ecrire";
    private int textIndex;

    public OLPresents(PacmanGame game) {
        super(game);
    }

    @Override
    public void updateOLPresents() {
        yield:
        while (true) {
            switch (3) { //originally switch instructionPointer
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    while (System.currentTimeMillis() - waitTime < 100) {
                        break yield;
                    }
                    textIndex++;
                    if (textIndex < text.length()) {
                        instructionPointer = 0;
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 2;
                case 2:
                    while (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    visible = false;
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 3;
                case 3:
//                    while (System.currentTimeMillis() - waitTime < 1500) {
//                        break yield;
//                    }
                    game.setState(State.READY);
                    break yield;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (!visible) {
            return;
        }
        game.drawText(g, text.substring(0, textIndex), 60, 130);
    }

    
    // broadcast messages
    
    @Override
    public void stateChanged() {
        visible = false;
        if (game.state == State.OL_PRESENTS) {
            visible = true;
            textIndex = 0;

        }
    }
        
}