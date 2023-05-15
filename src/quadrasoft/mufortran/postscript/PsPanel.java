package quadrasoft.mufortran.postscript;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PsPanel extends JScrollPane {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    DrawablePanel panel;

    public PsPanel(Image img) {
        panel = new DrawablePanel(toBufferedImage(img));
        panel.setSize(panel.getPreferredSize());
        this.setViewportView(panel);
        this.getVerticalScrollBar().setUnitIncrement(20);
    }

    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public JPanel getPSPane() {
        return panel;
    }

    public void updateImg(Image img) {
        panel.updateImage(img);
        panel.setSize(panel.getPreferredSize());
    }

    private class DrawablePanel extends JPanel {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private BufferedImage intersectionImage;

        public DrawablePanel(BufferedImage image) {
            intersectionImage = image;
        }

        @Override
        public Dimension getPreferredSize() {
            if (intersectionImage != null) {
                int width = intersectionImage.getWidth();
                int height = intersectionImage.getHeight();
                return new Dimension(width, height);
            }
            return super.getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (intersectionImage != null) {
                g.drawImage(intersectionImage, 0, 0, this);
            }
        }

        public void updateImage(Image img) {
            intersectionImage = toBufferedImage(img);
            this.setSize(panel.getPreferredSize());
            this.repaint();
        }
    }

}
