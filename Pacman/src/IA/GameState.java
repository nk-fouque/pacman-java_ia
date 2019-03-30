package IA;

import Elements.*;
import Elements.actor.*;
import Elements.actor.Ghost.Mode;
import Elements.infra.*;
import main.Main;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Stores important information about the board, temporary class until we can play real games
 *
 * Test
 */
public class GameState {
    /**
     * Information stored about the game
     */
    public PacmanGame game;
    public ArrayList<Ghost> ghosts = new ArrayList<>();
    public ArrayList<Food> food = new ArrayList<>();
    public ArrayList<PowerBall> powerBalls = new ArrayList<>();

    public int pacmanRow;
    public int pacmanCol;
    public boolean willBeSuper;
    


    /**
     * Activates the debug prints
     */
    protected boolean verbose = Main.verbose;

    /**
     * Debug feature,
     * TODO ultimately should be able to run with true but this tends to make him hesitate too much in empty spaces
     */
    protected boolean uTurnAllowed = false;

    /**
     * Stores the score once calculated
     */
    public int newScore;

    /**
     * indicates if this gamestate was created from a direction : 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     * -1 = no dir
     */
    public int dir;


    public int lastInput;

    /**
     * Base constructor
     *
     * @param game
     */
    public GameState(PacmanGame game,int lastInput) {
        this.game = game;
        this.newScore = game.getScoreInt();
        this.dir = -1;
        this.lastInput = lastInput;
        for (Actor a : game.actors) {
            if (a instanceof Ghost) {
                ghosts.add((Ghost) a);
            }
            if (a instanceof Food) {
                food.add((Food) a);
            }
            if (a instanceof PowerBall) {
                powerBalls.add((PowerBall) a);
            }
            if (a instanceof Pacman) {
                this.pacmanCol = ((Pacman) a).getCol();
                this.pacmanRow = ((Pacman) a).getRow();
            }
        }
        this.willBeSuper=false;
    }

    /**
     * Constructor for "future gamestates"
     *
     * @param game
     * @param dir  : the direction we will have gone to get there, only for "future gamestates"
     */
    public GameState(PacmanGame game, int lastInput, int dir,int newScore,boolean willBeSuper, int row, int col) {
        this.game = game;
        this.newScore = newScore;
        this.dir = dir;
        this.lastInput=dir;
        for (Actor a : game.actors) {
            if (a instanceof Ghost) {
                ghosts.add((Ghost) a);
            }
            if (a instanceof Food) {
                food.add((Food) a);
            }
            if (a instanceof PowerBall) {
                powerBalls.add((PowerBall) a);
            }
        }
        this.pacmanRow = row+dRow(dir);
        this.pacmanCol = col+dCol(dir);
        this.willBeSuper=willBeSuper;
    }

    /**
     * converts a direction into a vertical deplacement
     *
     * @return 1 = down, -1 = up
     */
    public int dRow(int direction) {
        int res = 0;
        switch (direction) {
            case 1: {
                res++;
                break;
            }
            case 3: {
                res--;
                break;
            }
        }
        return res;
    }

    /**
     * converts a direction into a horizontal deplacement
     *
     * @return 1 = right, -1 = left
     */
    public int dCol(int direction) {
        int res = 0;
        switch (direction) {
            case 0: {
                res++;
                break;
            }
            case 2: {
                res--;
                break;
            }
        }
        return res;
    }

    public int newCol(){
        int res = pacmanCol + dCol(dir);
        if(res < 2){
            res = 33;
        } else if (res > 33) {
            res = 2;
        }
        return res;
    }

    public int newRow(){
        return pacmanRow + dRow(dir);
    }

    /**
     * Currently factors eating food, capturing a ghost and dying
     *
     * @return the new score if we obtain this gamestate
     */
    public int newScore() {
        newScore += pacmanEat();
        becomeSuper();
        int getghost = pacmanGetGhost();
        if (getghost == -1) {
            newScore = -1000;
        } else {
            newScore += (game.catchedGhostScoreTable[game.currentCatchedGhostScoreTableIndex] * getghost);
        }

        return newScore;
    }

