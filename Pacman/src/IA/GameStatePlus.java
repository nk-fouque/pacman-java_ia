package IA;

import Elements.PacmanGame;
import Elements.actor.Ghost;
import Elements.infra.ShortestPathFinder;

import java.awt.*;

public class GameStatePlus extends GameState {

    /**
     * Bonus attribut to define the ghosts
     * ghostRows[0] is the vertical position of the red ghost (type = 0)
     * ghostCols[0] is the horizontal position of the red ghost (type = 0)
     * ghostModes[0] is the mode of the red ghost (type = 0)
     * Red = 0 : pink = 1 ; cyan = 2 ; orange = 3
     */
    public int[] ghostRows = new int[4];
    public int[] ghostCols = new int[4];
    public Ghost.Mode[] ghostModes = new Ghost.Mode[4];
    public ShortestPathFinder pathFinder;

	/** 
	 * Constructor : initialize ghost's values with actual ghost's values
	 * @param game the game
	 * @param lastInput the last move of Pacman
	 */
    public GameStatePlus(PacmanGame game, int lastInput){
        super(game,lastInput);
        for(Ghost g : ghosts) {
            ghostRows[g.type]=g.getRow();
            ghostCols[g.type]=g.getCol();
            ghostModes[g.type]=g.getMode();
        }
        this.pathFinder = new ShortestPathFinder(game.maze);
    }

    /** 
     * Constructor that use an existing gamestate
     * @param game the game
     * @param lastInput the last move of Pacman
     * @param dir the current direction of Pacman
     * @param newScore the score of Pacman
     * @param willBeSuper 
     * @param row 
     * @param col 
     * @param ghostRows 
     * @param ghostCols 
     * @param ghostModes 
     */
    public GameStatePlus(PacmanGame game, int lastInput, int dir, int newScore, boolean willBeSuper, int row, int col, int[] ghostRows,int[] ghostCols, Ghost.Mode[] ghostModes){
        super(game,lastInput,dir,newScore,willBeSuper,row,col);
        System.arraycopy(ghostRows,0,this.ghostRows,0,4);
        System.arraycopy(ghostCols,0,this.ghostCols,0,4);
        System.arraycopy(ghostModes,0,this.ghostModes,0,4);

    }

    /** 
     * Create the possible following states : if pacman goes in one out of 4 possible directions
     * wallRight/Left/Up/Down check if the path is clear
     * lastInput in the possible directions is not used (it doesn't work)
     * 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     * @return an array with the 4 possible gamestates
     */
    @Override
    public GameState[] possibleFollowingStates() {
        GameStatePlus[] res = new GameStatePlus[4];
        if (!wallRight()) {
            if (lastInput!=2 || uTurnAllowed) {
                res[0] = new GameStatePlus(game,lastInput, 0,newScore,willBeSuper,pacmanRow,pacmanCol,ghostRows,ghostCols,ghostModes);
            } else {
                if(verbose) System.out.println("Right is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on right");
        }
        if (!wallDown()) {
            if (lastInput!=3 || uTurnAllowed) {
                res[1] = new GameStatePlus(game,lastInput, 1,newScore,willBeSuper,pacmanRow,pacmanCol,ghostRows,ghostCols,ghostModes);
            } else {
                if(verbose) System.out.println("Down is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on down");
        }
        if (!wallLeft()) {
            if (lastInput!=0 || uTurnAllowed) {
                res[2] = new GameStatePlus(game,lastInput, 2,newScore,willBeSuper,pacmanRow,pacmanCol,ghostRows,ghostCols,ghostModes);
            } else {
                if(verbose) System.out.println("Left is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on left");
        }
        if (!wallUp()) {
            if (lastInput!=1 || uTurnAllowed) {
                res[3] = new GameStatePlus(game,lastInput, 3,newScore,willBeSuper,pacmanRow,pacmanCol,ghostRows,ghostCols,ghostModes);
            } else {
                if(verbose) System.out.println("Up is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on up");
        }
        return res;
    }

    /** 
     * Give the path taken by the ghosts depending on their position
     * @param ghostRow 
     * @param ghostCol
     * @return
     */
    public int shortestPathToPacman(int ghostRow,int ghostCol){
        return 0 ; //TODO
    }

    /** 
     * Update the ghost's moves with their direction
     */
    public void moveGhosts(){
        for(int i = 0;i<4;i++){
            int direction = shortestPathToPacman(ghostRows[i],ghostCols[i]);
            ghostRows[i]+=dRow(direction);
            ghostCols[i]+=dCol(direction);
        }
    }

    @Override
    public int searchBestGamestate(int depth) {
        PathfindTree tree = new PathfindTree(this,true);
        tree.node(depth);
        return tree.choose();
    }
}
