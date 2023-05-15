package quadrasoft.mufortran.app.forms;

import quadrasoft.mufortran.general.Project;
import quadrasoft.mufortran.general.Session;
import quadrasoft.mufortran.display.DisplayImage;
import quadrasoft.mufortran.display.SmoothLabel;
import quadrasoft.mufortran.resources.Resources;
import quadrasoft.mufortran.resources.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WelcomePanel extends JFrame implements MouseListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    SmoothLabel lblLink1;
    SmoothLabel lblLink2;
    SmoothLabel lblLink3;
    List<ProjectButtonSmoothLabel> btnsRecentProjects = new ArrayList<ProjectButtonSmoothLabel>();

    public WelcomePanel() {
        this.setVisible(false);
        setResizable(false);
        getContentPane().setBackground(new Color(51, 51, 51));
        getContentPane().setLayout(null);

        // µFortran title
        SmoothLabel lblNewLabel = new SmoothLabel(Strings.s("application.name"));
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setBounds(69, 11, 106, 43);
        getContentPane().add(lblNewLabel);

        try {
            // Logo at the top left corner.
            DisplayImage logoPanel = new DisplayImage("icon.icon2");
            logoPanel.setLocation(0, 0);
            logoPanel.setSize(64, 64);
            getContentPane().add(logoPanel);

            // Splash image.
            DisplayImage imagePanel = new DisplayImage("img.splash");
            imagePanel.setSize(600, 176);
            imagePanel.setLocation(0, 65);
            getContentPane().add(imagePanel);

            // Version label
            SmoothLabel smoothLabel = new SmoothLabel(" ");
            smoothLabel.setText("<html><b>" + Session.getVersion() + "</b>");
            smoothLabel.setForeground(Color.WHITE);
            smoothLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
            smoothLabel.setBounds(185, 11, 226, 43);
            getContentPane().add(smoothLabel);

            // Date label
            SmoothLabel smthlblDate = new SmoothLabel("\u00B5Fortran");
            smthlblDate.setVerticalAlignment(SwingConstants.TOP);
            smthlblDate.setText("" + new java.util.Date());
            smthlblDate.setForeground(Color.LIGHT_GRAY);
            smthlblDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
            smthlblDate.setBounds(397, 11, 193, 43);
            getContentPane().add(smthlblDate);

            // Label links
            SmoothLabel smthlblLinks = new SmoothLabel("\u00B5Fortran");
            smthlblLinks.setVerticalAlignment(SwingConstants.TOP);
            smthlblLinks.setText("Links:");
            smthlblLinks.setForeground(Color.LIGHT_GRAY);
            smthlblLinks.setFont(new Font("Tahoma", Font.PLAIN, 12));
            smthlblLinks.setBounds(23, 262, 99, 15);
            getContentPane().add(smthlblLinks);

            // Label recents
            SmoothLabel smthlblRecent = new SmoothLabel("\u00B5Fortran");
            smthlblRecent.setVerticalAlignment(SwingConstants.TOP);
            smthlblRecent.setText("Recent projects :");
            smthlblRecent.setForeground(Color.LIGHT_GRAY);
            smthlblRecent.setFont(new Font("Tahoma", Font.PLAIN, 12));
            smthlblRecent.setBounds(300, 262, 100, 15);
            getContentPane().add(smthlblRecent);

            // The three links in welcome screen :
            lblLink1 = new SmoothLabel("Fortran doc");
            lblLink1.setBackground(new Color(51, 51, 51));
            lblLink1.setForeground(UIManager.getColor("Button.highlight"));
            lblLink1.setBounds(23, 282, 235, 23);
            lblLink1.addMouseListener(this);
            lblLink1.setIcon(Resources.getImageResource("icon.browser"));
            lblLink1.setCursor(new Cursor(Cursor.HAND_CURSOR));
            getContentPane().add(lblLink1);

            lblLink2 = new SmoothLabel("Wiki page");
            lblLink2.setBackground(new Color(51, 51, 51));
            lblLink2.setForeground(UIManager.getColor("Button.highlight"));
            lblLink2.setBounds(23, 316, 235, 23);
            lblLink2.addMouseListener(this);
            lblLink2.setIcon(Resources.getImageResource("icon.browser"));
            lblLink2.setCursor(new Cursor(Cursor.HAND_CURSOR));
            getContentPane().add(lblLink2);

            lblLink3 = new SmoothLabel("GitHub");
            lblLink3.setBackground(new Color(51, 51, 51));
            lblLink3.setForeground(UIManager.getColor("Button.highlight"));
            lblLink3.setBounds(23, 350, 235, 23);
            lblLink3.setIcon(Resources.getImageResource("icon.browser"));
            lblLink3.addMouseListener(this);
            lblLink3.setCursor(new Cursor(Cursor.HAND_CURSOR));
            getContentPane().add(lblLink3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setForeground(Color.DARK_GRAY);
        this.setUndecorated(true);
        setBackground(new Color(0f, 0f, 0f));
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        setAlwaysOnTop(true);

        this.setPreferredSize(new Dimension(600, 400));
        this.setSize(this.getPreferredSize());
        this.getContentPane().addMouseListener(this);
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }

    public void loadRecents() {
        int i = 0;
        Iterator<String> projetcs_iterator = Session.getRecentProjects().iterator();
        while (projetcs_iterator.hasNext()) {
            String str = projetcs_iterator.next();
            if (new File(str).exists()) {
                ProjectButtonSmoothLabel temp = new ProjectButtonSmoothLabel(str);
                temp.setBackground(new Color(51, 51, 51));
                temp.setForeground(UIManager.getColor("Button.highlight"));
                temp.setBounds(300, 282 + i * 25, 289, 23);
                temp.setIcon(new ImageIcon(this.getClass().getResource("icons/prj.png")));
                temp.addMouseListener(this);
                temp.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnsRecentProjects.add(temp);
                this.getContentPane().add(temp);
                i++;
            } else {
                projetcs_iterator.remove();
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent arg0) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        for (ProjectButtonSmoothLabel temp : btnsRecentProjects) {
            if (e.getSource().equals(temp)) {
                temp.setBorder(BorderFactory.createEtchedBorder());
                temp.setForeground(Color.ORANGE);
                temp.setText(temp.getPath());
            }
        }
        if (e.getSource().equals(lblLink1)) {
            lblLink1.setBorder(BorderFactory.createEtchedBorder());
            lblLink1.setForeground(Color.ORANGE);
        }
        if (e.getSource().equals(lblLink3)) {
            lblLink3.setBorder(BorderFactory.createEtchedBorder());
            lblLink3.setForeground(Color.ORANGE);
        }
        if (e.getSource().equals(lblLink2)) {
            lblLink2.setBorder(BorderFactory.createEtchedBorder());
            lblLink2.setForeground(Color.ORANGE);
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {
        for (ProjectButtonSmoothLabel temp : btnsRecentProjects) {
            if (e.getSource().equals(temp)) {
                temp.setBorder(BorderFactory.createEmptyBorder());
                temp.setForeground(UIManager.getColor("Button.highlight"));
                temp.setText(temp.getPath().substring(temp.getPath().lastIndexOf("/") + 1));
            }
        }
        if (e.getSource().equals(lblLink1)) {
            lblLink1.setBorder(BorderFactory.createEmptyBorder());
            lblLink1.setForeground(UIManager.getColor("Button.highlight"));
        }
        if (e.getSource().equals(lblLink2)) {
            lblLink2.setBorder(BorderFactory.createEmptyBorder());
            lblLink2.setForeground(UIManager.getColor("Button.highlight"));
        }
        if (e.getSource().equals(lblLink3)) {
            lblLink3.setBorder(BorderFactory.createEmptyBorder());
            lblLink3.setForeground(UIManager.getColor("Button.highlight"));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (ProjectButtonSmoothLabel temp : btnsRecentProjects) {
            if (e.getSource().equals(temp)) {
                Session.addProject(new Project(temp.getPath()));
            }
        }

        if (e.getSource().equals(lblLink1)) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("http://www.fortran90.org/"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (e.getSource().equals(lblLink2)) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("http://wikipedia.com/"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (e.getSource().equals(lblLink3)) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("http://github.com/Kaeryv"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        }

        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        this.setVisible(false);

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    private class ProjectButtonSmoothLabel extends SmoothLabel {
        private static final long serialVersionUID = 1L;

        private String path;

        public ProjectButtonSmoothLabel(String p) {
            super(p.substring(p.lastIndexOf("/") + 1));
            path = p;
            this.setText(p.substring(p.lastIndexOf("/") + 1));
        }

        public String getPath() {
            return path;
        }
    }

}
