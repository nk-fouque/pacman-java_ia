package br.ol.pacman.infra;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Game class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Game {
    
    public Dimension screenSize;
    public Point2D screenScale;
    
    public List<Actor> actors = new ArrayList<Actor>();
    public BitmapFontRenderer bitmapFontRenderer = new BitmapFontRenderer("/res/font8x8.png", 16, 16);

    /**
     * Will be useful, set by default to 60
     */
    public long FPS;

    public void init() {
    }
    
    public void update() {
        for (Actor actor : actors) {
            actor.update();
        }
    }
    
    public void draw(Graphics2D g) {
        for (Actor actor : actors) {
            actor.draw(g);
        }
    }

    public <T> T checkCollision(Actor a1, Class<T> type) {
        a1.updateCollider();
        for(Actor a2 : actors) {
            a2.updateCollider();
            if (a1 != a2 
                && type.isInstance(a2)
                && a1.collider != null && a2.collider != null
                && a1.visible && a2.visible
                && a2.collider.intersects(a1.collider)) {
                    return type.cast(a2);
            }
        }
        return null;
    }

    public void broadcastMessage(String message) {
        for (Actor obj : actors) {
            try {
                Method method = obj.getClass().getMethod(message);
                if (method != null) {
                    method.invoke(obj);
                }
            } catch (Exception ex) {
            }
        }
    }

    public void drawText(Graphics2D g, String text, int x, int y) {
        bitmapFontRenderer.drawText(g, text, x, y);
    }
    
}
