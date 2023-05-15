package quadrasoft.mufortran.general;

import quadrasoft.mufortran.display.ErrorLineRenderer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompilerLog extends JPanel {

    private static final long serialVersionUID = -6182304516272936659L;
    static DefaultListModel<String> listModel;
    private static List<String> log = new ArrayList<String>();
    private static JList<String> lines;

    public CompilerLog() {
        listModel = new DefaultListModel<String>();
        lines = new JList<String>(listModel);
        this.setLayout(new BorderLayout());
        JScrollPane cons_scrollpane = new JScrollPane();
        cons_scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cons_scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        cons_scrollpane.setViewportView(lines);
        lines.setBackground(Color.WHITE);
        lines.setForeground(Color.RED);
        lines.setCellRenderer(new ErrorLineRenderer());
        this.setBorder(BorderFactory.createBevelBorder(0));
        this.add(cons_scrollpane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public static JList<String> getList() {
        return lines;
    }

    public static void reset() {
        listModel.removeAllElements();
    }

    public static void save(String string) {

        File file = new File(string);
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("=====================================");
            fileWriter.write(System.getProperty("line.separator"));
            fileWriter.write("	Compiler output");
            fileWriter.write(System.getProperty("line.separator"));
            fileWriter.write("=====================================");
            fileWriter.write(System.getProperty("line.separator"));
            for (String line : log) {
                fileWriter.write(line);
                fileWriter.write(System.getProperty("line.separator"));
            }
            fileWriter.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public static void send(String m) {
        listModel.addElement(m);
        log.add(m);

    }
}
