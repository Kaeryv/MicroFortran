package quadrasoft.mufortran.app.forms;

import quadrasoft.mufortran.fortran.Snippet;
import quadrasoft.mufortran.general.Session;
import quadrasoft.mufortran.display.DisplayImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SourceCreatorFrame extends JFrame implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    JLabel lblPrefab;
    JCheckBox chckbxAddToActive;
    private boolean concluded;
    private boolean addThisToProject;
    private String path;
    private JPanel formsPane = new JPanel();
    private DisplayImage imagePanel;
    private JTextField tfFileName;
    private JTextField tfFilePath;
    private JTextField tfFinalFilename;
    private JLabel lblWelcome;
    private JLabel lblResFilename;
    private JLabel lblFileName;
    private JLabel lblFolder;
    private JButton btnParcourir;
    private JButton btnEnd;
    private JButton btnNextBtn;
    private JComboBox<String> cbFileType;
    private JComboBox<String> cbFileExt;

    public SourceCreatorFrame() {
        super("Project wizard");
        this.setVisible(false);

        formsPane.setLayout(null);

        tfFileName = new JTextField();
        tfFileName.setBackground(Color.WHITE);
        tfFileName.setBounds(150, 36, 170, 20);
        tfFileName.setColumns(10);
        tfFileName.addKeyListener(this);
        tfFileName.setVisible(false);

        tfFilePath = new JTextField();
        tfFilePath.setBounds(150, 92, 235, 20);
        tfFilePath.setColumns(10);
        tfFilePath.setText(Session.getWorkDir());
        if (Session.getActiveProject() != null) {
            tfFilePath.setText(Session.getActiveProject().getPath());
        }
        tfFilePath.setVisible(false);
        tfFilePath.addKeyListener(this);

        btnParcourir = new JButton("...");
        btnParcourir.setBounds(395, 92, 25, 20);
        btnParcourir.addActionListener(this);
        btnParcourir.setVisible(false);

        btnEnd = new JButton("Finish");
        btnEnd.setBounds(350, 287, 89, 23);
        btnEnd.addActionListener(this);
        btnEnd.setVisible(false);

        lblFolder = new JLabel("Folder to create file in :");
        lblFolder.setBounds(150, 67, 235, 14);
        lblFolder.setVisible(false);

        tfFinalFilename = new JTextField(tfFilePath.getText());
        tfFinalFilename.setBackground(SystemColor.controlHighlight);
        tfFinalFilename.setBounds(150, 219, 235, 20);
        tfFinalFilename.setColumns(10);
        tfFinalFilename.setEditable(false);
        tfFinalFilename.setVisible(false);

        lblFileName = new JLabel("File name:");
        lblFileName.setBounds(150, 11, 104, 14);
        lblFileName.setVisible(false);

        lblResFilename = new JLabel("Resulting filename:");
        lblResFilename.setBounds(150, 194, 235, 14);
        lblResFilename.setVisible(false);

        try {
            imagePanel = new DisplayImage("img.source");
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
                "<html>\r\nWelcome to the source file <b> creation wizard </b> ! <br>\r\nWe will guide you troughout the creation process.<br>\r\n<br>\r\nWhen ready to proceed, check <b>next</b> !<br>\r\n</html><br>");
        lblWelcome.setBounds(180, 65, 219, 171);
        lblWelcome.setVisible(true);

        JSeparator separator = new JSeparator();
        separator.setBounds(10, 284, 429, 14);
        separator.setVisible(true);

        lblPrefab = new JLabel("Prefab :");
        lblPrefab.setBounds(150, 128, 69, 20);
        lblPrefab.setVisible(false);

        cbFileType = new JComboBox<String>();
        cbFileType.setModel(new DefaultComboBoxModel<String>(
                new String[]{"none", "program", "module", "function", "subroutine"}));
        cbFileType.setBounds(150, 152, 235, 26);
        cbFileType.setVisible(false);

        cbFileExt = new JComboBox<String>();
        cbFileExt.setModel(
                new DefaultComboBoxModel<String>(new String[]{".f90", ".f95", ".f", ".F90", ".F95", ".F"}));
        cbFileExt.setBounds(325, 36, 53, 20);
        cbFileExt.setVisible(false);
        cbFileExt.addActionListener(this);

        formsPane.add(tfFileName);
        formsPane.add(tfFilePath);
        formsPane.add(tfFinalFilename);

        formsPane.add(btnParcourir);
        formsPane.add(btnEnd);
        formsPane.add(btnNextBtn);

        formsPane.add(lblFileName);
        formsPane.add(lblResFilename);
        formsPane.add(lblFolder);
        formsPane.add(separator);
        formsPane.add(lblWelcome);
        formsPane.add(lblPrefab);

        formsPane.add(cbFileExt);
        formsPane.add(cbFileType);
        this.getContentPane().add(formsPane, BorderLayout.CENTER);

        chckbxAddToActive = new JCheckBox("Add to active project.");
        chckbxAddToActive.setBounds(146, 246, 239, 23);
        chckbxAddToActive.setVisible(false);
        chckbxAddToActive.setSelected(true);
        formsPane.add(chckbxAddToActive);

        this.setType(Type.UTILITY);
        this.setPreferredSize(new Dimension(455, 350));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnParcourir)) {
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
                tfFilePath.setText(directory.replaceAll("\\\\", "/"));

            }
            this.toFront();
            updateFields();

        } else if (e.getSource().equals(btnNextBtn)) {
            lblWelcome.setVisible(false);
            tfFileName.setVisible(true);
            tfFilePath.setVisible(true);
            lblResFilename.setVisible(true);
            lblFileName.setVisible(true);
            lblFolder.setVisible(true);
            btnParcourir.setVisible(true);
            tfFinalFilename.setVisible(true);
            lblPrefab.setVisible(true);
            cbFileExt.setVisible(true);
            cbFileType.setVisible(true);
            btnNextBtn.setVisible(false);
            btnEnd.setVisible(true);
            if (Session.getActiveProject() != null) {
                chckbxAddToActive.setVisible(true);
            }

        } else if (e.getSource().equals(btnEnd)) {

            if (!tfFilePath.getText().equals("") && !tfFileName.getText().equals("")) {
                this.setVisible(false);
                this.setPath(tfFinalFilename.getText());
                // We will begin ensuring the file doesn't already exists:
                if (new File(this.getPath()).exists()) {
                    if (JOptionPane.showConfirmDialog(new JFrame(),
                            "The file already exists, do you want to overwrite it ?") == JOptionPane.OK_OPTION) {
                        createFile(this.getPath());
                    } else {

                    }
                } else {
                    createFile(this.getPath());
                }

                if (Session.getActiveProject() != null) {
                    this.setAddThisToProject(chckbxAddToActive.isSelected());
                    this.setPath(this.getPath().substring(Session.getActiveProject().getPath().length(),
                            this.getPath().length()));
                } else {
                    this.setAddThisToProject(false);
                }
                this.setConcluded(true);
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

            } else {
                JOptionPane.showConfirmDialog(null, "Please, check empty fields", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource().equals(cbFileExt))
            updateFields();
    }

    private void createFile(String path) {
        Path filepath = Paths.get(path);
        Snippet snip = new Snippet(cbFileType.getSelectedItem().toString(), tfFileName.getText());
        try {
            snip.write_to_file(filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isAddThisToProject() {
        return addThisToProject;
    }

    public void setAddThisToProject(boolean addThisToProject) {
        this.addThisToProject = addThisToProject;
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
        if (arg0.getSource().equals(tfFileName) || arg0.getSource().equals(tfFilePath)) {
            updateFields();
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {}

    public void updateFields() {
        tfFinalFilename.setText(tfFilePath.getText() + tfFileName.getText() + cbFileExt.getSelectedItem());
    }
}
