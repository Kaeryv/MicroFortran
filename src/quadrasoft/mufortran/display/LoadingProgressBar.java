package quadrasoft.mufortran.display;

import javax.swing.*;
import java.awt.*;

public class LoadingProgressBar extends JProgressBar {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public LoadingProgressBar() {
        super();
        this.setForeground(Color.ORANGE);
        this.setStringPainted(true);
        this.setMaximum(100);
        this.setMinimum(0);
        this.setValue(5);
    }

}
