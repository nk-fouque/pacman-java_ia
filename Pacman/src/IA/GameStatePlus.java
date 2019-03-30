package IA;

import Elements.PacmanGame;
import Elements.actor.Ghost;

import java.awt.*;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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

    /**
     * Constructor : initialize ghost's values with actual ghost's values
     *
     * @param game      the game
     * @param lastInput the last move of Pacman
     */
    public GameStatePlus(PacmanGame game, int lastInput) {
        super(game, lastInput);
        for (Ghost g : ghosts) {
            ghostRows[g.type] = g.getRow();
            ghostCols[g.type] = g.getCol();
            ghostModes[g.type] = g.getMode();
        }
    }

    /**
     * Constructor that use an existing gamestate
     *
     * @param game        the game
     * @param lastInput   the last move of Pacman
     * @param dir         the current direction of Pacman
     * @param newScore    the score of Pacman
     * @param willBeSuper
     * @param row
     * @param col
     * @param ghostRows
     * @param ghostCols
     * @param ghostModes
     */
    public GameStatePlus(PacmanGame game, int lastInput, int dir, int newScore, boolean willBeSuper, int row, int col, int[] ghostRows, int[] ghostCols, Ghost.Mode[] ghostModes) {
        super(game, lastInput, dir, newScore, willBeSuper, row, col);
        System.arraycopy(ghostRows, 0, this.ghostRows, 0, 4);
        System.arraycopy(ghostCols, 0, this.ghostCols, 0, 4);
        System.arraycopy(ghostModes, 0, this.ghostModes, 0, 4);

    }

    /**
     * Create the possible following states : if pacman goes in one out of 4 possible directions
     * wallRight/Left/Up/Down check if the path is clear
     * lastInput in the possible directions is not used (it doesn't work)
     * 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     *
     * @return an array with the 4 possible gamestates
     */
    @Override
    public GameState[] possibleFollowingStates() { //TODO I think if we replace these walls tests with a 3D matrix [col][row][direction] containing booleans, we might solve our tunnel problems and other things
        GameStatePlus[] res = new GameStatePlus[4];
        if (!wallRight()) {
            if (lastInput != 2 || uTurnAllowed) {
                res[0] = new GameStatePlus(game, lastInput, 0, newScore, willBeSuper, pacmanRow, pacmanCol, ghostRows, ghostCols, ghostModes);
            } else {
                if (verbose) System.out.println("Right is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on right");
        }
        if (!wallDown()) {
            if (lastInput != 3 || uTurnAllowed) {
                res[1] = new GameStatePlus(game, lastInput, 1, newScore, willBeSuper, pacmanRow, pacmanCol, ghostRows, ghostCols, ghostModes);
            } else {
                if (verbose) System.out.println("Down is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on down");
        }
        if (!wallLeft()) {
            if (lastInput != 0 || uTurnAllowed) {
                res[2] = new GameStatePlus(game, lastInput, 2, newScore, willBeSuper, pacmanRow, pacmanCol, ghostRows, ghostCols, ghostModes);
            } else {
                if (verbose) System.out.println("Left is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on left");
        }
        if (!wallUp()) {
            if (lastInput != 1 || uTurnAllowed) {
                res[3] = new GameStatePlus(game, lastInput, 3, newScore, willBeSuper, pacmanRow, pacmanCol, ghostRows, ghostCols, ghostModes);
            } else {
                if (verbose) System.out.println("Up is U turn");
            }
        } else {
            if (verbose) System.out.println("Wall on up");
        }
        return res;
    }

    /**
     * Give the path taken by the ghosts depending on their position
     *
     * @param ghostRow
     * @param ghostCol
     * @param fw
     * @return
     */
    public int shortestPathToPacman(int ghostRow, int ghostCol, FloydWarshall fw, boolean vulnerable) {
        int[][][][] d = fw.d;
        int distRight = d[ghostRow][ghostCol + 1][pacmanRow][pacmanCol];
        int distDown = d[ghostRow + 1][ghostCol][pacmanRow][pacmanCol];
        int distLeft = d[ghostRow][ghostCol - 1][pacmanRow][pacmanCol];
        int distUp = d[ghostRow - 1][ghostCol][pacmanRow][pacmanCol];
        int value;
        if (!vulnerable) {
            value = Math.min(
                    Math.min(distUp, distDown), Math.min(distLeft, distRight)
            );
        } else {
            if(distRight>1000)distRight=0;
            if(distDown>1000)distDown=0;
            if(distLeft>1000)distLeft=0;
            if(distUp>1000)distUp=0;
            value = Math.max(
                    Math.max(distUp, distDown), Math.max(distLeft, distRight)
            );
        }

        ArrayList<Integer> choice = new ArrayList<>();
        if (distRight == value) choice.add(0);
        if (distDown == value) choice.add(1);
        if (distLeft == value) choice.add(2);
        if (distUp == value) choice.add(3);

        int res = choice.get(ThreadLocalRandom.current().nextInt(0, choice.size()));

        return res;
    }

    /**
     * Update the ghost's moves with their direction
     */
    public void moveGhosts(FloydWarshall fw) {
        for (int i = 0; i < 4; i++) {
            int direction = shortestPathToPacman(ghostRows[i], ghostCols[i], fw,ghostModes[i]== Ghost.Mode.VULNERABLE);
            ghostRows[i] += dRow(direction);
            ghostCols[i] += dCol(direction);
        }
    }

    public int searchBestGamestate(int depth, FloydWarshall fw) {
        PathfindTree tree = new PathfindTree(this);
        tree.node(depth, fw);
        return tree.choose();
    }
}
