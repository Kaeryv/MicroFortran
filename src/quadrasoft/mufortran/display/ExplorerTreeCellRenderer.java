package quadrasoft.mufortran.display;

import quadrasoft.mufortran.resources.Resources;
import quadrasoft.mufortran.resources.Strings;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.io.File;

public class ExplorerTreeCellRenderer implements TreeCellRenderer {
    private JLabel label;

    public ExplorerTreeCellRenderer() {
        label = new JLabel();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        if (o instanceof File) {
            File File = (File) o;
            if (File.isDirectory()) {
                label.setIcon(Resources.getImageResource("icon.folderTree"));
            } else if (File.isFile()) {
                if (File.getAbsolutePath().endsWith(".exe")) {
                    label.setIcon(Resources.getImageResource("icon.exec"));
                } else if (File.getAbsolutePath().contains(".f")) {
                    label.setIcon(Resources.getImageResource("icon.source"));
                } else if (File.getAbsolutePath().endsWith(".mod")) {
                    label.setIcon(Resources.getImageResource("icon.mod"));
                } else if (File.getAbsolutePath().endsWith(Strings.s("application.project_extension"))) {
                    label.setIcon(Resources.getImageResource("icon.project"));
                } else if (File.getAbsolutePath().endsWith(".ps")) {
                    label.setIcon(Resources.getImageResource("icon.graph"));
                } else {
                    label.setIcon(Resources.getImageResource("icon.file"));
                }
            }
            // label.setIcon(new ImageIcon(getClass().getResource("")));
            label.setText(File.getName());
            // label.setPreferredSize(label.getMaximumSize());
            if (selected) {
                label.setForeground(new Color(173, 114, 13));
                label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
            } else {
                label.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize()));
                label.setForeground(Color.black);
            }
        } else {
            label.setIcon(null);
            label.setText("" + value);
        }
        label.setPreferredSize(new Dimension(300, 20));
        return label;
    }
}
