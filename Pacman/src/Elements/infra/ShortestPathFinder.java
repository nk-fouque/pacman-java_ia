package Elements.infra;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * ShortestPathFinder class.
 * Helps the Ghosts returning home
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class ShortestPathFinder {
    
    // map[row][col]
    // -1 = wall
    public int[][] map;
    public List<Integer> path = new ArrayList<Integer>();
    private int pathIndex;
    private Point pathPosition = new Point();

    public ShortestPathFinder(int[][] originalMap) {
        map = new int[originalMap.length][originalMap[0].length];
        for (int y = 0; y < map.length; y++) {
            System.arraycopy(originalMap[y], 0, map[y], 0, map[0].length);
        }
    }
    
    private void clearMap() {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] > 0) {
                    map[y][x] = 0;
                }
            }
        }
    }
    
    private int getMapScore(int x, int y) {
        if (x < 0 || x > map[0].length - 1 || y < 0 || y > map.length - 1) {
            return -1;
        }
        return map[y][x];
    }
    
    private int[] neighbors = { 1, 0, -1, 0, 0, 1, 0, -1 };
    
    public void find(int srcX, int srcY, int destX, int destY) {
        path.clear();
        clearMap();
        int score = 1;
        map[destY][destX] = score;
        found:
        while (true) {
            boolean foundAtLeastOne = false;
            for (int y=0; y<map.length; y++) {
                for (int x=0; x<map[0].length; x++) {
                    if (getMapScore(x, y) == score) {
                        foundAtLeastOne = true;
                        for (int n=0; n<neighbors.length; n+=2) {
                            int dx = x + neighbors[n];
                            int dy = y + neighbors[n + 1];
                            if (getMapScore(dx, dy) == 0) {
                                map[dy][dx] = score + 1;
                                if (dx == srcX && dy == srcY) {
                                    fillPath(path, score + 1, dx, dy);
                                    pathIndex = 0;
                                    break found;
                                }
                            }
                        }
                    }
                }
            }
            if (!foundAtLeastOne) {
                break;
            }
            score++;
        }
    }

    // just for debugging purposes
    public void print() {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                String n = "000" + getMapScore(x, y);
                n = n.substring(n.length() - 3, n.length());
                System.out.print(n + " ");
            }
            System.out.println();
        }
    }    
    
    private void fillPath(List<Integer> path, int score, int dx, int dy) {
        int direction = 10;
        while (score > 0) {
            int ax = (direction & 3) - 2;
            int ay = ((direction >> 2) & 3) - 2;
            direction >>= 4;
            if (getMapScore(dx + ax, dy + ay) == score) {
                path.add(dx += ax);
                path.add(dy += ay);
                int k = 4 * (int) (32 * Math.random());
                direction = (28315 >> k) | (28315 << (32 - k)); 
                score--;
            }
        }
    }
    
    public boolean hasNext() {
        return pathIndex < path.size() - 1;
    }
    
    public Point getNext() {
        if (!hasNext()) {
            return null;
        }
        pathPosition.setLocation(path.get(pathIndex), path.get(pathIndex+1));
        pathIndex += 2;
        return pathPosition;
    }
    
}
