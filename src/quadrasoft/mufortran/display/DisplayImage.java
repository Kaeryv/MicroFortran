package quadrasoft.mufortran.display;

import quadrasoft.mufortran.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DisplayImage extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DisplayImage(String filename) throws IOException {
        ImageIcon icon = Resources.getImageResource(filename);
        this.setLayout(new BorderLayout());
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        this.add(lbl);
        this.setVisible(true);
    }
}
