package quadrasoft.mufortran.display;

import quadrasoft.mufortran.resources.Resources;

import javax.swing.*;
import java.awt.*;

public class ErrorLineRenderer extends JLabel implements ListCellRenderer<Object> {

    private static final long serialVersionUID = 1L;

    public ErrorLineRenderer() {
        setOpaque(true);
        setIconTextGap(2);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        if (value instanceof String) {
            String entry = (String) value;

            if (entry.contains("Error:")) {
                setText("<html><font color=\"red\"><b>>> " + entry + "</font></b>");
            } else if (entry.contains("Warning")) {
                setText("<html><font color=\"orange\"><b>>> " + entry + "</font></b>");
            } else if (entry.contains(".f")) {
                setIcon(Resources.getImageResource("icon.error"));
                setText("<html><b><font color=\"black\">Location:</font><font color=\"gray\"> " + entry
                        + "</font></b>");
            } else {
                if (entry.equals("")) {
                    setText(entry);
                } else {
                    setText("         > " + entry);

                }
                setIcon(null);
            }
        }
        if (isSelected) {
            setBackground(Color.orange);
            setForeground(Color.black);
        } else {
            setBackground(Color.white);
            setForeground(Color.black);
        }
        return this;
    }
}