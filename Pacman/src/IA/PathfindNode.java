package IA;

import java.util.Objects;

public class PathfindNode {
    public int bestScore;
    public int bestDirection;
    private GameState[] states;
    private int[] evaluateDirs;

    public PathfindNode(GameState state){
        bestDirection = 0;
        bestScore = 0;
        states=state.possibleFollowingStates();
        evaluateDirs = new int[4];
        for(int i = 0;i<4;i++){
            evaluateDirs[i]=0;
        }
    }

    public void leaf() {
        for(int i = 0;i<4;i++){
            if(!Objects.isNull(states[i])) {
                evaluateDirs[i] = states[i].newScore();
            }
            if (evaluateDirs[i]>evaluateDirs[bestDirection]){
                bestDirection = i;
                bestScore = evaluateDirs[i];
            }
        }
    }
}
