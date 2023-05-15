package quadrasoft.mufortran.app.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class FileSelector extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    DefaultListModel<String> listModel_left;

    DefaultListModel<String> listModel_right;
    JButton toLeftBtn;
    JButton toRightBtn;
    JList<String> list_left;
    JList<String> list_right;
    List<String> selectedFiles = new ArrayList<String>();
    private JButton endBtn;
    private boolean concluded = false;
    private JPanel panel;
    private JLabel lblChooseFilesYou;
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;
    private JSeparator separator;
    private JSeparator separator_1;
    private JPanel panel_1;

    public FileSelector(List<String> files) {
        super("Project wizard");
        setTitle("Add files recursively");
        setResizable(false);
        setType(Type.UTILITY);
        this.setPreferredSize(new Dimension(700, 300));
        this.setVisible(true);

        listModel_left = new DefaultListModel<String>();
        listModel_right = new DefaultListModel<String>();

        for (String file : files) {
            listModel_left.addElement(file);
        }
        JScrollPane scrollPane_left = new JScrollPane();
        getContentPane().add(scrollPane_left, BorderLayout.WEST);

        JScrollPane scrollPane_right = new JScrollPane();
        getContentPane().add(scrollPane_right, BorderLayout.EAST);

        JPanel centerPane = new JPanel();
        centerPane.setPreferredSize(new Dimension(100, 400));
        getContentPane().add(centerPane, BorderLayout.CENTER);
        centerPane.setLayout(new BorderLayout(0, 0));

        toLeftBtn = new JButton("<<");
        toLeftBtn.addActionListener(this);
        centerPane.add(toLeftBtn, BorderLayout.SOUTH);

        toRightBtn = new JButton(">>");
        toRightBtn.addActionListener(this);
        centerPane.add(toRightBtn, BorderLayout.NORTH);

        endBtn = new JButton("Done");
        endBtn.addActionListener(this);
        centerPane.add(endBtn, BorderLayout.CENTER);

        list_left = new JList<String>(listModel_left);
        list_left.setPreferredSize(new Dimension(200, 800));
        scrollPane_left.setViewportView(list_left);
        scrollPane_left.setPreferredSize(new Dimension(300, 300));
        list_right = new JList<String>(listModel_right);
        list_right.setPreferredSize(new Dimension(200, 800));
        scrollPane_right.setViewportView(list_right);
        scrollPane_right.setPreferredSize(new Dimension(300, 300));

        panel = new JPanel();

        getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new BorderLayout(0, 0));

        lblChooseFilesYou = new JLabel("Choose files you want to add to the project:");
        panel.add(lblChooseFilesYou, BorderLayout.NORTH);

        separator = new JSeparator();
        panel.add(separator, BorderLayout.SOUTH);

        separator_1 = new JSeparator();
        panel.add(separator_1, BorderLayout.CENTER);

        panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.SOUTH);
        panel_1.setLayout(new BorderLayout(0, 0));

        lblNewLabel_1 = new JLabel("Selected");
        panel_1.add(lblNewLabel_1, BorderLayout.EAST);

        lblNewLabel = new JLabel("Available");
        panel_1.add(lblNewLabel);
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(toLeftBtn)) {
            if (list_right.getSelectedValue() != null) {
                int index = list_right.getSelectedIndex();
                selectedFiles.remove(list_right.getSelectedValue());
                listModel_left.addElement(list_right.getSelectedValue());
                listModel_right.removeElementAt(index);
                list_right.setSelectedIndex(index);
            }
        } else if (e.getSource().equals(toRightBtn)) {

            if (list_left.getSelectedValue() != null) {
                int index = list_left.getSelectedIndex();
                selectedFiles.add(list_left.getSelectedValue());
                listModel_right.addElement(list_left.getSelectedValue());
                listModel_left.removeElementAt(list_left.getSelectedIndex());
                list_left.setSelectedIndex(index);
            }
        } else if (e.getSource().equals(endBtn)) {
            if (!selectedFiles.isEmpty()) {
                this.setConcluded(true);
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            } else {
                JOptionPane.showConfirmDialog(null, "Error: No file to import.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public List<String> getSelectedFiles() {
        return selectedFiles;
    }

    public boolean isConcluded() {
        return concluded;
    }

    public void setConcluded(boolean concluded) {
        this.concluded = concluded;
    }
}
