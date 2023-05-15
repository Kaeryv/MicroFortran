package quadrasoft.mufortran.app.forms;

import quadrasoft.mufortran.general.Log;
import quadrasoft.mufortran.general.Project;
import quadrasoft.mufortran.general.Session;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectOptionMenu extends JFrame implements ActionListener, KeyListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    static DefaultListModel<String> listModel;
    private final JPanel generalPane = new JPanel();
    private final JScrollPane filesSp = new JScrollPane();
    JLabel lblNewLabel_1 = new JLabel("Author:");
    JLabel lblNewLabel = new JLabel("Project folder:");
    JLabel projNameLabel = new JLabel("Project name:");
    JLabel lblNewLabel_4 = new JLabel("Files list:");
    JCheckBox printLog = new JCheckBox("Print log to file");
    JList<String> list;
    JPanel buildPane = new JPanel();
    JPanel executePane = new JPanel();
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    JButton applyBtn = new JButton("Apply");
    JButton endBtn = new JButton("End");
    java.util.List<String> externals = new ArrayList<String>();
    JList<String> list_ext;
    private JTextField nameTf;
    private JTextField pathTf;
    private JTextField AuthorTf;
    private JTextField compPath;
    private JTextField buildPath;
    private JTextField exePlace;
    private JTextField execArg;
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnParc;
    private JLabel lblExecutableName;

    private JTextField exeNameTf;
    private JTextField textField;
    private DefaultListModel<String> listModel1;
    private JButton btnClear;
    private JTextField textField_1;

    public ProjectOptionMenu(Project project) {
        super();
        this.setResizable(false);
        compPath = new JTextField(project.getCompilerPath());
        if (compPath.getText().endsWith(".exe") && new File(compPath.getText()).exists()) {
            compPath.setBackground(Color.GREEN);
        } else if (compPath.getText().endsWith(".exe")) {
            compPath.setBackground(Color.RED);
        } else {
            compPath.setBackground(Color.CYAN);
        }
        compPath.addKeyListener(this);
        compPath.setBounds(133, 56, 295, 20);
        compPath.setColumns(10);

        this.setPreferredSize(new Dimension(500, 300));
        tabbedPane.setBounds(0, 0, 484, 235);

        listModel = new DefaultListModel<String>();
        for (String file : project.getSource()) {
            listModel.addElement(file);
        }
        listModel1 = new DefaultListModel<String>();
        for (String file : project.getExternals()) {
            listModel1.addElement(file);
        }

        getContentPane().setLayout(null);
        tabbedPane.addTab("General", null, generalPane, null);
        generalPane.setLayout(null);

        nameTf = new JTextField(project.getName());
        nameTf.setBackground(SystemColor.controlHighlight);
        nameTf.setEditable(false);
        nameTf.setBounds(270, 24, 199, 20);
        generalPane.add(nameTf);
        nameTf.setColumns(10);

        projNameLabel.setBounds(20, 27, 86, 14);
        generalPane.add(projNameLabel);

        lblNewLabel.setBounds(20, 58, 86, 14);
        generalPane.add(lblNewLabel);

        pathTf = new JTextField(project.getPath());
        pathTf.setBackground(SystemColor.controlHighlight);
        pathTf.setEditable(false);
        pathTf.setBounds(128, 55, 341, 20);
        generalPane.add(pathTf);
        pathTf.setColumns(10);

        AuthorTf = new JTextField(project.getAuthor());
        AuthorTf.setBackground(SystemColor.controlHighlight);
        AuthorTf.setEditable(false);
        AuthorTf.setBounds(270, 86, 199, 20);
        generalPane.add(AuthorTf);
        AuthorTf.setColumns(10);

        lblNewLabel_1.setBounds(20, 89, 86, 14);
        generalPane.add(lblNewLabel_1);
        filesSp.setBounds(20, 132, 449, 64);

        generalPane.add(filesSp);

        lblNewLabel_4.setBounds(20, 114, 86, 14);
        generalPane.add(lblNewLabel_4);
        list = new JList<String>(listModel);
        list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        list.setBounds(86, 145, 199, 58);
        filesSp.setViewportView(list);
        tabbedPane.addTab("Build options", buildPane);
        buildPane.setLayout(null);

        buildPane.add(compPath);

        JButton btnNewButton = new JButton("New button");
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                chooser.removeChoosableFileFilter(chooser.getFileFilter());
                FileFilter filter = new FileNameExtensionFilter("Executable file", "exe", "bat", "sh", "app");
                chooser.addChoosableFileFilter(filter);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    compPath.setText(chooser.getSelectedFile().getAbsolutePath());
                }
                if (compPath.getText().endsWith(".exe") && new File(compPath.getText()).exists()) {
                    compPath.setBackground(Color.GREEN);
                } else if (compPath.getText().endsWith(".exe")) {
                    compPath.setBackground(Color.RED);
                } else {
                    compPath.setBackground(Color.CYAN);
                }
            }
        });
        btnNewButton.setBounds(438, 55, 31, 23);
        buildPane.add(btnNewButton);

        JLabel lblNewLabel_5 = new JLabel("Compiler path:");
        lblNewLabel_5.setBounds(10, 59, 108, 14);
        buildPane.add(lblNewLabel_5);

        JLabel lblBuildPath = new JLabel("Build path:");
        lblBuildPath.setBounds(10, 119, 232, 14);
        buildPane.add(lblBuildPath);

        buildPath = new JTextField(project.getBuildPath());
        buildPath.setBounds(252, 116, 168, 20);
        buildPane.add(buildPath);
        buildPath.setColumns(10);

        lblExecutableName = new JLabel("Output path");
        lblExecutableName.setBounds(10, 147, 232, 14);
        buildPane.add(lblExecutableName);

        exeNameTf = new JTextField();
        exeNameTf.addKeyListener(this);
        exeNameTf.setBounds(252, 169, 171, 20);
        exeNameTf.setText(project.getExecutableName());
        buildPane.add(exeNameTf);
        exeNameTf.setColumns(10);

        tabbedPane.setSelectedIndex(0);
        getContentPane().add(tabbedPane);

        JPanel externalPane = new JPanel();
        tabbedPane.addTab("More options", null, externalPane, null);
        externalPane.setLayout(null);

        textField = new JTextField();
        textField.setBounds(10, 140, 352, 20);
        externalPane.add(textField);
        textField.setColumns(10);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 459, 118);
        externalPane.add(scrollPane);

        list_ext = new JList<String>(listModel1);
        list_ext.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        scrollPane.setViewportView(list_ext);

        btnParc = new JButton("New button");
        btnParc.setBounds(372, 139, 23, 23);
        btnParc.addActionListener(this);
        externalPane.add(btnParc);

        btnAdd = new JButton("add");
        btnAdd.addActionListener(this);
        btnAdd.setBounds(372, 171, 97, 23);
        externalPane.add(btnAdd);

        btnRemove = new JButton("remove");
        btnRemove.addActionListener(this);
        btnRemove.setBounds(273, 171, 89, 23);
        externalPane.add(btnRemove);

        btnClear = new JButton("Clear");
        btnClear.addActionListener(this);
        btnClear.setBounds(405, 139, 64, 23);
        externalPane.add(btnClear);
        tabbedPane.addTab("Execution", null, executePane, null);
        executePane.setLayout(null);

        JLabel execPlace = new JLabel("Execution place:");
        execPlace.setBounds(10, 60, 99, 14);
        executePane.add(execPlace);

        exePlace = new JTextField();
        exePlace.setBounds(119, 57, 197, 20);
        executePane.add(exePlace);
        exePlace.setColumns(10);
        exePlace.setText(Session.getActiveProject().getExecutionPath());

        JLabel lblExecutionArgin = new JLabel("Execution argin:");
        lblExecutionArgin.setBounds(10, 85, 99, 14);
        executePane.add(lblExecutionArgin);

        execArg = new JTextField(project.getArgument());
        execArg.setBounds(119, 82, 197, 20);
        executePane.add(execArg);
        execArg.setColumns(10);

        printLog.setBounds(11, 12, 186, 23);
        printLog.setSelected(project.isPrintLog());
        buildPane.add(printLog);

        JLabel label = new JLabel("Executable name");
        label.setBounds(10, 172, 232, 14);
        buildPane.add(label);

        textField_1 = new JTextField();
        textField_1.setText((String) null);
        textField_1.setColumns(10);
        textField_1.setBounds(252, 144, 171, 20);
        buildPane.add(textField_1);
        endBtn.setBounds(433, 236, 51, 23);
        endBtn.addActionListener(this);
        applyBtn.addActionListener(this);
        getContentPane().add(endBtn);
        applyBtn.setBounds(374, 236, 59, 23);
        getContentPane().add(applyBtn);
        externals = project.getExternals();

        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(endBtn)) {
            apply();
            this.dispose();
        } else if (e.getSource().equals(applyBtn)) {
            apply();
        } else if (e.getSource().equals(btnAdd)) {
            if (!textField.getText().equals("")) {
                externals.add(textField.getText());
                listModel1.addElement(textField.getText());
            }
        } else if (e.getSource().equals(btnParc)) {
            JFileChooser chooser = new JFileChooser(Session.getWorkDir());
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Adding RL");
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                textField.setText(textField.getText() + chooser.getSelectedFile().getAbsolutePath());
            }

        } else if (e.getSource().equals(btnRemove)) {
            if (list_ext.getSelectedIndex() >= 0) {
                externals.remove(list_ext.getSelectedValue());
                listModel1.remove(list_ext.getSelectedIndex());
                // list_ext.remove(list_ext.getSelectedIndex());
            }

        } else if (e.getSource().equals(btnClear)) {
            textField.setText("");
        }

    }

    private final void apply() {
        Session.getActiveProject().setArgument(execArg.getText());
        Session.getActiveProject().setAuthor(AuthorTf.getText());
        if (buildPath.getText().endsWith("/"))
            Session.getActiveProject().setBuildPath(buildPath.getText());
        else
            Session.getActiveProject().setBuildPath(buildPath.getText() + "/");
        Session.getActiveProject().setCompilerOption("-o");
        Session.getActiveProject().setCompilerPath(compPath.getText());
        Session.getActiveProject().setExecutableName(exeNameTf.getText());
        Session.getActiveProject().setPrintLog(printLog.isSelected());
        Session.getActiveProject().setExternals(externals);
        Session.getActiveProject().setExecutionPath(exePlace.getText());

        try {
            Session.getActiveProject().save();
            Log.send("Saving change to project.");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
        if (compPath.getText().endsWith(".exe") && new File(compPath.getText()).exists()) {
            compPath.setBackground(Color.GREEN);
        } else if (compPath.getText().endsWith(".exe")) {
            compPath.setBackground(Color.RED);
        } else {
            compPath.setBackground(Color.CYAN);
        }

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }
}
