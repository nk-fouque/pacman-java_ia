package IA;
import Elements.PacmanGame;
import Elements.actor.Ghost;
import Elements.actor.Pacman;
import Elements.infra.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import main.Main;

public class ExpectiMax {
	
	public Pacman pacman;
	public List<Ghost> ghosts;

	public ExpectiMax(GameState state,Pacman pacman) {
		 bestDirection = new ArrayList<>();
	        bestScore = -1;
	        states=state.possibleFollowingStates();
	        scoreByDir = new int[4];
	        for(int i = 0;i<4;i++){
	            scoreByDir[i]=-1;
	        }
	        ghosts = state.ghosts;
	        this.pacman = pacman;
	}
	
	/**
     * Stores the best score
     */
    public int bestScore;

    /**
     * Stores all the directions leading to the best score (as integers)
     */
    public List<Integer> bestDirection;

    /**
     * Stores the GameStates to build the sons
     */
    private GameState[] states;

    /**
     * For each direction, stores the score
     */
    private int[] scoreByDir;

    /**
     * Activates debug prints, can be set to true, false or @Link Main.verbose
     */
	public boolean verbose = Main.verbose;
	public int verboseLevel = 6;
	
	 /**
     * Called when at maximum depth
     *
     */
    public void leaf() {
        for(int i = 0;i<4;i++){                                     // states has 4 cases
            if(!Objects.isNull(states[i])) {                        // some of them are null because the direction wasn't valid, so we don't treat them
                scoreByDir[i] = states[i].newScore();               // we calculate the "score if we go there"
                if (verbose ) System.out.println("Dir : "+i+" Score : "+ scoreByDir[i]);
                consider(i);                                        // then we decide what to do with it
            }
        }
        if(verbose) System.out.println("Leaf : "+bestDirection.toString()+" Score : "+bestScore);
    }

    public void leaf(FloydWarshall fw) {
        for(int i = 0;i<4;i++){                                     // states has 4 cases
            if(!Objects.isNull(states[i])) {                        // some of them are null because the direction wasn't valid, so we don't treat them
                ((GameStatePlus)states[i]).moveGhosts(fw);        	// FIXME decides if we consider the ghosts' moves or not
                scoreByDir[i] = states[i].newScore();               // we calculate the "score if we go there"
                if (verbose ) System.out.println("Dir : "+i+" Score : "+ scoreByDir[i]);
                consider(i);                                        // then we decide what to do with it
            }
        }
        if(verbose) System.out.println("Leaf : "+bestDirection.toString()+" Score : "+bestScore);
    }

    public void node(int depth){
        if(depth == 0){                                             // When at max depth
            leaf();                                                 // we directly evaluate the directions
        } else {                                                    // Else
            PathfindTree[] sons = new PathfindTree[4];              // we build the sons an array to store the sons
            for(int i = 0;i<4;i++){                                 // TODO This for block is where we will put the multi thread if we put one
                if(!Objects.isNull(states[i])) {                    // If the direction is valid (i.e. no walls)
                    states[i].newScore();                           // we calculate the "score if we get there"
                    sons[i] = new PathfindTree(states[i]);          // we build a son from it
                    sons[i].node(depth -1);                   		// and call the node function recursively
                    scoreByDir[i]=sons[i].bestScore;                // then we get the best score the son is capable of
                }
                consider(i);                                        // and decide if we want to follow this son
            }
            if(verbose||depth==verboseLevel) System.out.println("Node Level "+depth+" : "+bestDirection+" Score : "+bestScore);
        }
    }

    public void node(int depth,FloydWarshall fw){
        if(depth == 0){                                             // When at max depth
            leaf(fw);                                                 // we directly evaluate the directions
        } else {                                                    // Else
            PathfindTree[] sons = new PathfindTree[4];              // we build the sons an array to store the sons
            for(int i = 0;i<4;i++){                                 // TODO This for block is where we will put the multi thread if we put one
                if(!Objects.isNull(states[i])) {                    // If the direction is valid (i.e. no walls)
                    ((GameStatePlus)states[i]).moveGhosts(fw);      // FIXME decides if we consider the ghosts' moves or not
                    states[i].newScore();                           // we calculate the "score if we get there"
                    sons[i] = new PathfindTree(states[i]);          // we build a son from it
                    sons[i].node(depth -1,fw);                		// and call the node function recursively
                    scoreByDir[i]=sons[i].bestScore;                // then we get the best score the son is capable of
                    consider(i);                                    // and decide if we want to follow this son
                }
            }
            if(verbose||depth>=verboseLevel) System.out.println("Node Level "+depth+" : "+bestDirection+" Score : "+bestScore);
        }
    }
    
