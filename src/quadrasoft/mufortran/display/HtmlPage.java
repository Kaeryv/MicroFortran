package quadrasoft.mufortran.gui;

import javax.swing.*;
import java.awt.*;

public class HtmlPage extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public HtmlPage() {
        setBackground(Color.WHITE);
        JLabel lbl = new JLabel(
                "<html>\r\n<body>\r\n<br><br><br><h1><font size=\"32\", color=\"orange\">\u00B5</font><font size=\"32\">Fortran</font></h1>\r\n<p><font color=\"blue\">Version 0.98a</font></p><br><br><br><br><br>\r\n</body>\r\n</html>");
        // this.setBackground(Color.WHITE);
        this.add(lbl);
        this.setBorder(BorderFactory.createEtchedBorder());
    }

}
