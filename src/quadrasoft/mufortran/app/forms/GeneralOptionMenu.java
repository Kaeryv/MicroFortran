package quadrasoft.mufortran.app.forms;

import quadrasoft.mufortran.general.Log;
import quadrasoft.mufortran.general.Session;
import quadrasoft.mufortran.resources.Strings;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Properties;

public class GeneralOptionMenu extends JFrame implements ActionListener, KeyListener, ChangeListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JPanel generalPane = new JPanel();
    JCheckBox autochk;
    JLabel lblWarning;
    JComboBox<String> comboBox;
    JButton parcBtn;
    JCheckBox chckbxEnableAutosave;
    JLabel lblAutoSave;
    JLabel lblAutosaveTreshold;
    JSlider slider;
    private JLabel programNameLbl = new JLabel("\u00B5Fortran:");
    private JPanel buildPane = new JPanel();
    private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    private JButton applyBtn = new JButton("Apply");
    private JButton endBtn = new JButton("End");
    private JTextField workDirTf;
    private JLabel lblTheme;
    private JTextField textField_1;
    private JTextField textField_2;

    public GeneralOptionMenu() {
        super();
        this.setResizable(false);

        this.setPreferredSize(new Dimension(500, 300));
        tabbedPane.setBounds(0, 0, 484, 235);
        getContentPane().setLayout(null);
        tabbedPane.addTab("General", null, generalPane, null);
        generalPane.setLayout(null);

        programNameLbl.setBounds(54, 27, 206, 14);
        generalPane.add(programNameLbl);

        JLabel versionLbl = new JLabel("ver. " + Session.getVersion());
        versionLbl.setBounds(265, 24, 117, 20);
        generalPane.add(versionLbl);

        JLabel lblWorkingDirectory = new JLabel("Working directory:");
        lblWorkingDirectory.setBounds(54, 57, 206, 20);
        generalPane.add(lblWorkingDirectory);

        workDirTf = new JTextField();
        workDirTf.setBackground(SystemColor.controlHighlight);
        workDirTf.setEditable(false);
        workDirTf.setBounds(265, 54, 161, 26);
        workDirTf.setText(Session.getWorkDir());
        generalPane.add(workDirTf);
        workDirTf.setColumns(10);

        parcBtn = new JButton("...");
        parcBtn.setBounds(427, 53, 37, 29);
        parcBtn.addActionListener(this);
        generalPane.add(parcBtn);

        JLabel lblJavaRuntime = new JLabel("Java runtime:");
        lblJavaRuntime.setBounds(54, 95, 206, 20);
        generalPane.add(lblJavaRuntime);

        JLabel lblNewLabel = new JLabel(System.getProperty("java.version"));
        lblNewLabel.setBounds(265, 132, 199, 20);
        generalPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel(System.getProperty("java.vendor"));
        lblNewLabel_1.setBounds(265, 95, 199, 20);
        generalPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel(System.getProperty("os.arch") + ":" + System.getProperty("os.name") + " ver. "
                + System.getProperty("os.version"));
        lblNewLabel_2.setBounds(265, 165, 199, 20);
        generalPane.add(lblNewLabel_2);

        autochk = new JCheckBox("Auto check for updates");
        autochk.setBounds(53, 131, 186, 23);
        autochk.setSelected(Session.autoCheck());
        generalPane.add(autochk);

        JPanel editorPane = new JPanel();
        tabbedPane.addTab("Editor", null, editorPane, null);
        editorPane.setLayout(null);

        slider = new JSlider();
        slider.setToolTipText("<html> Edits before <b>autosave</b>");
        slider.setValue(Session.getAutoSaveTreshold());
        slider.setMinorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setBounds(243, 60, 200, 26);
        slider.addChangeListener(this);

        editorPane.add(slider);

        lblAutosaveTreshold = new JLabel("AutoSave treshold :");
        lblAutosaveTreshold.setToolTipText("<html> Edits before <b>autosave</b>");
        lblAutosaveTreshold.setBounds(15, 60, 165, 20);
        editorPane.add(lblAutosaveTreshold);

        lblAutoSave = new JLabel("20");
        lblAutoSave.setToolTipText("<html> Edits before <b>autosave</b>");
        lblAutoSave.setBounds(190, 60, 69, 20);
        lblAutoSave.setText("" + slider.getValue());
        editorPane.add(lblAutoSave);

        chckbxEnableAutosave = new JCheckBox("Enable Autosave");
        chckbxEnableAutosave.setBounds(15, 19, 280, 29);
        chckbxEnableAutosave.addActionListener(this);
        chckbxEnableAutosave.setSelected(Session.isAutoSave());
        editorPane.add(chckbxEnableAutosave);
        tabbedPane.addTab("Apparence", buildPane);
        buildPane.setLayout(null);

        lblTheme = new JLabel("Theme:");
        lblTheme.setBounds(15, 16, 69, 20);
        buildPane.add(lblTheme);

        comboBox = new JComboBox<String>();
        comboBox.setModel(
                new DefaultComboBoxModel<String>(new String[]{"Classic", "Aluminium", "Mint", "Leather"}));
        comboBox.setBounds(288, 16, 161, 26);

        comboBox.setSelectedItem(Session.parameter("Theme"));
        buildPane.add(comboBox);

        JLabel lblFontSize = new JLabel("Font size:");
        lblFontSize.setBounds(15, 52, 69, 20);

        buildPane.add(lblFontSize);

        textField_1 = new JTextField();
        textField_1.setText("" + Session.parameter("FontSize"));
        textField_1.setBounds(288, 52, 161, 26);
        buildPane.add(textField_1);
        textField_1.setColumns(10);

        JLabel lblFont = new JLabel("Font:");
        lblFont.setBounds(15, 88, 69, 20);
        buildPane.add(lblFont);

        textField_2 = new JTextField();
        textField_2.setBounds(288, 85, 161, 26);
        textField_2.setText(Session.parameter("FontName"));

        buildPane.add(textField_2);
        textField_2.setColumns(10);

        lblWarning = new JLabel("This requires restart to apply.");
        lblWarning.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblWarning.setForeground(Color.ORANGE);
        lblWarning.setBounds(15, 152, 271, 33);
        lblWarning.setVisible(false);
        buildPane.add(lblWarning);
        tabbedPane.setSelectedIndex(0);
        getContentPane().add(tabbedPane);
        endBtn.setBounds(386, 236, 98, 23);
        endBtn.addActionListener(this);
        applyBtn.addActionListener(this);
        getContentPane().add(endBtn);
        applyBtn.setBounds(291, 236, 92, 23);
        getContentPane().add(applyBtn);

        comboBox.addActionListener(this);

        if (chckbxEnableAutosave.isSelected()) {
            slider.setEnabled(true);
            lblAutosaveTreshold.setEnabled(true);
            lblAutoSave.setEnabled(true);
        } else {
            slider.setEnabled(false);
            lblAutosaveTreshold.setEnabled(false);
            lblAutoSave.setEnabled(false);
        }

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
            this.toFront();
        } else if (e.getSource().equals(parcBtn)) {
            JFileChooser chooser = new JFileChooser(Session.getWorkDir());
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Select folder to store projects in");
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                workDirTf.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        } else if (e.getSource().equals(comboBox)) {
            lblWarning.setVisible(true);
        } else if (e.getSource().equals(chckbxEnableAutosave)) {
            if (chckbxEnableAutosave.isSelected()) {
                slider.setEnabled(true);
                lblAutosaveTreshold.setEnabled(true);
                lblAutoSave.setEnabled(true);
            } else {
                slider.setEnabled(false);
                lblAutosaveTreshold.setEnabled(false);
                lblAutoSave.setEnabled(false);
            }
        }

    }

    private final void apply() {
        Session.setParameter("FontSize", textField_1.getText().trim());
        Session.setParameter("FontName", textField_2.getText().trim());
        Session.setParameter("Theme", (String) comboBox.getSelectedItem());
        Session.setAutoCheck(autochk.isSelected());
        if (new File(workDirTf.getText()).exists() && new File(workDirTf.getText()).isDirectory()) {
            Session.setWorkDir(workDirTf.getText());
        }
        Session.setAutoSaveTreshold(slider.getValue());
        Session.setAutoSave(chckbxEnableAutosave.isSelected());
        Session.save();
        Log.send("Saving changes");
    }

    @Override
    public void keyPressed(KeyEvent arg0) {}
    @Override
    public void keyReleased(KeyEvent arg0) {}
    @Override
    public void keyTyped(KeyEvent arg0) {}

    @Override
    public void stateChanged(ChangeEvent arg0) {
        lblAutoSave.setText("" + slider.getValue());
    }

    public void theme() {
        tabbedPane.setSelectedIndex(2);
    }

}