    public void consider(int direction) {
    	if (scoreByDir[direction]> bestScore){                      // if we find a better direction to gp
            bestDirection.clear();                                  // we forget the previous best directions
            bestDirection.add(direction);                           // and we add this one
            bestScore = scoreByDir[direction];                      // and we update the best possible score
        } else if (scoreByDir[direction] == bestScore){             // If we find a direction that's as good as the current one
            bestDirection.add(direction);                           // We add it to the best directions
        }
    }
	
    /**
     * Code de l'IA TicTacToe
     * @param ttt
     * @param c
     * @param alpha
     * @param beta
     * @return
     */
    public int MaxValue(PacmanGame game, int score, int depth, FloydWarshall fw) {
		int res = -1;
		//int opti = -1;
		if (game.state == game.State.LEVEL_CLEARED ) {
			return score; // L'IA gagne
		} else if (game.state == game.State.PACMAN_DIED) {
				return -1; // L'IA perd
		} else {								// 10pts par boule, 200 puis 400 puis 800 puis 1600 par fant�me
			
			if(depth == 0){                                             // When at max depth
				for(int i = 0;i<4;i++){                                     // states has 4 cases
		            if(!Objects.isNull(states[i])) {                        // some of them are null because the direction wasn't valid, so we don't treat them
		                ((GameStatePlus)states[i]).moveGhosts(fw);        	// FIXME decides if we consider the ghosts' moves or not
		                scoreByDir[i] = states[i].newScore();               // we calculate the "score if we go there"
		                if (verbose ) System.out.println("Dir : "+i+" Score : "+ scoreByDir[i]);
		                consider(i);                                        // then we decide what to do with it
		            }
		        }
		        if(verbose) System.out.println("Leaf : "+bestDirection.toString()+" Score : "+bestScore);                                                 // we directly evaluate the directions
	        } else {                                                    // Else
	            ExpectiMax[] sons = new ExpectiMax[4];              	// we build the sons an array to store the sons
	            for(int i = 0;i<4;i++){                                 // TODO This for block is where we will put the multi thread if we put one
	                if(!Objects.isNull(states[i])) {                    // If the direction is valid (i.e. no walls)
	                    ((GameStatePlus)states[i]).moveGhosts(fw);      // FIXME decides if we consider the ghosts' moves or not
	                    states[i].newScore();                           // we calculate the "score if we get there"
	                    sons[i] = new ExpectiMax(states[i],this.pacman);          // we build a son from it
	                    sons[i].MinValue(game, states[i].newScore ,depth -1,fw);     // and call the node function recursively FIXME c'est pas score
	                    scoreByDir[i]=sons[i].bestScore;                // then we get the best score the son is capable of
	                    consider(i);                                    // and decide if we want to follow this son
	                }
	            }
	            if(verbose||depth>=verboseLevel) System.out.println("Node Level "+depth+" : "+bestDirection+" Score : "+bestScore);
	        }
			
			
			
			v = MinValue(game, score + 10);	//cas o� pacman va vers une bouboule
			
			v = MinValue(game, score + 50);	// cas o� pacman va vers une powerball
			
			return v;

	}

	public int MinValue(PacmanGame game, int score, int depth, FloydWarshall fw) {
		if (game.state == game.state.LEVEL_CLEARED ) {
			return score; // L'IA gagne
		} else if (game.state == game.state.PACMAN_DIED) {
				return -1; // L'IA perd
		} else {
			int[] dirGhosts = {0,0,0,0};
			for(int i=0;i<4;i++) {
				if(ghosts.get(i).getMode()==ghosts.get(i).mode.VULNERABLE) {
					dirGhosts[i] = scaredGhostChoice(ghosts.get(i).col,ghosts.get(i).row);
				}else {
					switch (i) {
						case 0:
							dirGhosts[i] = aggressiveGhostChoice(ghosts.get(i).col,ghosts.get(i).row,pacman.getCol(),pacman.getRow());
							break;
						case 1:
							dirGhosts[i] = trickyGhostChoice(ghosts.get(i).col,ghosts.get(i).row,pacman.getCol(),pacman.getRow(),pacman.getDesiredDirection());
							break;
						case 2:
							dirGhosts[i] = randomGhostChoice(ghosts.get(i).col,ghosts.get(i).row);
							break;
						case 3:
							dirGhosts[i] = randomGhostChoice(ghosts.get(i).col,ghosts.get(i).row);
							break;
					}
					}						
				}
			/* simuler le déplacement des fantômes et évaluer le score */
				int v = MaxValue(game,score,depth+1,fw);
				return v;
			}
		}
		
	}
	
