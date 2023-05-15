package quadrasoft.mufortran.app.forms;

import quadrasoft.mufortran.general.Project;
import quadrasoft.mufortran.general.Session;
import quadrasoft.mufortran.display.DisplayImage;
import quadrasoft.mufortran.resources.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectImporterFrame extends JFrame implements ActionListener, KeyListener, WindowListener {

    /**
     *
     */

    private static final long serialVersionUID = 1L;
    List<String> candidats = new ArrayList<String>();
    FileSelector selector;
    private boolean concluded;
    private List<String> files = new ArrayList<String>();
    private JPanel formsPane = new JPanel();

    private DisplayImage imagePanel;

    private JTextField tfProjName;

    private JTextField tfProjPath;

    private JTextField tfCompPath;

    private JTextField tfFinalFilename;

    private JLabel lblWelcome;

    private JLabel lblCompilerPath;

    private JLabel lblResFilename;
    private JLabel lblProjName;

    private JLabel lblFolder;

    private JButton btnParcourir;

    private JButton btnParcourir2;

    private JButton btnEnd;
    private JButton btnNextBtn;

    public ProjectImporterFrame() {
        super("Import wizard");
        this.setVisible(false);

        formsPane.setLayout(null);

        tfProjName = new JTextField();
        tfProjName.setBackground(SystemColor.controlHighlight);
        tfProjName.setBounds(150, 36, 235, 20);
        tfProjName.setColumns(10);
        tfProjName.addKeyListener(this);
        tfProjName.setVisible(false);
        tfProjName.setEditable(false);

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

        lblFolder = new JLabel("Folder to load sources from :");
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

        tfFinalFilename = new JTextField(tfProjPath.getText() + tfProjName.getText());
        tfFinalFilename.setBackground(SystemColor.controlHighlight);
        tfFinalFilename.setBounds(150, 219, 235, 20);
        tfFinalFilename.setColumns(10);
        tfFinalFilename.setEditable(false);
        tfFinalFilename.setVisible(false);

        lblProjName = new JLabel("Project Name");
        lblProjName.setBounds(150, 11, 104, 14);
        lblProjName.setVisible(false);

        lblResFilename = new JLabel("Resulting filename:");
        lblResFilename.setBounds(150, 194, 235, 14);
        lblResFilename.setVisible(false);

        lblCompilerPath = new JLabel("Compiler path :");
        lblCompilerPath.setBounds(150, 123, 235, 14);
        lblCompilerPath.setVisible(false);

        try {
            imagePanel = new DisplayImage("img.import");
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
                "<html>\r\nWelcome to the source<b> importation wizard </b> ! <br>\r\nSelect a folder, the wizard will load all the source files.<br>\r\nThen you 'll be able to select the files you want.<br>\r\nThe project's name is set to the folder's name.<br>\r\n<br>\r\nWhen ready to proceed, check <b>next</b> !<br>\r\n</html><br>");
        lblWelcome.setBounds(180, 65, 219, 193);
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

        formsPane.add(lblProjName);
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
                directory = chooser.getSelectedFile().getAbsolutePath();
                tfProjPath.setText(directory.replaceAll("\\\\", "/"));

            }
            this.toFront();
            updateFields();

        } else if (arg0.getSource().equals(btnParcourir2)) {
            String directory = System.getProperty("user.home");
            JFileChooser chooser = new JFileChooser(directory);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Choose project folder");
            // this.toBack();
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                directory = chooser.getSelectedFile().getAbsolutePath();
                tfProjPath.setText(directory.replaceAll("\\\\", "/"));

            }
            this.toFront();
            updateFields();
        } else if (arg0.getSource().equals(btnNextBtn)) {
            lblWelcome.setVisible(false);
            tfProjName.setVisible(true);
            tfProjPath.setVisible(true);
            lblCompilerPath.setVisible(true);
            lblResFilename.setVisible(true);
            lblProjName.setVisible(true);
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

                loadFolder(tfProjPath.getText());
                selector = new FileSelector(candidats);
                selector.setLocationRelativeTo(null);
                selector.addWindowListener(this);

            } else {
                JOptionPane.showConfirmDialog(null, "Please, check empty fields", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public boolean isConcluded() {
        return concluded;
    }

    public void setConcluded(boolean concluded) {
        this.concluded = concluded;
    }

    @Override
    public void keyPressed(KeyEvent arg0) {

    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        updateFields();
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    public void loadFolder(String folderPath) {
        for (File file : new File(folderPath).listFiles()) {
            if (!file.isDirectory()) {
                String path = file.getAbsolutePath().replaceAll("\\\\", "/");
                if (path.contains(".f") && path.contains(folderPath)) {
                    candidats.add(path.substring(tfProjPath.getText().length() + 1));
                }
            } else {
                String path = file.getAbsolutePath().replaceAll("\\\\", "/");
                loadFolder(path);
            }
        }
    }

    public void updateFields() {
        if (!tfFinalFilename.equals(Session.getWorkDir()))
            tfProjName.setText(tfProjPath.getText().substring(tfProjPath.getText().lastIndexOf("/") + 1));
        if (!tfProjName.getText().equals("")) {
            tfFinalFilename.setText(tfProjPath.getText() + tfProjName.getText() + Strings.s("app:projectextension"));
        }

    }

    @Override
    public void windowActivated(WindowEvent arg0) {

    }

    @Override
    public void windowClosed(WindowEvent arg0) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (e.getSource().equals(selector)) {

            if (selector.isConcluded()) {
                candidats = selector.getSelectedFiles();
            } else {
                candidats.removeAll(candidats);
            }

            selector.setVisible(false);
            selector.dispose();

            Session.addProject(new Project(tfProjName.getText(),
                    tfProjPath.getText().substring(0, tfProjPath.getText().lastIndexOf("/") + 1), "bin/",
                    tfCompPath.getText(), candidats));

            try {
                Session.getActiveProject().save();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            this.setConcluded(true);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {

    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {

    }

    @Override
    public void windowIconified(WindowEvent arg0) {

    }

    @Override
    public void windowOpened(WindowEvent arg0) {

    }
}
