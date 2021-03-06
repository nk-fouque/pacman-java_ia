package main;

import Elements.PacmanGame;
import Elements.infra.Display;
import Elements.infra.Game;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * Main class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Main {

    public static boolean visibleGame;       //Used to switch between to versions of the game running
    public static final boolean verbose = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                visibleGame = true;       //TODO : set it to true to play a normal game, false will make the game play in background

                Game game = new PacmanGame(verbose,60);    //FIXME (Matthieu) : param fps is the "speed" of the game, 1<fps<1000, COOL = 60

                Display view = new Display(game, visibleGame);
                JFrame frame = new JFrame();
                frame.setTitle("Pacman");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(view);
                frame.pack();
                frame.setLocationRelativeTo(null);

               if(visibleGame)
                    frame.setVisible(true); //Makes the whole graphic interface visible, runs in the back
                view.requestFocus();
                view.start();
            }

        });
    }
    
}
