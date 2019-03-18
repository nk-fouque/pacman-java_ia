package Elements.actor;

import Elements.infra.ShortestPathFinder;
import Elements.PacmanActor;
import Elements.PacmanGame;
import Elements.PacmanGame.State;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * A new ghost class (not sure it works because i need to test the waitTime but hey i tried
 * 
 * @author Gregre
 */
public class Ghosts extends PacmanActor {
    
    public Pacman pacman;
    public int type;
    // Start of the ghosts
    public Point[] initialPositions = { 
        new Point(18, 11), new Point(16, 14), 
        new Point(18, 14), new Point(20, 14)};
    public int cageUpDownCount;

    
    /**
     * Trying to code better ghosts
     * @author Gregre
     */
    public static int scatterCount;	// tentative de scatter mode
    public Point[] scatterDestinations = { 
            new Point(0, 31), new Point(0, 2), 
            new Point(33, 0), new Point(33, 33)};
    
    /**
     * State machine of the ghost
     *
     */
    public static enum Mode { CAGE, SCATTER, NORMAL, VULNERABLE, DIED }
    /**
     * Actual state of the ghost
     */
    public Mode mode = Mode.CAGE;
    
    /**
     * Position of the ghost
     */
    public int dx;
    public int dy;
    public int col;
    public int row;
    
    public int direction = 0;
    public int lastDirection;
    
    public List<Integer> desiredDirections = new ArrayList<Integer>();
    public int desiredDirection;
    public static final int[] backwardDirections = { 2, 3, 0, 1 };
    
    public long vulnerableModeStartTime;
    public boolean markAsVulnerable;
    
    // in this version, i'm using path finder just to return the ghost to the center (cage)
    public ShortestPathFinder pathFinder;
    
    
    /**
     * Constructor 
     * @param game the current game
     * @param pacman the target of the ghost
     * @param type the color and behavior of the ghost (0 is red, 1 is pink, 2 is blue, 3 is orange)
     */
    public Ghosts(PacmanGame game, Pacman pacman, int type) {
        super(game);
        this.pacman = pacman;
        this.type = type;
        this.pathFinder = new ShortestPathFinder(game.maze);
        this.scatterCount = 0; // FIXME tentative de meilleur scatter mode
    }

    /**
     * Setter of the mode (the state of the ghost)
     * @param mode the next mode
     */
    private void setMode(Mode mode) {
        this.mode = mode;
        //modeChanged();
    }
    
    /**
     * Initialisation of the view and collider of the ghost
     */
    @Override
    public void init() {
        String[] ghostFrameNames = new String[8 + 4 + 4];
        for (int i=0; i<8; i++) {	// from 0 to 7 = frames of 'ghost'
            ghostFrameNames[i] = "/res/ghost_" + type + "_" + i + ".png";
        }
        for (int i=0; i<4; i++) {	// from 8 to 11 = frames of 'ghost_vulnerable'
            ghostFrameNames[8 + i] = "/res/ghost_vulnerable_" + i + ".png";
        }
        for (int i=0; i<4; i++) {	// from 12 to 15 = frames of 'ghost_died'
            ghostFrameNames[12 + i] = "/res/ghost_died_" + i + ".png";
        }
        loadFrames(ghostFrameNames);
        collider = new Rectangle(0, 0, 8, 8);
        setMode(Mode.CAGE);
    }
    
    /**
     * Getter of the X coordinate between the 2 scales
     * @param col the coordinate in the grid scale
     * @return the coordinate in the other scale
     */
    private int getTargetX(int col) {
        return col * 8 - 3 - 32;
    }

    /**
     * Getter of the Y coordinate between the 2 frames
     * @param row the coordinate in the grid frame
     * @return the coordinate in the other frame
     */
    private int getTargetY(int row) {
        return (row + 3) * 8 - 2;
    }

    /**
     * Update the collider position
     */
    public void updatePosition() {
        x = getTargetX(col);
        y = getTargetY(row);
    }
    
    /**
     * Update the ghost position
     * @param col the X coordinate in the grid frame
     * @param row the Y position in the grid frame
     */
    private void updatePosition(int col, int row) {
        this.col = col;
        this.row = row;
        updatePosition();
    }
    
    /**
     * Move in the direction of a given target (using its coordinates)
     * @param targetX the X coordinate of the target
     * @param targetY the Y coordinate of the target
     * @param velocity the speed of the ghost
     * @return boolean (if the ghost has moved or not)
     */
    private boolean moveToTargetPosition(int targetX, int targetY, int velocity) {
        int sx = (int) (targetX - x);
        int sy = (int) (targetY - y);
        int vx = Math.abs(sx) < velocity ? Math.abs(sx) : velocity;
        int vy = Math.abs(sy) < velocity ? Math.abs(sy) : velocity;
        int idx = vx * (sx == 0 ? 0 : sx > 0 ? 1 : -1);
        int idy = vy * (sy == 0 ? 0 : sy > 0 ? 1 : -1);
        x += idx;
        y += idy;
        return sx != 0 || sy != 0;
    }

    /**
     * Move in the direction of the point (col,row) in the grid
     * @param col the X coordinate of the destination
     * @param row the Y coordinate of the destination
     * @param velocity the speed of the ghost
     * @return boolean (if the ghost has moved or not via {@link #moveToTargetPosition(int,int,int)} )
     */
    private boolean moveToGridPosition(int col, int row, int velocity) {
        int targetX = getTargetX(col);
        int targetY = getTargetY(row);
        return moveToTargetPosition(targetX, targetY, velocity);
    }
    
    /**
     * Adjust the movement while going out of the grid
     */
    private void adjustHorizontalOutsideMovement() {
        if (col == 1) {
            col = 34;
            x = getTargetX(col);
        }
        else if (col == 34) {
            col = 1;
            x = getTargetX(col);
        }
    }
    
    /**
     * Update the frame of the title depending on pacman direction
     */
    @Override
    public void updateTitle() {
        int frameIndex = 0;
        x = pacman.x + 17 + 17 * type;
        y = 200;
        if (pacman.direction == 0) {
            frameIndex = 8 + (int) (System.nanoTime() * 0.00000001) % 2;
        }
        else if (pacman.direction == 2) {
            frameIndex = 2 * pacman.direction + (int) (System.nanoTime() * 0.00000001) % 2;
        }
        frame = frames[frameIndex];
    }
    
    /**
     * Update the animation of the ghost depending on its state
     * {@link updateAnimation()}
     */
    @Override
    public void updatePlaying() {
        switch (mode) {
            case CAGE: updateGhostCage(); break;
            case SCATTER: updateGhostScatter(); break;
            case NORMAL: updateGhostNormal(); break;
            case VULNERABLE: updateGhostVulnerable(); break;
            case DIED: updateGhostDied(); break;
        }
        updateAnimation();
    }

    /**
     * Update the frames of the ghost
     */
    public void updateAnimation() {
        int frameIndex = 0;
        switch (mode) {
            case CAGE: 
            case SCATTER:
            case NORMAL:
                frameIndex = 2 * direction + (int) (System.nanoTime() * 0.00000001) % 2;
                if (!markAsVulnerable) {
                    break;
                }
            case VULNERABLE:
                if (System.currentTimeMillis() - vulnerableModeStartTime > 5000*60/game.FPS) {
                    frameIndex = 8 + (int) (System.nanoTime() * 0.00000002) % 4;
                }
                else {
                    frameIndex = 8 + (int) (System.nanoTime() * 0.00000001) % 2;
                }
                break;
            case DIED:
                frameIndex = 12 + direction;
                break;
        }
        frame = frames[frameIndex];
    }

    /** 
     * FIXME tentative de meilleur scatter mode
     * @author Gregre is trying to code better ghost : Scatter mode
     */
    private void updateGhostScatter() {
    	waitTime = System.currentTimeMillis();
    	Point scatterDestination = scatterDestinations[type];
    	updateGhostMovement(true, scatterDestination.x, scatterDestination.y, 1, pacmanCatchedAction, 0,1,2,3);
    	if(scatterCount < 2) {
    		while(System.currentTimeMillis()-waitTime < 7000*60/game.FPS) {
    			// Scatter for 7 seconds
    		}
    	}else{
    		while(System.currentTimeMillis()-waitTime < 5000*60/game.FPS) {
    			// Scatter for 5 seconds
    		}
    	}
    	scatterCount++;
    setMode(Mode.NORMAL);
    }
    
    
    /**
     * Update the behavior of the ghost while it's in cage mode
     */
    private void updateGhostCage() {
        yield:
        while (true) {
            switch (type) {
                case 0:
                	setMode(Mode.NORMAL);
	                break yield;
                case 1:
                	setMode(Mode.NORMAL);
                	break yield;
                case 2:		// Inky doesn't get out of the cage before 30 foods have been eaten
                	if((game.totalFood - game.currentFoodCount)>30) {
                		setMode(Mode.NORMAL);
                	}
                	break yield;
                case 3:		// Clyde doesn't get out of the cage before a third of the foods have been eaten
                	if((game.totalFood - game.currentFoodCount)>(game.totalFood/3)) {
                		setMode(Mode.NORMAL);
                	}
                	break yield;
            }
        }
    }
    
    private PacmanCatchedAction pacmanCatchedAction = new PacmanCatchedAction();
    
    /**
     * Pacman died : victory of ghosts
     * @author Gregre
     *
     */
    private class PacmanCatchedAction implements Runnable {
        @Override
        public void run() {
            game.setState(State.PACMAN_DIED);
        }
    }
    
    
    /** 
     * FIXME tentative de meilleur normal/chase mode
     * 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
     * @author Gregre is trying to code better ghost : Chase mode
     */
    private void updateGhostNormal() {
    	waitTime = System.currentTimeMillis();
    	if (checkVulnerableModeTime() && markAsVulnerable) {
            setMode(Mode.VULNERABLE);
            markAsVulnerable = false;
        }
    	
    	// for debbuging purposes
//      if (Keyboard.keyPressed[KeyEvent.VK_Q] && type == 0) {
//          game.currentCatchedGhostScoreTableIndex = 0;
//          game.ghostCatched(Ghost.this);
//      }
//      else if (Keyboard.keyPressed[KeyEvent.VK_W] && type == 1) {
//          game.currentCatchedGhostScoreTableIndex = 0;
//          game.ghostCatched(Ghost.this);
//      }
//      else if (Keyboard.keyPressed[KeyEvent.VK_E] && type == 2) {
//          game.currentCatchedGhostScoreTableIndex = 0;
//          game.ghostCatched(Ghost.this);
//      }
//      else if (Keyboard.keyPressed[KeyEvent.VK_R] && type == 3) {
//          game.currentCatchedGhostScoreTableIndex = 0;
//          game.ghostCatched(Ghost.this);
//      }
      
    	/**
    	 * Old method
    	 */
    /*
      if (type == 0 || type == 1) {
          updateGhostMovement(true, pacman.col, pacman.row, 1, pacmanCatchedAction, 0, 1, 2, 3); // chase movement
      }
      else {
          updateGhostMovement(false, 0, 0, 1, pacmanCatchedAction, 0, 1, 2, 3); // random movement
      }
     */
    	
    	yield:
    		while(true) {
    			switch(type) {
    				case 0:	// Red ghost : Blinky
    					updateGhostMovement(true, pacman.col, pacman.row, 1, pacmanCatchedAction, 0, 1, 2, 3);
    					break yield;
    				case 1:	// Pink ghost : Pinky
    					int target_col;
    					int target_row;
    					switch(pacman.direction) {	// @TODO try to anticipate pacman direction but 4 tiles ahead
    						case 0:
    							target_col = pacman.col;
    							target_row = pacman.row + 1;
    							updateGhostMovement(true, target_col, target_row, 1, pacmanCatchedAction, 0, 1, 2, 3);
    							break;
							case 1:
								target_col = pacman.col - 1;
    							target_row = pacman.row;
    							updateGhostMovement(true, target_col, target_row, 1, pacmanCatchedAction, 0, 1, 2, 3);
    							break;
							case 2:
								target_col = pacman.col;
    							target_row = pacman.row - 1;
    							updateGhostMovement(true, target_col, target_row, 1, pacmanCatchedAction, 0, 1, 2, 3);
    							break;
							case 3:
								target_col = pacman.col + 1;
    							target_row = pacman.row;
    							updateGhostMovement(true, target_col, target_row, 1, pacmanCatchedAction, 0, 1, 2, 3);
    							break;
    					}
    					break yield;
    				case 2:	// Cyan ghost : Inky
    					waitTime = System.currentTimeMillis();
    					if(System.currentTimeMillis() - waitTime > 3000*60/game.FPS) {	// change d'objectif toutes les 3 secondes, en attendant de trouver comment g�n�rer le vrai algo 
    						int rand_col = 1 + (int) (Math.random() * 32);
    						int rand_row = 1 + (int) (Math.random() * 32);
    						updateGhostMovement(true, rand_col, rand_row, 1, pacmanCatchedAction, 0, 1, 2, 3);
    					}
    					break yield;
    				case 3:	// Orange ghost : Clyde
    					if((Math.pow((pacman.col - col), 2) + Math.pow((pacman.row - row), 2))>64) {	// if pacman is far of more than 8 tiles
    						updateGhostMovement(true, pacman.col, pacman.row, 1, pacmanCatchedAction, 0, 1, 2, 3);
    					}else {
    						setMode(Mode.SCATTER);
    					}
    					break yield;
    			}
    		}
    }
    
    /**
     * FIXME uncomment to make it work
     * @author Gregre
     */
    private GhostCatchedAction ghostCatchedAction = new GhostCatchedAction();
    
    
    /**
     * The ghosts dies
     *
     */
    
    /**
     * FIXME uncomment to make it work
     * @author Gregre
     */
    private class GhostCatchedAction implements Runnable {
        @Override
        public void run() {
            //game.ghostCatched(Ghosts.this);
        }
    }
         
    
    /**
     * Vulnerable mode of the ghosts : unmarked them to avoid keeping them in the state then run away movement. Stop the vulnerable state after 8s
     */
    private void updateGhostVulnerable() {
        if (markAsVulnerable) {
            markAsVulnerable = false;
        }
        
        /**
         * FIXME uncomment to make it work
         * @author Gregre
         */
        updateGhostMovement(true, pacman.col, pacman.row, 1, ghostCatchedAction, 2, 3, 0, 1); // run away movement
        
        
        // return to normal mode after 8 seconds
        if (!checkVulnerableModeTime()) {
            setMode(Mode.NORMAL);
        }
    }
    
    /**
     * Chronometer for the vulnerable mode
     * @return true after 8 seconds
     */
    private boolean checkVulnerableModeTime() {
        return System.currentTimeMillis() - vulnerableModeStartTime <= 8000*60/game.FPS;
    }
    
    /**
     * Update the behavior of the ghost when they died : they go beck to the cage using a pathfinder
     */
    private void updateGhostDied() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    pathFinder.find(col, row, 18, 11);
                    instructionPointer = 1;
                case 1:
                    if (!pathFinder.hasNext()) {
                        instructionPointer = 3;
                        continue yield;
                    }
                    Point nextPosition = pathFinder.getNext();
                    col = nextPosition.x;
                    row = nextPosition.y;
                    instructionPointer = 2;
                case 2:
                    if (!moveToGridPosition(col, row, 4)) {
                        if (row == 11 && (col == 17 || col == 18)) {
                            instructionPointer = 3;
                            continue yield;
                        }
                        instructionPointer = 1;
                        continue yield;
                    }
                    break yield;
                case 3:
                    if (!moveToTargetPosition(105, 110, 4)){
                        instructionPointer = 4;
                        continue yield;
                    }
                    break yield;
                case 4:
                    if (!moveToTargetPosition(105, 134, 4)){
                        instructionPointer = 5;
                        continue yield;
                    }
                    break yield;
                case 5:
                    setMode(Mode.CAGE);
                    instructionPointer = 4;
                    break yield;
            }
        }
    }    
    
    
    
    
    /**
     * Make the ghosts move
     * @param useTarget true if the ghost look for a target, false otherwise
     * @param targetCol the target x-coordinate in grid frame
     * @param targetRow the target y-coordinate in grid frame
     * @param velocity speed of the ghost
     * @param collisionWithPacmanAction {@link GhostCatchedAction}
     * @param desiredDirectionsMap coordinate of the goal
     */
    private void updateGhostMovement(boolean useTarget, int targetCol, int targetRow
            , int velocity, Runnable collisionWithPacmanAction, int ... desiredDirectionsMap) {
        
    	/**
    	 * Update the desiredDirections
    	 */
        desiredDirections.clear();
        if (useTarget) {				// if the ghost has a target, change the desiredDirections
            if (targetCol - col > 0) {
                desiredDirections.add(desiredDirectionsMap[0]);
            }
            else if (targetCol - col < 0) {
                desiredDirections.add(desiredDirectionsMap[2]);
            }
            if (targetRow - row > 0) {
                desiredDirections.add(desiredDirectionsMap[1]);
            }
            else if (targetRow - row < 0) {
                desiredDirections.add(desiredDirectionsMap[3]);
            }
        }
        if (desiredDirections.size() > 0) {
            int selectedChaseDirection = (int) (desiredDirections.size() * Math.random());
            desiredDirection = desiredDirections.get(selectedChaseDirection);
        }
        
        /**
         * Manage the movement of the ghosts
         */
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    if ((row == 14 && col == 1 && lastDirection == 2) 
                            || (row == 14 && col == 34 && lastDirection == 0)) {
                        adjustHorizontalOutsideMovement();
                    }
                    
                    double angle = Math.toRadians(desiredDirection * 90);
                    dx = (int) Math.cos(angle);
                    dy = (int) Math.sin(angle);
                    if (useTarget && game.maze[row + dy][col + dx] == 0
                            && desiredDirection != backwardDirections[lastDirection]) {
                        
                        direction = desiredDirection;
                    }
                    else {
                        do {
                            direction = (int) (4 * Math.random());
                            angle = Math.toRadians(direction * 90);
                            dx = (int) Math.cos(angle);
                            dy = (int) Math.sin(angle);
                        }
                        while (game.maze[row + dy][col + dx] == -1               
                            || direction == backwardDirections[lastDirection]);
                    }
                    
                    col += dx;
                    row += dy;
                    instructionPointer = 1;
                case 1:
                    if (!moveToGridPosition(col, row, velocity)) {
                        lastDirection = direction;
                        instructionPointer = 0;
                        // adjustHorizontalOutsideMovement();
                    }
                    if (collisionWithPacmanAction != null && checkCollisionWithPacman()) {
                        collisionWithPacmanAction.run();
                    }
                    break yield;
            }
        }        
    }

    /**
     * Display of the death of the ghost
     */
    @Override
    public void updateGhostCatched() {
        if (mode == Mode.DIED) {
            updateGhostDied();
            updateAnimation();
        }
    }

    /**
     * Set the ghosts at the start of the game
     */
    @Override
    public void updatePacmanDied() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 1500*60/game.FPS) {
                        break yield;
                    }
                    visible = false;
                    setMode(Mode.CAGE);
                    updateAnimation();
                    break yield;
            }
        }
        updateAnimation();
    }

    /**
     * I don't understand the difference between this methode and {@link updatePacmanDied()}
     */
    @Override
    public void updateLevelCleared() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 1500*60/game.FPS) {
                        break yield;
                    }
                    visible = false;
                    setMode(Mode.CAGE);
                    updateAnimation();
                    instructionPointer = 2;
                case 2:
                    break yield;
            }
        }
    }
   
    /**
     * Check if the ghost touch pacman
     * @return if the pacman and the ghost touched
     */
    private boolean checkCollisionWithPacman() {
        pacman.updateCollider();
        updateCollider();
        return pacman.collider.intersects(collider);
    }

    /**
     * Update of the collider depending on the position of the ghost
     */
    @Override
    public void updateCollider() {
        collider.setLocation((int) (x + 4), (int) (y + 4));
    }
    
    
    private void modeChanged() {
    	updatePlaying();
    }
    
    // broadcast messages

    /**
     * Display : Set the state of the ghost depending on the state of the game
     */
    @Override
    public void stateChanged() {
        if (game.getState() == PacmanGame.State.TITLE) {
            updateTitle();
            visible = true;
        }
        else if (game.getState() == PacmanGame.State.READY) {
            visible = false;
        }
        else if (game.getState() == PacmanGame.State.READY2) {
            setMode(Mode.CAGE);
            updateAnimation();
            Point initialPosition = initialPositions[type];
            updatePosition(initialPosition.x, initialPosition.y); // col, row
            x -= 4;
        }
        else if (game.getState() == PacmanGame.State.PLAYING && mode != Mode.CAGE) {
            instructionPointer = 0;
        }
        else if (game.getState() == PacmanGame.State.PACMAN_DIED) {
            instructionPointer = 0;
        }
        else if (game.getState() == PacmanGame.State.LEVEL_CLEARED) {
            instructionPointer = 0;
        }
    }
    
    public void showAll() {
        visible = true;
    }

    public void hideAll() {
        visible = false;
    }
    
    public void startGhostVulnerableMode() {
        vulnerableModeStartTime = System.currentTimeMillis();
        markAsVulnerable = true;
    }
    
    public void died() {
        setMode(Mode.DIED);
    }
        
}
