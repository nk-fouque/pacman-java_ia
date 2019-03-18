package IA;

import java.util.Objects;

public class PathfindNode {
    public int bestScore;
    public int bestDirection;
    private GameState[] states;
    private int[] scoreByDir;

    public PathfindNode(GameState state){
        bestDirection = 0;
        bestScore = 0;
        states=state.possibleFollowingStates();
        scoreByDir = new int[4];
        for(int i = 0;i<4;i++){
            scoreByDir[i]=0;
        }
    }

    public void leaf() {
        for(int i = 0;i<4;i++){
            if(!Objects.isNull(states[i])) {
                scoreByDir[i] = states[i].newScore();
                System.out.println("Dir : "+i+" Score : "+ scoreByDir[i]);
                if (scoreByDir[i]> scoreByDir[bestDirection]){
                    bestDirection = i;
                    bestScore = scoreByDir[i];
                }
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
                    scoreByDir[i]=sons[i].bestScore;
                }
                if (scoreByDir[i]> scoreByDir[bestDirection]){
                    bestDirection = i;
                    bestScore = scoreByDir[i];
                }
            }
            System.out.println("Node : "+bestDirection+" Score : "+bestScore);
        }
    }
}
