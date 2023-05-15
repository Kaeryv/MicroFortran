package quadrasoft.mufortran.display;

import javax.swing.*;
import java.awt.*;

public class SmoothLabel extends JLabel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SmoothLabel(String text) {
        super(text);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        super.paintComponent(g2d);
    }
}