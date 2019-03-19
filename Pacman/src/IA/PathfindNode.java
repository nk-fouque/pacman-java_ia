package IA;

import main.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class PathfindNode {
    public int bestScore;
    public List<Integer> bestDirection;
    private GameState[] states;
    private int[] scoreByDir;
    public boolean verbose = Main.verbose;

    public PathfindNode(GameState state){
        bestDirection = new ArrayList<>();
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
                if (verbose ) System.out.println("Dir : "+i+" Score : "+ scoreByDir[i]);
                if (scoreByDir[i]> bestScore){
                    bestDirection.clear();
                    bestDirection.add(i);
                    bestScore = scoreByDir[i];
                } else if (scoreByDir[i] == bestScore){
                    bestDirection.add(i);
                }
            }
        }
        if(verbose) System.out.println("Leaf : "+bestDirection.toString()+" Score : "+bestScore);
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
                if (scoreByDir[i]> bestScore){
                    bestDirection.clear();
                    bestDirection.add(i);
                    bestScore = scoreByDir[i];
                } else if (scoreByDir[i] == bestScore){
                    bestDirection.add(i);
                }
            }
            if(verbose) System.out.println("Node : "+bestDirection+" Score : "+bestScore);
        }
    }

    public int choose(){
        if(bestDirection.isEmpty()){
            int res = ThreadLocalRandom.current().nextInt(0, 4);
            if(verbose) System.out.println("No good direction, chose"+res);
            return res;
        } else {
            int index = ThreadLocalRandom.current().nextInt(0, bestDirection.size());
            int res=bestDirection.get(index);
            if(verbose) System.out.println("Chose "+res);
            return res;
        }
    }
}
