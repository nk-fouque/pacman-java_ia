package IA;

import Elements.*;
import Elements.actor.*;
import Elements.infra.*;

import java.util.ArrayList;

public class GameState {
    public PacmanGame game;
    public ArrayList<Ghost> ghosts = new ArrayList<>();
    public ArrayList<Food> food= new ArrayList<>();
    public ArrayList<PowerBall> powerBalls= new ArrayList<>();
    public int pacmanRow;
    public int pacmanCol;

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
            if(a instanceof Pacman ){
                this.pacmanCol = ((Pacman) a).getCol();
                this.pacmanRow = ((Pacman) a).getRow();
            }
        }
    }

    public int evaluateState(){
        int score = game.getScoreInt();
        return score;
    }

    public int pacmanEat(){
        int res = 0;
        if (pacmanOnFood() && !pacmanOnWall()){
            res += 10;
        }
        return res;
    }

    public int newScore(){
        int res = game.getScoreInt();
        res+=pacmanEat();
        return res;
    }

    public boolean pacmanOnFood(){
        boolean res = false;
        for(Food f : food){
            if (f.getCol()==pacmanCol && f.getRow() == pacmanRow){
                res= true;
                break;
            }
        }
        return res;
    }

    public boolean pacmanOnWall(){
        if(game.maze[pacmanRow][pacmanCol]==(-1)){
            return true;
        } else { return false ;}
    }

    public void move(int dir){
        switch (dir){
            case 0 : pacmanCol++;
            case 1 : pacmanRow--;
            case 2 : pacmanCol--;
            case 3 : pacmanRow++;
        }
    }

    /**
     * Tries every different positions
     * @return all possible GameStates
     */
    public GameState[] possibleGameStates(){
       GameState[] res = new GameState[4];
        for (int i = 0;i<4;i++) {
            res[i] = new GameState(game);
            res[i].move(i);
            if (res[i].pacmanOnWall()){
                System.out.println("Wall on "+i);
                res[i]=null;
            }
        }
       return res;
    }







}
