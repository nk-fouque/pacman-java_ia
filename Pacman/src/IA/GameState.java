package IA;

import Elements.*;
import Elements.actor.*;
import Elements.infra.*;

import java.util.ArrayList;

public class GameState {
    public PacmanGame game;
    public ArrayList<Ghost> ghosts;
    public ArrayList<Food> food;
    public ArrayList<PowerBall> powerBalls;

    public int distanceToNearestFood;
    public int getDistanceToNearestPowerBall;
    public int ditanceToNearestGhost;

    public GameState(PacmanGame game){
        this.game = game;
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
        }
    }

    /**
     *
     * @return the best possible GameState
     */
    public void evaluatePossibleGameStates(){
        ;
    }




}
