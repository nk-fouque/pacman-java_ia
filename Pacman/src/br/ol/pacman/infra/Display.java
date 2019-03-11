package br.ol.pacman.infra;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;


/**
 * Display class.
 *
 * @author Leonardo Ono (ono.leo@gmail.com)
 * @author nfouque
 */
public class Display extends Canvas {

    private Game game;
    private boolean running;
    private BufferStrategy bs;

    public Display(Game game) {
        this.game = game;
        int sx = (int) (game.screenSize.width * game.screenScale.getX());
        int sy = (int) (game.screenSize.height * game.screenScale.getY());
        setPreferredSize(new Dimension(sx, sy));
        addKeyListener(new Keyboard());
    }

    public void start() {
        if (running) {
            return;
        }
        createBufferStrategy(3);
        bs = getBufferStrategy();
        game.init();
        running = true;
        Thread thread = new Thread(new MainLoop(game));
        thread.start();
    }

    private class MainLoop implements Runnable {
        private Game game;

        public MainLoop(Game game){
            super();
            this.game = game;
        }



        @Override
        public void run() {
            long desiredFrameRateTime = 1000 / game.FPS;
            long currentTime = System.currentTimeMillis();
            long lastTime = currentTime - desiredFrameRateTime;
            long unprocessedTime = 0;
            boolean needsRender = false;
            while (running) {
                currentTime = System.currentTimeMillis();
                unprocessedTime += currentTime - lastTime;
                lastTime = currentTime;

                /**
                 * Constantly checks if it is time to render (based on the number of FPS specified
                 */
                while (unprocessedTime >= desiredFrameRateTime) {
                    unprocessedTime -= desiredFrameRateTime;
                    update(); //<-- This is what actually runs the game
                    needsRender = true;
                }

                /** If it's time to render, draws everything */
                if (needsRender) {
                    Graphics2D g = (Graphics2D) bs.getDrawGraphics();
                    g.setBackground(Color.BLACK);
                    g.clearRect(0, 0, getWidth(), getHeight());
                    g.scale(game.screenScale.getX(), game.screenScale.getY());
                    draw(g);
                    g.dispose();
                    bs.show();
                    needsRender = false;
                } else {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }

    }


    public void update() {
        game.update();
    }

    public void draw(Graphics2D g) {
        game.draw(g);
    }

}
