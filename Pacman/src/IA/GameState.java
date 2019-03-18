package IA;

import Elements.*;
import Elements.actor.*;
import Elements.infra.*;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Stores important information about the board, temporary class until we can play real games
 */
public class GameState {
    /**
     * Information stored about the game
     */
    public PacmanGame game;
    public ArrayList<Ghost> ghosts = new ArrayList<>();
    public ArrayList<Food> food= new ArrayList<>();
    public ArrayList<PowerBall> powerBalls= new ArrayList<>();

    public int pacmanRow;
    public int pacmanCol;

    /**
     * Activates the debug prints
     */
    private boolean verbose = true;

    /**
     * Stores the score once calculated
     */
    public int newScore;

    /**
     * indicates if this gamestate was created from a direction : 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     * -1 = no dir
     */
    public int dir;

    /**
     * Base constructor
     * @param game
     */
    public GameState(PacmanGame game){
        this.game = game;
        this.newScore=game.getScoreInt();
        this.dir = -1;
        for(Actor a : game.actors){
            if (a instanceof Ghost){
                ghosts.add((Ghost)a);
            }
            if(a instanceof Food){
                food.add((Food)a);
            }
            if(a instanceof PowerBall ){
                powerBalls.add((PowerBall)a);
            }
            if(a instanceof Pacman ){
                this.pacmanCol = ((Pacman) a).getCol();
                this.pacmanRow = ((Pacman) a).getRow();
            }
        }
    }

    /**
     * Constructor for "future gamestates"
     * @param game
     * @param dir : the direction we will have input to get there
     */
    public GameState(PacmanGame game,int dir){
        this.game = game;
        this.newScore=game.getScoreInt();
        this.dir= dir;
        for(Actor a : game.actors){
            if (a instanceof Ghost){
                ghosts.add((Ghost)a);
            }
            if(a instanceof Food){
                food.add((Food)a);
            }
            if(a instanceof PowerBall ){
                powerBalls.add((PowerBall)a);
            }
            if(a instanceof Pacman ){
                this.pacmanRow = ((Pacman) a).getRow()+dRow();
                this.pacmanCol = ((Pacman) a).getCol()+dCol();
            }
        }
    }

    /**
     * converts a direction into a vertical deplacement
     * @return 1 = down, -1 = down
     */
    public int dRow(){
        int res = 0;
        switch(dir){
            case 1 : {
                res++;
                break;
            }
            case 3 : {
                res--;
                break;
            }
        }
        return res;
    }

    /**
     * converts a direction into a horizontal deplacement
     * @return 1 = right, -1 = left
     */
    public int dCol(){
        int res = 0;
        switch(dir){
            case 0 : {
                res++;
                break;
            }
            case 2 : {
                res--;
                break;
            }
        }
        return res;
    }

    /**
     * Currently factors eating food, capturing a ghost and dying
     * @return the new score if we obtain this gamestate
     */
    public int newScore(){
        newScore+=pacmanEat();
        int getghost = pacmanGetGhost();
        if (getghost == -1){
            newScore = 0;
        } else {
            newScore += (game.catchedGhostScoreTable[game.currentCatchedGhostScoreTableIndex]*getghost);
        }

        return newScore;
    }

    /**
     * Checks if pacman will be able to eat there and updates the score
     * @return
     */
    public int pacmanEat(){
        int res = 0;
        if (pacmanGetFood()){
            res += 10;
        }
        return res;
    }

    public boolean pacmanGetFood(){
        boolean res = false;
        for(Food f : food){
            if (f.getCol()==pacmanCol+dCol() && f.getRow() == pacmanRow+dRow() && f.visible==true){
                res= true;
                break;
            }
        }
        return res;
    }

    /**
     *
     * @return 0 if no ghost, 1 if vulnerable ghost, -1 if normal ghost
     */
    public int pacmanGetGhost(){
        int res = 0;
        for(Ghost g : ghosts){
            if (g.getCol()==pacmanCol+dCol() && g.getRow() == pacmanRow+dRow() && g.visible==true){
                if (g.markAsVulnerable){
                    res = 1;
                } else {
                    res = -1;
                }
                break;
            }
        }
        return res;
    }

    /**
     * Check if pacman has walls around him
     * @return
     */
    public boolean wallRight(){
        return this.game.maze[pacmanRow][pacmanCol+1]==-1;
    }

    public boolean wallLeft(){
        return this.game.maze[pacmanRow][pacmanCol-1]==-1;
    }

    public boolean wallDown(){
        return this.game.maze[pacmanRow+1][pacmanCol]==-1;
    }

    public boolean wallUp(){
        return this.game.maze[pacmanRow-1][pacmanCol]==-1;
    }

    /**
     * Tries every different positions
     * @return all possible GameStates
     */
    public GameState[] possibleFollowingStates(){
       GameState[] res = new GameState[4];
        if(!wallRight()) {
            res[0]=new GameState(game,0);
        } else {
            if(verbose) System.out.println("Wall on right");
        }
        if (!wallDown()) {
            res[1] = new GameState(game,1);
        } else {
            if (verbose) System.out.println("Wall on down");
        }
        if (!wallLeft()) {
            res[2] = new GameState(game,2);
        } else {
            if (verbose) System.out.println("Wall on left");
        }
        if (!wallUp()) {
            res[3] = new GameState(game,3);
        } else {
            if (verbose) System.out.println("Wall on up");
        }
        return res;
    }


    /**
     * Activate tree search
     * @param depth
     * @return
     */
    public int searchBestGamestate(int depth){
        PathfindNode tree = new PathfindNode(this);
        tree.node(depth);
        return tree.bestDirection;
    }



}
