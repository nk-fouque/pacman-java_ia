package IA;

import Elements.PacmanGame;
import Elements.actor.Ghost;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class iis an extension of GameState, it stores more different information
 * It's been made separate, to allow the use of "eatmax" AI that doesn't need as much information
 */
public class GameStatePlus extends GameState {

    /**
     * Bonus attribut to define the ghosts
     * ghostRows[0] is the vertical position of the red ghost (type = 0)
     * ghostCols[0] is the horizontal position of the red ghost (type = 0)
     * ghostModes[0] is the mode of the red ghost (type = 0)
     * Red = 0 : pink = 1 ; cyan = 2 ; orange = 3
     */
    public int[] ghostRows = new int[4];                //the ghosts' respective vertical positions
    public int[] ghostCols = new int[4];                //the ghosts' respective horizontal positions
    public Ghost.Mode[] ghostModes = new Ghost.Mode[4]; //the ghosts' respective states

    /**
     * An two dimentional matrix, containing, for every position of pacman an Arraylist of the valid directions to go
     * passed in constructor by the IA call
     */
    public ArrayList<Integer>[][] possibleMoves;

    /**
     * Constructor : initialize ghost's values with current ghost's values
     * @param game      the game
     * @param lastInput the last move of Pacman
     */
    public GameStatePlus(PacmanGame game, int lastInput, ArrayList<Integer>[][] possibleMoves) {
        super(game, lastInput);
        for (Ghost g : ghosts) {
            ghostRows[g.type] = g.getRow();
            ghostCols[g.type] = g.getCol();
            ghostModes[g.type] = g.getMode();
        }
        this.possibleMoves=possibleMoves;
    }

    /**
     * Constructor used by an existing gamestate,
     * copies its informations either changed in constructor (like pacman's position) or to be changed by another method (like the ghosts' positions)
     * @param game        the game
     * @param lastInput   the last move of Pacman
     * @param dir         the direction used to move Pacman to this state, used to change its position
     * @param newScore    the score of Pacman
     * @param willBeSuper whether Pacman can eat a Powerball before this state
     * @param row         pacman's vertical position
     * @param col         pacman's horizontal position
     */
    public GameStatePlus(PacmanGame game, int lastInput, int dir, int newScore, boolean willBeSuper, int row, int col, int[] ghostRows, int[] ghostCols, Ghost.Mode[] ghostModes, ArrayList<Integer>[][] possibleMoves) {
        super(game, lastInput, dir, newScore, willBeSuper, row, col);
        // Need arraycopy because these will be changed by the moveghosts
        System.arraycopy(ghostRows, 0, this.ghostRows, 0, 4);
        System.arraycopy(ghostCols, 0, this.ghostCols, 0, 4);
        System.arraycopy(ghostModes, 0, this.ghostModes, 0, 4);
        this.possibleMoves=possibleMoves;
    }

