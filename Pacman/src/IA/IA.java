package IA;

import Elements.PacmanGame;
import Elements.actor.Pacman;
import Elements.infra.Keyboard;
import main.Main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class IA {

    private final int[] uTurn = new int[]{2, 3, 0, 1};
    private int lastInput;
    private boolean verbose = Main.verbose;
    private FloydWarshall fw;
    private ArrayList<Integer>[][] possibleMoves;

    private Pacman pacman;

    //Change this string depending on what you want to do
    //manual is literally manual input where the player directs Pacman, maddog is random and eatmax is bugged
    //The real good one is "minmax"
    private String mode;

    public IA(Pacman pacman){
        this.lastInput=3;
        this.fw = new FloydWarshall();
        fw.algoFW();
        System.out.println("Floyd Warshall Ready");
        possibleMoves=fw.possibleMoves();
        this.mode = pacman.game.getIAMode();
        this.pacman=pacman;
    }

    public void changeMode(PacmanGame game){
        if (Keyboard.keyPressed[KeyEvent.VK_M]) {
            game.setIAMode("manual");
        } else if (Keyboard.keyPressed[KeyEvent.VK_L]) {
            game.setIAMode("minmax");
        }
        mode = game.getIAMode();
    }

    public int askDirection(PacmanGame game){
        int res = pacman.getDesiredDirection();
        switch(mode){
            case "maddog": {
                res =  randDirection(game);
                break;
            }
            case "eatmax": {
                res = askDirectionEatmaxTreeSearch(game);
                break;
            }
            case "minmax": {
                res = askDirectionMinMaxTreeSearch(game);
                break;
            }
            case "manual": {
                if (Keyboard.keyPressed[KeyEvent.VK_LEFT]) {
                    res = 2;
                } else if (Keyboard.keyPressed[KeyEvent.VK_RIGHT]) {
                    res = 0;
                } else if (Keyboard.keyPressed[KeyEvent.VK_UP]) {
                    res = 3;
                } else if (Keyboard.keyPressed[KeyEvent.VK_DOWN]) {
                    res = 1;
                }
                break;
            }
        }
        return res;
    }

    /**
     * Return a random direction for Pacman
     * @return 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     */
    public int randDirection(PacmanGame game){
        int res;
        do{
            res = ThreadLocalRandom.current().nextInt(0, 4); //Chooses a random direction
        }while(res == uTurn[pacman.getDesiredDirection()]);                 //Checks if the chosen direction isn't the opposite of the way we're currently headed to avoid staying stuck
        return res;
    }

    /**
     * Use a treesearch {@link PathfindTree} to find the best gamestate
     * @param game the current game
     * @return the new direction of Pacman
     */
    public int askDirectionEatmaxTreeSearch(PacmanGame game){
        GameState state = new GameState(game,lastInput);
        int res = state.searchBestGamestate(8);
        if(verbose) System.out.println("\n");
        lastInput=res; //Records the input before sending it
        return res;
    }

    public int askDirectionMinMaxTreeSearch(PacmanGame game){
        GameStatePlus state = new GameStatePlus(game,lastInput,possibleMoves);
        int res = state.searchBestGamestate(8,fw);
        if(verbose) System.out.println("\n");
        lastInput=res; //Records the input before sending it
        return res;
    }
    

    
    
    
    
    
    

}
