package br.ol.pacman.infra;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Actor class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Actor<T extends Game> {
    
    public static final boolean DRAW_COLLIDER = false;
    
    public T game;
    public double x, y;
    public boolean visible;
    public BufferedImage frame;
    public BufferedImage[] frames;
    public Rectangle collider; 
    
    protected int instructionPointer;
    protected long waitTime;
    
    
    public Actor(T game) {
        this.game = game;
    }
    
    public void init() {
    }
    
    public void update() {
    }
    
    public void draw(Graphics2D g) {
        if (!visible) {
            return;
        }
        if (frame != null) {
            g.drawImage(frame, (int) x, (int) y, frame.getWidth(), frame.getHeight(), null);
        }
        if (DRAW_COLLIDER && collider != null) {
            updateCollider();
            g.setColor(Color.RED);
            g.draw(collider);
        }
    }
    
    protected void loadFrames(String ... framesRes) {
        try {
            frames = new BufferedImage[framesRes.length];
            for (int i = 0; i < framesRes.length; i++) {
                String frameRes = framesRes[i];
                frames[i] = ImageIO.read(getClass().getResourceAsStream(frameRes));
            }
            frame = frames[0];
        } catch (IOException ex) {
            Logger.getLogger(Actor.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    } 

    public void updateCollider() {
        if (collider != null) {
            collider.setLocation((int) x, (int) y);
        }
    }
    
}
