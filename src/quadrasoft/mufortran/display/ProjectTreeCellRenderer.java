package quadrasoft.mufortran.display;

import quadrasoft.mufortran.app.TreeFile;
import quadrasoft.mufortran.general.Project;
import quadrasoft.mufortran.resources.Resources;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class ProjectTreeCellRenderer implements TreeCellRenderer {
    private JLabel label;
    private JPanel pane = new JPanel();

    public ProjectTreeCellRenderer() {
        label = new JLabel();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        if (o instanceof TreeFile) {
            TreeFile TreeFile = (TreeFile) o;
            pane.setBackground(new Color(1f, 0f, 0f, .0f));

            label.setIcon(Resources.getImageResource(TreeFile.getIcon()));

            label.setText(TreeFile.getName());
            if (hasFocus) {
                label.setForeground(Color.blue);
            } else {

                label.setForeground(Color.black);

            }
            if (selected) {
                label.setForeground(new Color(173, 114, 13));
                label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
            } else {
                label.setForeground(Color.black);
                label.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize()));
            }
        } else if (o instanceof Project) {
            Project project = (Project) o;

            label.setText(project.getName());
            if (hasFocus) {
                label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
                label.setForeground(Color.black);
            } else {
                label.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize()));
                label.setForeground(Color.black);
                label.setIcon(Resources.getImageResource("icon.project"));
            }
            if (project.isSelected()) {
                label.setIcon(Resources.getImageResource("icon.projectOpen"));
                label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
            } else {
                label.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize()));
                label.setIcon(Resources.getImageResource("icon.project"));
            }

        } else {
            label.setIcon(null);
            label.setText("" + value);
        }
        pane.add(label);
        return pane;
    }
}
