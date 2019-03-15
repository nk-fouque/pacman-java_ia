package Elements.infra;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * BitmapFontRenderer class.
 * Contains the functions to draw textes like the crdits, title screen, "HI-SCORE" etc...
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 * @author nfouque
 */
public class BitmapFontRenderer {
    
    public BufferedImage bitmapFontImage;
    public BufferedImage[] letters;
    
    public int letterWidth;
    public int letterHeight;
    public int letterVerticalSpacing = 0;
    public int letterHorizontalSpacing = 0;

    public BitmapFontRenderer(String fontRes, int cols, int rows) {
        loadFont(fontRes, cols, rows);
    }
    
    public void drawText(Graphics2D g, String text, int x, int y) {
        if (letters == null) {
            return;
        }
        int px = 0;
        int py = 0;
        for (int i=0; i<text.length(); i++) {
            int c = text.charAt(i);
            if (c == (int) '\n') {
                py += letterHeight + letterVerticalSpacing;
                px = 0;
                continue;
            }
            else if (c == (int) '\r') {
                continue;
            }
            Image letter = letters[c];
            g.drawImage(letter, (int) (px + x), (int) (py + y + 1), null);
            px += letterWidth + letterHorizontalSpacing;
        }
    }

    private void loadFont(String filename, Integer cols, Integer rows) {
        try {
            bitmapFontImage = ImageIO.read(getClass().getResourceAsStream(filename));
            loadFont(bitmapFontImage, cols, rows);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void loadFont(BufferedImage image, Integer cols, Integer rows) {
        int lettersCount = cols * rows; 
        bitmapFontImage = image;
        letters = new BufferedImage[lettersCount];
        letterWidth = bitmapFontImage.getWidth() / cols;
        letterHeight = bitmapFontImage.getHeight() / rows;

        for (int y=0; y<rows; y++) {
            for (int x=0; x<cols; x++) {
                letters[y * cols + x] = new BufferedImage(letterWidth, letterHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D ig = (Graphics2D) letters[y * cols + x].getGraphics();
                ig.drawImage(bitmapFontImage, 0, 0, letterWidth, letterHeight
                        , x * letterWidth, y * letterHeight
                        , x * letterWidth + letterWidth, y * letterHeight + letterHeight, null);
            }
        }
    }
    
}