    /**
     * Create the possible following states : if pacman goes in one out of 4 possible directions
     * lastInput in the possible directions is not used (it doesn't work)
     * 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     * @return an array with the 4 possible gamestates
     */
    @Override
    public GameState[] possibleFollowingStates() { //TODO I think if we replace these walls tests with a 3D matrix [col][row][direction] containing booleans, we might solve our tunnel problems and other things
        GameStatePlus[] res = new GameStatePlus[4];
        try {
            for(Integer i : possibleMoves[pacmanRow][pacmanCol]){ //For every position, the possible moves are already know so it directly checks the "possiblemoves" matrix
                res[i] = new GameStatePlus(game, lastInput, i, newScore, willBeSuper, pacmanRow, pacmanCol, ghostRows, ghostCols, ghostModes,possibleMoves);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * The real purpose of this class
     * Update the ghost's moves using shortestPathToPacman to evaluate the easiest way for the ghost to get to/away from Pacman
     * This is what makes it a "MinMax" Algorithm
     * (considering that the best move for a ghost is to go towards Pacman when it's able to eat it and away from Pacman when Pacman would eat it)
     * Not an Expectimax since we don't account for the ScatterMode of the ghosts (the mode where they have a bit of a chaotic behavior)
     */
    public void moveGhosts(FloydWarshall fw) {
        for (int i = 0; i < 4; i++) {
            int direction = shortestPathToPacman(ghostRows[i], ghostCols[i], fw,ghostModes[i]== Ghost.Mode.VULNERABLE);
            ghostRows[i] += dRow(direction);
            ghostCols[i] += dCol(direction);
        }
    }

    /**
     * Give the path taken by the ghosts depending on their position
     * @param ghostRow the ghost's current vertical position
     * @param ghostCol the ghost's current horizontal position
     * @param fw the FloydWarshall item passed in parameters until there serves its purpose here, it has the distance from every point to every point of the maze
     * @param vulnerable whether the ghost is in its vulnerable state (= can be eaten by Pacman)
     *                   IMPORTANT : In these commentaries, "it" refers to the "simulated" ghost, the ghost's behavior according to the AI
     * @return
     */
    public int shortestPathToPacman(int ghostRow, int ghostCol, FloydWarshall fw, boolean vulnerable) {
        int[][][][] d = fw.d;                                               //Gets the distance  matrix from the FloydWarshall
        int distRight = d[ghostRow][ghostCol + 1][pacmanRow][pacmanCol];    //We will wonder for every direction
        int distDown = d[ghostRow + 1][ghostCol][pacmanRow][pacmanCol];     //if the simulated ghost goes there, how far will it be from pacman
        int distLeft = d[ghostRow][ghostCol - 1][pacmanRow][pacmanCol];
        int distUp = d[ghostRow - 1][ghostCol][pacmanRow][pacmanCol];
        int value;
        if (!vulnerable) {                                                      //If the ghost isn't vulnerable
            value = Math.min(                                                   //It will understand that the best action is to get closest to pacman
                    Math.min(distUp, distDown), Math.min(distLeft, distRight)
            );
        } else {                                                                //If it's vulnerable
            //Changes every move that  would put the ghost in a wall into a zero to avoid consideration
            if(distRight>1000)distRight=0;
            if(distDown>1000)distDown=0;
            if(distLeft>1000)distLeft=0;
            if(distUp>1000)distUp=0;
            //Then decides that the best action is the one that puts it furthest from pacman
            value = Math.max(
                    Math.max(distUp, distDown), Math.max(distLeft, distRight)
            );
        }
        //To solve the case where several positions have the same result a list is created with every "direction" (in the form of integer) leading to the best distance
        ArrayList<Integer> choice = new ArrayList<>();
        if (distRight == value) choice.add(0);
        if (distDown == value) choice.add(1);
        if (distLeft == value) choice.add(2);
        if (distUp == value) choice.add(3);
        //Then picks a direction at random in said list
        int res = choice.get(ThreadLocalRandom.current().nextInt(0, choice.size()));

        return res;
    }

    /**
     * Evaluates known (updated) positions of the ghosts and check potential encounter with pacman
     * @return 0 if no ghost, 1 if vulnerable ghost, -1 if normal ghost
     */
    @Override
    public int pacmanGetGhost() {
        int res = 0;
        for (Ghost g : ghosts) {
            if ((ghostCols[g.type] == pacmanCol || g.getCol()==pacmanCol) && (ghostRows[g.type] == pacmanRow||g.getRow()==pacmanRow) && g.visible == true) {
                if(verbose) System.out.print("Ghost on "+g.getRow()+":"+g.getCol());
                if (ghostModes[g.type]== Ghost.Mode.VULNERABLE || willBeSuper) {
                    res = 1;
                    if(verbose) System.out.println(", good");
                } else {
                    res = -1;
                    if(verbose) System.out.println(", not good");
                }
                break;
            }
        }
        return res;
    }

    public int searchBestGamestate(int depth, FloydWarshall fw) {
        PathfindTree tree = new PathfindTree(this);
        tree.node(depth, fw); //Uses the version of node that will pass down the FloydWarshall item
        return tree.choose();
    }
}
