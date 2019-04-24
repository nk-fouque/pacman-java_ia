package IA;
import Elements.PacmanGame;
import Elements.actor.Pacman;
import Elements.infra.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import main.Main;

public class ExpectiMax {

	public ExpectiMax(GameState state) {
		 bestDirection = new ArrayList<>();
	        bestScore = -1;
	        states=state.possibleFollowingStates();
	        scoreByDir = new int[4];
	        for(int i = 0;i<4;i++){
	            scoreByDir[i]=-1;
	        }
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
		} else {								// 10pts par boule, 200 puis 400 puis 800 puis 1600 par fantôme
			
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
	            ExpectiMax[] sons = new ExpectiMax[4];              // we build the sons an array to store the sons
	            for(int i = 0;i<4;i++){                                 // TODO This for block is where we will put the multi thread if we put one
	                if(!Objects.isNull(states[i])) {                    // If the direction is valid (i.e. no walls)
	                    ((GameStatePlus)states[i]).moveGhosts(fw);      // FIXME decides if we consider the ghosts' moves or not
	                    states[i].newScore();                           // we calculate the "score if we get there"
	                    sons[i] = new ExpectiMax(states[i]);          // we build a son from it
	                    sons[i].MinValue(game, states[i].newScore ,depth -1,fw);     // and call the node function recursively FIXME c'est pas score
	                    scoreByDir[i]=sons[i].bestScore;                // then we get the best score the son is capable of
	                    consider(i);                                    // and decide if we want to follow this son
	                }
	            }
	            if(verbose||depth>=verboseLevel) System.out.println("Node Level "+depth+" : "+bestDirection+" Score : "+bestScore);
	        }
			
			
			
			v = MinValue(game, score + 10);	//cas où pacman va vers une bouboule
			
			v = MinValue(game, score + 50);	// cas où pacman va vers une powerball
			
			
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (ttt.isEmpty(i, j)) {
						ttt.play(i, j);
						int v = MinValue(game, alpha, beta);
						ttt.undo(i, j);
						if (v >= beta) {
							
							return v;
						}
						if (res < v) {
							alpha = v;
							res = v;
							c.setX(i);
							c.setY(j);
							
							System.out.println(c);
						}
						//ttt.undo(i, j);
					}

				}
			}
		}

		return res;

	}

	public int MinValue(Game game, int score, int depth, FloydWarshall fw) {
		int res = 1;
		//int opti = 1;
		if (ttt.isFinished()) {
			if (ttt.draw()) {
				return 0;
			} else if ((ttt.circleWon() && !isCross) || (ttt.crossWon() && isCross)) {
				return 1; // L'IA gagne
			} else if ((ttt.crossWon() && !isCross) || (ttt.circleWon() && isCross)) {
				return -1; // L'IA perd
			}
		} else {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (ttt.isEmpty(i, j)) {
						ttt.play(i, j);
						
						int v = MaxValue(ttt, new Coordinate(0, 0), alpha, beta);
						ttt.undo(i, j);
						blbl++;
						if (v <= alpha) {
							
							return v;

						}
						if(res > v) {
							beta =v;
							res = v;
						}
					}

				}
			}
		}
		return res;
	}
    
    
//	public int value(int s) {
//		return 0;
//	}
//	
//	public int maxValue(int s) {
//		return 0;
//	}
//	
//	public int expValue(int s) {
//		return 0;
//	}
	
}
