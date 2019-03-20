package Elements;

import Elements.infra.Actor;
import Elements.infra.Game;
import Elements.actor.Background;
import Elements.actor.Food;
import Elements.actor.GameOver;
import Elements.actor.Ghost;
import Elements.actor.HUD;
import Elements.actor.Initializer;
import Elements.actor.OLPresents;
import Elements.actor.Pacman;
import Elements.actor.Point;
import Elements.actor.PowerBall;
import Elements.actor.Ready;
import Elements.actor.Title;
import main.Main;

import java.awt.Dimension;
import java.awt.geom.Point2D;

/**
 * PacmanGame class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 *
 * Creates the maze and makes all the functions for the whole game itself
 */
public class PacmanGame extends Game {

    // maze[row][col] 
    // 36 x 31 
    // cols: 0-3|4-31|32-35

    //After launching the game, all the 2 and 3 are replaced by 0, the cases we can walk on
    //After launching the game, the 1 are replaced by -1, the walls
    public int maze[][] = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,1,1,1},
        {1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1},
        {1,1,1,1,1,3,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,3,1,1,1,1,1},
        {1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1},
        {1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,1,1,1,1},
        {1,1,1,1,1,2,1,1,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,1,1,2,1,1,1,1,1},
        {1,1,1,1,1,2,1,1,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,1,1,2,1,1,1,1,1},
        {1,1,1,1,1,2,2,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,2,2,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,2,1,1,1,1,1,0,1,1,0,1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,2,1,1,1,1,1,0,1,1,0,1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,2,1,1,0,0,0,0,0,0,0,0,0,0,1,1,2,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,2,1,1,0,1,1,0,0,0,0,1,1,0,1,1,2,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,2,2,2,2,2,2,2,0,0,0,1,1,0,0,0,0,1,1,0,0,0,2,2,2,2,2,2,2,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,2,1,1,0,0,0,0,0,0,0,0,0,0,1,1,2,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,1,1,1},
        {1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1},
        {1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1},
        {1,1,1,1,1,3,2,2,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,1,2,2,3,1,1,1,1,1},
        {1,1,1,1,1,1,1,2,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,2,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,2,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,2,1,1,1,1,1,1,1},
        {1,1,1,1,1,2,2,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,2,2,1,1,1,1,1},
        {1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,1,1,1,1,2,1,1,1,1,1},
        {1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,1,1,1,1,2,1,1,1,1,1},
        {1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    };

    public enum State { INITIALIZING, OL_PRESENTS, TITLE, READY, READY2
        , PLAYING, PACMAN_DIED, GHOST_CATCHED, LEVEL_CLEARED, GAME_OVER }
    
    public State state = State.INITIALIZING;
    public static int LIVES = 3; //TODO Modify to change the base number of lives
    public int lives = LIVES;
    private int score;
    private int hiscore;
    
    public Ghost catchedGhost;
    public int currentCatchedGhostScoreTableIndex = 0;
    public final int[] catchedGhostScoreTable = { 200, 400, 800, 1600 };
    
    public int foodCount;
    public int currentFoodCount;
    public int totalFood;


    
    public PacmanGame(boolean verbose) {
        screenSize = new Dimension(224, 288);
        screenScale = new Point2D.Double(2, 2);
        FPS = 60; // Used only by the base game
    }

    public PacmanGame(boolean verbose, long fps) {
        screenSize = new Dimension(224, 288);
        screenScale = new Point2D.Double(2, 2);
        FPS = fps;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (this.state != state) {
            this.state = state;
            broadcastMessage("stateChanged");
        }
    }
    
    public void addScore(int point) {
        score += point;
        if (score > hiscore) {
            hiscore = score;
        }
    }

    public int getScoreInt(){
        return score;
    }
    
    public String getScore() {
        String scoreStr = "0000000" + score;
        scoreStr = scoreStr.substring(scoreStr.length() - 7, scoreStr.length());
        return scoreStr;
    }

    public String getHiscore() {
        String hiscoreStr = "0000000" + hiscore;
        hiscoreStr = hiscoreStr.substring(hiscoreStr.length() - 7, hiscoreStr.length());
        return hiscoreStr;
    }
    
    @Override
    public void init() {    //FIXME (Matt) : function slowing the game at start, long time for setup
        addAllObjs();   //FIXME (Matt) : we have to make a second version of these 2, to init a game with the current state of our game, to resume from "main" game position
        initAllObjs();
    }
    
    private void addAllObjs() {
        Pacman pacman = new Pacman(this);
        if(main.Main.visibleGame) {
            actors.add(new Initializer(this));
            actors.add(new OLPresents(this));
            actors.add(new Title(this));
            actors.add(new Background(this));
        }else{
            this.startGame();
        }
        foodCount = 0;
        for (int row=0; row<31; row++) {
            for (int col=0; col<36; col++) {
                if (maze[row][col] == 1) {
                    maze[row][col] = -1; // wall convert to -1 for ShortestPathFinder
                }
                else if (maze[row][col] == 2) {
                    maze[row][col] = 0;
                    actors.add(new Food(this, col, row));
                    foodCount++;
                }
                else if (maze[row][col] == 3) {
                    maze[row][col] = 0;
                    actors.add(new PowerBall(this, col, row));
                }
            }
        }
        for (int i=0; i<4; i++) {
            actors.add(new Ghost(this, pacman, i));
        }
        actors.add(pacman);
        actors.add(new Point(this, pacman));
        actors.add(new Ready(this));
        actors.add(new GameOver(this));
        actors.add(new HUD(this));
        totalFood = foodCount;
    }
    
    private void initAllObjs() {
        for (Actor actor : actors) {
            actor.init();
        }
    }
    
    // ---

    public void restoreCurrentFoodCount() {
        currentFoodCount = foodCount;
    }

    public boolean isLevelCleared() {
        return currentFoodCount == 0;
    }
    
    public void startGame() {
        setState(State.READY);
    }
    
    public void startGhostVulnerableMode() {
        currentCatchedGhostScoreTableIndex = 0;
        broadcastMessage("startGhostVulnerableMode");
    }
    
    public void ghostCatched(Ghost ghost) {
        catchedGhost = ghost;
        setState(State.GHOST_CATCHED);
    }
    
    public void nextLife() {
        lives--;
        if (lives == 0) {
            setState(State.GAME_OVER);
        }
        else {
            setState(State.READY2);
        }
    }

    public void levelCleared() {
        setState(State.LEVEL_CLEARED);
    }

    public void nextLevel() {
        setState(State.READY);
    }

    public void returnToTitle() {
        lives = LIVES;
        score = 0;
        setState(State.TITLE);
    }

    public void returnToReady() {
        lives = LIVES;
        score = 0;
        setState(State.READY);
    }
    
}