	/**
	 * Guessing the choice of the scared ghosts with the coordinate of the ghost
	 * @param x the x coordinate of the ghost
	 * @param y the y coordinate of the ghost
	 * @return the next direction of the ghost
	 */
	public int scaredGhostChoice(int x, int y) {
		int dx = 18-x;
		int dy = 11-y;
		
		if(dx>0 && dy>0) {	//pacman en bas à droite
			if(dx > dy) {
				return 0;
			}else {
				return 1;
			}
		}else if(dx<0 && dy>0) {	//pacman en bas à gauche
			if(Math.abs(dx)>dy) {
				return 2;
			}else {
				return 1;
			}
		}else if(dx<0 && dy<0) {	//pacman en haut à gauche
			if(dx>dy) {
				return 3;
			}else {
				return 2;
			}
		}else {		//dernier cas : haut à droite
			if(dx > Math.abs(dy)) {
				return 0;
			}else {
				return 3;
			}
		}
	}
	
	/**
	 * Guessing the choice of the random ghosts (orange and cyan) with the coordinate of the ghost
	 * @param x the x coordinate of the ghost
	 * @param y the y coordinate of the ghost
	 * @return the next direction of the ghost
	 */
	public int randomGhostChoice(int x, int y) {
		return (int) (Math.random()*3);
	}
	/**
	 * Guessing the choice of the red ghost with the coordinate of the ghost
	 * @param x the x coordinate of the ghost
	 * @param y the y coordinate of the ghost
	 * @param px the x coordinate of pacman
	 * @param py the y coordinate of pacman
	 * @return 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
	 */
	public int aggressiveGhostChoice(int x, int y, int px, int py) {
		int dx = px-x;
		int dy = py-y;
		if(dx==0) {
			if(dy<0) {
				return 3;
			}else {
				return 1;
			}
		}else if(dy==0) {
			if(dx<0) {
				return 2;
			}else {
				return 0;
			}
		}
		
		if(dx>0 && dy>0) {	//pacman en bas à droite
			if(dx > dy) {
				return 0;
			}else {
				return 1;
			}
		}else if(dx<0 && dy>0) {	//pacman en bas à gauche
			if(Math.abs(dx)>dy) {
				return 2;
			}else {
				return 1;
			}
		}else if(dx<0 && dy<0) {	//pacman en haut à gauche
			if(dx>dy) {
				return 3;
			}else {
				return 2;
			}
		}else {		//dernier cas : haut à droite
			if(dx > Math.abs(dy)) {
				return 0;
			}else {
				return 3;
			}
		}
	}
	/**
	 * Guessing the choice of the pink ghost with the coordinate of the ghost
	 * @param x the x coordinate of the ghost
	 * @param y the y coordinate of the ghost
	 * @param px the x coordinate of pacman
	 * @param py the y coordinate of pacman
	 * @param dir the direction of pacman
	 * @return 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
	 */
	public int trickyGhostChoice(int x, int y,int px, int py,int dir) {
		int dx = px-x;
		int dy = py-y;
		if(dir == 0 || dir == 2) {
			dx = (dir==0) ? (dx+2) : (dx-2);
		}else {
			dy = (dir==1) ? (dy+2) : (dy-2);
		}
		
		if(dx==0) {
			if(dy<0) {
				return 3;
			}else {
				return 1;
			}
		}else if(dy==0) {
			if(dx<0) {
				return 2;
			}else {
				return 0;
			}
		}
		
		if(dx>0 && dy>0) {	//pacman en bas à droite
			if(dx > dy) {
				return 0;
			}else {
				return 1;
			}
		}else if(dx<0 && dy>0) {	//pacman en bas à gauche
			if(Math.abs(dx)>dy) {
				return 2;
			}else {
				return 1;
			}
		}else if(dx<0 && dy<0) {	//pacman en haut à gauche
			if(dx>dy) {
				return 3;
			}else {
				return 2;
			}
		}else {		//dernier cas : haut à droite
			if(dx > Math.abs(dy)) {
				return 0;
			}else {
				return 3;
			}
		}
	}
    
}