package main;

import br.ol.pacman.PacmanGame;
import br.ol.pacman.infra.Display;
import br.ol.pacman.infra.Game;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * Main class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                Game game = new PacmanGame(1000);    //FIXME (Matthieu) : param fps is the "speed" of the game, max = 1000
                Display view = new Display(game);
                JFrame frame = new JFrame();
                frame.setTitle("Pacman");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(view);
                frame.pack();
                frame.setLocationRelativeTo(null);
                //frame.setVisible(true); //FIXME (Matt) : Makes the whole graphic interface visible, runs in the back
                view.requestFocus();
                view.start();
            }

        });
    }
    
}
