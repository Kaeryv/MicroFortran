package quadrasoft.mufortran.general;

import quadrasoft.mufortran.display.SmoothLabel;
import quadrasoft.mufortran.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class FirstLaunchManager extends JFrame implements MouseListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Runnable gfortranDownloader;
    SmoothLabel lblLink1;
    SmoothLabel lblLink2;
    SmoothLabel lblLink3;
    List<ProjectButtonSmoothLabel> btnsRecentProjects = new ArrayList<ProjectButtonSmoothLabel>();

    public FirstLaunchManager() {
        this.setVisible(false);
        setResizable(false);
        getContentPane().setBackground(new Color(51, 51, 51));
        getContentPane().setLayout(null);

        SmoothLabel lblNewLabel = new SmoothLabel("\u00B5Fortran");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setBounds(24, 11, 106, 43);
        getContentPane().add(lblNewLabel);

        SmoothLabel smoothLabel = new SmoothLabel("\u00B5Fortran");
        smoothLabel.setText("<html><b>" + Session.getVersion() + "</b>");
        smoothLabel.setForeground(Color.WHITE);
        smoothLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
        smoothLabel.setBounds(130, 11, 226, 43);
        getContentPane().add(smoothLabel);

        SmoothLabel smthlblDate = new SmoothLabel("\u00B5Fortran");
        smthlblDate.setVerticalAlignment(SwingConstants.TOP);
        smthlblDate.setText("" + new java.util.Date());
        smthlblDate.setForeground(Color.LIGHT_GRAY);
        smthlblDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
        smthlblDate.setBounds(426, 11, 164, 43);
        getContentPane().add(smthlblDate);

        SmoothLabel smthlblLinks = new SmoothLabel("\u00B5Fortran");
        smthlblLinks.setVerticalAlignment(SwingConstants.TOP);
        smthlblLinks.setText("First Start");
        smthlblLinks.setForeground(Color.LIGHT_GRAY);
        smthlblLinks.setFont(new Font("Trebuchet MS", Font.PLAIN, 19));
        smthlblLinks.setBounds(24, 55, 175, 33);
        getContentPane().add(smthlblLinks);

        lblLink1 = new SmoothLabel("Fortran doc");
        lblLink1.setBackground(new Color(51, 51, 51));
        lblLink1.setForeground(UIManager.getColor("Button.highlight"));
        lblLink1.setBounds(215, 350, 235, 23);
        lblLink1.addMouseListener(this);
        lblLink1.setIcon(Resources.getImageResource("icon.browser"));
        getContentPane().add(lblLink1);

        lblLink2 = new SmoothLabel("Wiki page");
        lblLink2.setBackground(new Color(51, 51, 51));
        lblLink2.setForeground(UIManager.getColor("Button.highlight"));
        lblLink2.setBounds(417, 350, 235, 23);
        lblLink2.addMouseListener(this);
        lblLink2.setIcon(Resources.getImageResource("icon.browser"));
        getContentPane().add(lblLink2);

        lblLink3 = new SmoothLabel("GitHub");
        lblLink3.setBackground(new Color(51, 51, 51));
        lblLink3.setForeground(UIManager.getColor("Button.highlight"));
        lblLink3.setBounds(23, 350, 235, 23);
        lblLink3.setIcon(Resources.getImageResource("icon.browser"));
        lblLink3.addMouseListener(this);

        getContentPane().add(lblLink3);

        JPanel panel = new JPanel();
        panel.setBounds(24, 49, 187, 5);
        getContentPane().add(panel);

        JButton btnNewButton = new JButton("Configure gfortran");
        btnNewButton.setBounds(42, 119, 137, 25);
        getContentPane().add(btnNewButton);

        JButton btnLocate = new JButton("Locate");
        btnLocate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        btnLocate.setBounds(191, 119, 137, 25);
        getContentPane().add(btnLocate);

        JButton btnDownlad = new JButton("Download");
        btnDownlad.setBounds(191, 145, 137, 25);
        getContentPane().add(btnDownlad);
        System.out.println(Session.getRecentProjects());

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

    public void loadRecents() {
        int i = 0;
        if (!Session.getRecentProjects().isEmpty()) {
            for (String str : Session.getRecentProjects()) {
                if (new File(str).exists()) {
                    ProjectButtonSmoothLabel temp = new ProjectButtonSmoothLabel(str);
                    temp.setBackground(new Color(51, 51, 51));
                    temp.setForeground(UIManager.getColor("Button.highlight"));
                    temp.setBounds(300, 282 + i * 25, 289, 23);
                    temp.setIcon(new ImageIcon(this.getClass().getResource("icons/prj.png")));
                    temp.addMouseListener(this);
                    btnsRecentProjects.add(temp);
                    this.getContentPane().add(temp);
                    i++;
                } else {
                    Session.getRecentProjects().remove(str);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        for (SmoothLabel temp : btnsRecentProjects) {
            if (e.getSource().equals(temp)) {
                temp.setBorder(BorderFactory.createEtchedBorder());
                temp.setForeground(Color.ORANGE);
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
        for (SmoothLabel temp : btnsRecentProjects) {
            if (e.getSource().equals(temp)) {
                temp.setBorder(BorderFactory.createEmptyBorder());
                temp.setForeground(UIManager.getColor("Button.highlight"));
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

        // this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        // this.setVisible(false);

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    private class ProjectButtonSmoothLabel extends SmoothLabel {
        private static final long serialVersionUID = 1L;

        private String path;

        public ProjectButtonSmoothLabel(String path) {
            super(path.substring(path.lastIndexOf("/") + 1));
            this.setPath(path);
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}