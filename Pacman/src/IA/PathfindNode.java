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
        System.out.println("Leaf : "+bestDirection+" Score : "+bestScore);
    }

    public void node(int depth){
        if(depth == 0){
            leaf();
        } else {
            PathfindNode[] sons = new PathfindNode[4];
            for(int i = 0;i<4;i++){
                if(!Objects.isNull(states[i])) {
                    sons[i] = new PathfindNode(states[i]);
                    sons[i].node(depth -1);
                    evaluateDirs[i]=sons[i].bestScore;
                }
                if (evaluateDirs[i]>evaluateDirs[bestDirection]){
                    bestDirection = i;
                    bestScore = evaluateDirs[i];
                }
            }
            System.out.println("Node : "+bestDirection+" Score : "+bestScore);
        }
    }
}
