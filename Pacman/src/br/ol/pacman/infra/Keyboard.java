package br.ol.pacman.infra;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Keyboard class.
 * Makes it easier to read calls to keyboard listeners, will probably be replaced by our AI listeners
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Keyboard extends KeyAdapter {
    
    public static boolean[] keyPressed = new boolean[256];

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed[e.getKeyCode()] = false;
    }
    
}
