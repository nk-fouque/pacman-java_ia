package IA;

import Elements.*;
import Elements.actor.*;
import Elements.infra.*;
import main.Main;

import java.util.ArrayList;

/**
 * Stores important information about the board, those are the information to take in account to evaluate the input possibilities
 */
public class GameState {
    /**
     * Information stored about the game
     */
    public PacmanGame game;                                             //The game it came from
    protected ArrayList<Ghost> ghosts = new ArrayList<>();             //Direct informations about the ghosts
    private ArrayList<Food> food = new ArrayList<>();                    //Direct informations about the food on the board
    private ArrayList<PowerBall> powerBalls = new ArrayList<>();        //Direct informations about the powerballs on the board
    protected int pacmanRow;                                           //Coordinates of current pacman position
    protected int pacmanCol;
    protected boolean willBeSuper;                                     //Wether Pacman can become super-pacman by this gamestate (only useful in future states)

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
    private int dir;
    public int lastInput;

    /**
     * Base constructor
     * links the state to the game and stores diverse information,
     * some fixed at this point in time,
     * some (that don't need that to be effectively treated) stay dynamic adresses.
     */
    public GameState(PacmanGame game, int lastInput) {
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
        this.willBeSuper = false;
    }

    /**
     * Constructor for "future gamestates"
     *
     * @param dir       : the direction we will have gone to get there, only for "future gamestates"
     * @param row       : pacman's new row location
     * @param col       : pacman's new column location
     * @param newScore  : the new score by this gamestate
     * @param lastInput I don't know why but it has to stay here even if we don't use it or else there are some bugs
     */
    public GameState(PacmanGame game, int lastInput, int dir, int newScore, boolean willBeSuper, int row, int col) {
        this.game = game;
        this.newScore = newScore;
        this.dir = dir;
        this.lastInput = dir;
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
        this.pacmanRow = row + dRow(dir);
        this.pacmanCol = col + dCol(dir);
        if (pacmanCol > 33) pacmanCol = 2;
        if (pacmanCol < 2) pacmanCol = 33;
        this.willBeSuper = willBeSuper;
    }

    /**
     * converts a direction (in "pacman control" language) into a vertical deplacement
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
     * converts a direction (in "pacman control" language) into a horizontal deplacement
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
            newScore = 0;
        } else {
            try {
                newScore += (game.catchedGhostScoreTable[game.currentCatchedGhostScoreTableIndex] * getghost);
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {          //Only happens when it wonders if it can eat a ghost despite the fact that it's already eaten,
                //linked to the ghost teleportation problem
                System.out.println("Too many ghosts");
                e.printStackTrace();
            }
        }

        return newScore;
    }

    /**
     * Checks if pacman will be able to eat there and updates the score
     *
     * @return
     */
    public int pacmanEat() {
        int res = 0;
        if (pacmanOnFood()) {
            res += 10;
        }
        return res;
    }

    /**
     * What complicates things is that food and powerballs aren't removed from the Actors when they are eaten,
     * they only set their visible boolean to false
     *
     * @return true if pacman will encounter food
     */
    public boolean pacmanOnFood() {
        boolean res = false;
        for (Food f : food) {                                           //For every food on the board
            if (f.getCol() == pacmanCol && f.getRow() == pacmanRow      //If it's on the same coordinates as pacman
                    && f.visible == true) {                             //and hasn't been eaten
                res = true;                                             //return true
                break;
            }
        }
        return res;
    }

    /**
     *
     */
    public void becomeSuper() {
        for (PowerBall p : powerBalls) {                                 //For every Powerball on the board
            if (p.getCol() == pacmanCol && p.getRow() == pacmanRow       //If it's on the same coordinates as pacman
                    && p.visible == true) {                              //and hasn't been eaten
                willBeSuper = true;                                      //the boolean willbesuper will make it known to
                //pacman that in future states it can eat ghosts
                break;
            }
        }
    }

    /**
     * Checks if  we will encounter a ghost on the current case, and return depending on the state of the ghost
     * @return 0 if no ghost, 1 if vulnerable ghost, -1 if normal ghost
     */
    public int pacmanGetGhost() {
        int res = 0;
        for (Ghost g : ghosts) {
            if (g.getCol() == pacmanCol && g.getRow() == pacmanRow && g.visible == true) {
                if (verbose) System.out.print("Ghost on " + g.getRow() + ":" + g.getCol());
                if (g.mode == Ghost.Mode.VULNERABLE || willBeSuper) {
                    res = 1;
                    if (verbose) System.out.println(", good");
                } else {
                    res = -1;
                    if (verbose) System.out.println(", not good");
                }
                break;
            }
        }
        return res;
    }

    /**
     * Check if pacman has walls around him
     *
     * @return true if there's a wall on the concerned direction
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
     * Very clunky, only used by the "eatmax" AI, conserved for archiving's sake
     *
     * @return all possible GameStates, in an Array, in usual order : Right,down,left,up
     */
    public GameState[] possibleFollowingStates() {
        GameState[] res = new GameState[4];
        if (!wallRight()) {
            if (lastInput != 2 || uTurnAllowed) {  //This boolean checks if
                res[0] = new GameState(game, lastInput, 0, newScore, willBeSuper, pacmanRow, pacmanCol);
            } else {
                if (verbose) System.out.println("Right is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on right");
        }
        if (!wallDown()) {
            if (lastInput != 3 || uTurnAllowed) {
                res[1] = new GameState(game, lastInput, 1, newScore, willBeSuper, pacmanRow, pacmanCol);
            } else {
                if (verbose) System.out.println("Down is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on down");
        }
        if (!wallLeft()) {
            if (lastInput != 0 || uTurnAllowed) {
                res[2] = new GameState(game, lastInput, 2, newScore, willBeSuper, pacmanRow, pacmanCol);
            } else {
                if (verbose) System.out.println("Left is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on left");
        }
        if (!wallUp()) {
            if (lastInput != 1 || uTurnAllowed) {
                res[3] = new GameState(game, lastInput, 3, newScore, willBeSuper, pacmanRow, pacmanCol);
            } else {
                if (verbose) System.out.println("Up is U turn");
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
