package quadrasoft.mufortran.app.forms;

import quadrasoft.mufortran.general.Project;
import quadrasoft.mufortran.general.Session;
import quadrasoft.mufortran.display.DisplayImage;
import quadrasoft.mufortran.resources.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ProjectCreatorFrame extends JFrame implements ActionListener, KeyListener {

    /**
     *
     */

    private static final long serialVersionUID = 1L;

    private boolean concluded;

    private JPanel formsPane = new JPanel();

    private DisplayImage imagePanel;

    private JTextField tfProjName;

    private JTextField tfProjPath;

    private JTextField tfCompPath;

    private JTextField tfFinalFilename;

    private JLabel lblWelcome;

    private JLabel lblCompilerPath;

    private JLabel lblResFilename;

    private JLabel lblProjectName;
    private JLabel lblFolder;
    private JButton btnParcourir;
    private JButton btnParcourir2;

    private JButton btnEnd;
    private JButton btnNextBtn;

    public ProjectCreatorFrame() {
        super("Project wizard");
        this.setVisible(false);

        formsPane.setLayout(null);

        tfProjName = new JTextField();
        tfProjName.setBackground(Color.WHITE);
        tfProjName.setBounds(150, 36, 235, 20);
        tfProjName.setColumns(10);
        tfProjName.addKeyListener(this);
        tfProjName.setVisible(false);

        tfProjPath = new JTextField();
        tfProjPath.setBounds(150, 92, 235, 20);
        tfProjPath.setColumns(10);
        tfProjPath.setText(Session.getWorkDir());
        tfProjPath.setVisible(false);
        tfProjPath.addKeyListener(this);

        btnParcourir = new JButton("...");
        btnParcourir.setBounds(395, 91, 25, 23);
        btnParcourir.addActionListener(this);
        btnParcourir.setVisible(false);

        btnEnd = new JButton("Finish");
        btnEnd.setBounds(350, 287, 89, 23);
        btnEnd.addActionListener(this);
        btnEnd.setVisible(false);

        lblFolder = new JLabel("Folder to create project in :");
        lblFolder.setBounds(150, 67, 235, 14);
        lblFolder.setVisible(false);

        tfCompPath = new JTextField();
        tfCompPath.setBounds(150, 148, 235, 20);
        formsPane.add(tfCompPath);
        tfCompPath.setColumns(10);
        tfCompPath.setText("gfortran");
        tfCompPath.setVisible(false);

        btnParcourir2 = new JButton("...");
        btnParcourir2.setBounds(395, 147, 25, 23);
        btnParcourir2.addActionListener(this);
        btnParcourir2.setVisible(false);

        tfFinalFilename = new JTextField(tfProjPath.getText());
        tfFinalFilename.setBackground(SystemColor.controlHighlight);
        tfFinalFilename.setBounds(150, 219, 235, 20);
        tfFinalFilename.setColumns(10);
        tfFinalFilename.setEditable(false);
        tfFinalFilename.setVisible(false);

        lblProjectName = new JLabel("Project title:");
        lblProjectName.setBounds(150, 11, 104, 14);
        lblProjectName.setVisible(false);

        lblResFilename = new JLabel("Resulting filename:");
        lblResFilename.setBounds(150, 194, 235, 14);
        lblResFilename.setVisible(false);

        lblCompilerPath = new JLabel("Compiler path :");
        lblCompilerPath.setBounds(150, 123, 235, 14);
        lblCompilerPath.setVisible(false);

        try {
            imagePanel = new DisplayImage("img.create");
            imagePanel.setSize(160, 310);
            imagePanel.setLocation(-20, -25);
            imagePanel.setVisible(true);
            formsPane.add(imagePanel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnNextBtn = new JButton("Next");
        btnNextBtn.setBounds(350, 287, 89, 23);
        btnNextBtn.addActionListener(this);
        btnNextBtn.setVisible(true);

        lblWelcome = new JLabel(
                "<html>\r\nWelcome to the project <b> creation wizard </b> ! <br>\r\nWe will guide you troughout the creation process.<br>\r\n<br>\r\nWhen ready to proceed, check <b>next</b> !<br>\r\n</html><br>");
        lblWelcome.setBounds(180, 65, 219, 171);
        lblWelcome.setVisible(true);

        JSeparator separator = new JSeparator();
        separator.setBounds(10, 284, 429, 14);
        separator.setVisible(true);

        formsPane.add(tfProjName);
        formsPane.add(tfProjPath);
        formsPane.add(tfFinalFilename);

        formsPane.add(btnParcourir);
        formsPane.add(btnParcourir2);
        formsPane.add(btnEnd);
        formsPane.add(btnNextBtn);

        formsPane.add(lblProjectName);
        formsPane.add(lblResFilename);
        formsPane.add(lblCompilerPath);
        formsPane.add(lblFolder);
        formsPane.add(separator);
        formsPane.add(lblWelcome);

        this.getContentPane().add(formsPane, BorderLayout.CENTER);

        this.setType(Type.UTILITY);
        this.setPreferredSize(new Dimension(455, 350));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource().equals(btnParcourir)) {
            String directory = Session.getWorkDir();
            JFileChooser chooser = new JFileChooser(directory);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Choose project folder");
            this.toBack();
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                directory = chooser.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "/");
                if (!directory.endsWith("/"))
                    directory += "/";
                tfProjPath.setText(directory);
            }
            this.toFront();

        } else if (arg0.getSource().equals(btnParcourir2)) {
            String directory = System.getProperty("user.home");
            JFileChooser chooser = new JFileChooser(directory);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Choose project folder");
            // this.toBack();
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                directory = chooser.getSelectedFile().getAbsolutePath();
                tfProjPath.setText(directory);

            }
            this.toFront();
        } else if (arg0.getSource().equals(btnNextBtn)) {
            lblWelcome.setVisible(false);
            tfProjName.setVisible(true);
            tfProjPath.setVisible(true);
            lblCompilerPath.setVisible(true);
            lblResFilename.setVisible(true);
            lblProjectName.setVisible(true);
            lblFolder.setVisible(true);
            tfCompPath.setVisible(true);
            btnParcourir.setVisible(true);
            btnParcourir2.setVisible(true);
            tfFinalFilename.setVisible(true);
            btnNextBtn.setVisible(false);
            btnEnd.setVisible(true);

        } else if (arg0.getSource().equals(btnEnd)) {

            if (!tfProjPath.getText().equals("") && !tfProjName.getText().equals("")) {
                this.setVisible(false);

                Session.addProject(
                        new Project(tfProjName.getText(), tfProjPath.getText(), "bin/", tfCompPath.getText()));
                try {
                    Session.getActiveProject().save();
                    Session.getActiveProject().createStartFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                this.setConcluded(true);
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

            } else {
                JOptionPane.showConfirmDialog(null, "Please, check empty fields", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public boolean isConcluded() {
        return concluded;
    }

    public void setConcluded(boolean concluded) {
        this.concluded = concluded;
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
        if (arg0.getSource().equals(tfProjName) || arg0.getSource().equals(tfProjPath)) {
            tfFinalFilename.setText(tfProjPath.getText() + tfProjName.getText() + Strings.s("application.project_extension"));
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }
}