    /**
     * Checks if pacman will be able to eat there and updates the score
     * @return
     */
    public int pacmanEat() {
        int res = 0;
        if (pacmanGetFood()) {
            res += 10;
        }
        if(pacmanOnFood()){
            res += 10;
        }
        return res;
    }

    public boolean pacmanGetFood() {
        boolean res = false;
        for (Food f : food) {
            if (f.getCol() == newCol() && f.getRow() == newRow() && f.visible == true) {
                res = true;
                break;
            }
        }
        return res;
    }

    public boolean pacmanOnFood() {
        boolean res = false;
        for (Food f : food) {
            if (f.getCol() == pacmanCol && f.getRow() == pacmanRow && f.visible == true) {
                res = true;
                break;
            }
        }
        return res;
    }

    /**
     *
     */
    public void becomeSuper(){
        for (PowerBall p : powerBalls) {
            if (p.getCol() == newCol() && p.getRow() == newRow() && p.visible == true) {
                willBeSuper = true;
                break;
            }
        }
    }

    /**
     * @return 0 if no ghost, 1 if vulnerable ghost, -1 if normal ghost
     */
    public int pacmanGetGhost() {
        int res = 0;
        for (Ghost g : ghosts) {
            if (g.getCol() == pacmanCol && g.getRow() == pacmanRow && g.visible == true) {
                System.out.println();
                if (g.mode== Ghost.Mode.VULNERABLE || willBeSuper) {
                    res = 1;
                } else if(g.mode==Mode.NORMAL||g.mode==Mode.SCATTER){
                    res = -1;
                }
                break;
            }
        }
        return res;
    }

    /**
     * Check if pacman has walls around him
     *
     * @return
     */
    public boolean wallRight() {
        return this.game.maze[pacmanRow][pacmanCol + 1] < 0;
    }

    public boolean wallLeft() {
        return this.game.maze[pacmanRow][pacmanCol - 1] < 0;
    }

    public boolean wallDown() {
        return this.game.maze[pacmanRow + 1][pacmanCol] < 0;
    }

    public boolean wallUp() {
        return this.game.maze[pacmanRow - 1][pacmanCol] < 0;
    }

    /**
     * Tries every different positions
     *
     * @return all possible GameStates
     */
    public GameState[] possibleFollowingStates() {
        GameState[] res = new GameState[4];
        if (!wallRight()) {
            if (lastInput!=2 || uTurnAllowed) {
                res[0] = new GameState(game,lastInput, 0,newScore,willBeSuper,pacmanRow,pacmanCol);
            } else {
                if(verbose) System.out.println("Right is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on right");
        }
        if (!wallDown()) {
            if (lastInput!=3 || uTurnAllowed) {
                res[1] = new GameState(game,lastInput, 1,newScore,willBeSuper,pacmanRow,pacmanCol);
            } else {
                if(verbose) System.out.println("Down is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on down");
        }
        if (!wallLeft()) {
            if (lastInput!=0 || uTurnAllowed) {
                res[2] = new GameState(game,lastInput, 2,newScore,willBeSuper,pacmanRow,pacmanCol);
            } else {
                if(verbose) System.out.println("Left is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on left");
        }
        if (!wallUp()) {
            if (lastInput!=1 || uTurnAllowed) {
                res[3] = new GameState(game,lastInput, 3,newScore,willBeSuper,pacmanRow,pacmanCol);
            } else {
                if(verbose) System.out.println("Up is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on up");
        }
        return res;
    }


    /**
     * Activate tree search
     *
     * @param depth
     * @return
     */
    public int searchBestGamestate(int depth) {
        PathfindTree tree = new PathfindTree(this);
        tree.node(depth);
        return tree.choose();
    }

}
