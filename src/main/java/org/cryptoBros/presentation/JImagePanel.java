package org.cryptoBros.presentation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A JPanel that renders a scaled image as its background.
 */
public class JImagePanel extends JPanel {

    /** The image to render. */
    private BufferedImage image;

    /**
     * Creates a new JImagePanel from the given image path.
     *
     * @param path the file path of the image to display
     */
    public JImagePanel(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            // Not properly managed, sorry!
            e.printStackTrace();
        }
    }

    /**
     * Returns the preferred size by scaling the image height to maintain the aspect ratio for the current panel width.
     *
     * @return the preferred dimension with the aspect-ratio-adjusted height
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension preferred = super.getPreferredSize();

        float width = image.getWidth();
        float height = image.getHeight();

        // Calculate the height needed to mantain aspect ratio
        preferred.height = Math.round(getWidth()*height/width);

        return preferred;
    }

    /**
     * Paints the background image scaled to fill the entire panel area.
     *
     * @param g the graphics context used for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		// test comment
    }
}
