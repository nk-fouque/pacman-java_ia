package IA;

import main.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class PathfindTree {
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
    public int verboseLevel = 10;

    /**
     * The constructor and initialization of parameters
     * @param state the state from which we build the following states, not stored
     */

    public PathfindTree(GameState state){
        bestDirection = new ArrayList<>();
        bestScore = -1;
        states=state.possibleFollowingStates();
        scoreByDir = new int[4];
        for(int i = 0;i<4;i++){
            scoreByDir[i]=-1;
        }
    }

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

    /**
     * This is a heuristic
     * @param direction the direction we consider
     */
    protected void consider(int direction){
        if (scoreByDir[direction]> bestScore){                      // if we find a better direction to gp
            bestDirection.clear();                                  // we forget the previous best directions
            bestDirection.add(direction);                           // and we add this one
            bestScore = scoreByDir[direction];                      // and we update the best possible score
        } else if (scoreByDir[direction] == bestScore){             // If we find a direction that's as good as the current one
            bestDirection.add(direction);                           // We add it to the best directions
        }
    }

    /**
     * Chooses a direction, after they've been evaluated
     * @return the direction chosen (as Integer)
     */
    public int choose(){
        if(bestDirection.isEmpty()){                                            //If we have no valid direction FIXME (which should not happen)
            int res = ThreadLocalRandom.current().nextInt(0, 4);   //We choose a random one
            if(verbose) System.out.println("No good direction, chose"+res);
            return res;
        } else {                                                                //else
            int index = ThreadLocalRandom.current().nextInt(0, bestDirection.size());
            int res=bestDirection.get(index);                                   //We choose a random direction among the ones leading to the best score
            if(verbose) System.out.println("Chose "+res);
            return res;
        }
    }
}
