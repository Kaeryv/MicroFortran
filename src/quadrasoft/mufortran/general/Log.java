package quadrasoft.mufortran.general;

import quadrasoft.mufortran.app.DirectoryViewerPane;
import quadrasoft.mufortran.app.MainFrame;
import quadrasoft.mufortran.fortran.BinaryManager;
import quadrasoft.mufortran.resources.Strings;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

public class Log extends JPanel implements KeyListener {

    private static final long serialVersionUID = -6182304516272936659L;
    private final static JTextField consoleInput = new JTextField();
    private static String log;
    private static JTextPane textArea = new JTextPane();
    private final static Style errstyle = textArea.addStyle("error", null);
    private final static Style inputstyle = textArea.addStyle("input", null);
    private final static Style infostyle = textArea.addStyle("info", null);
    private final static Style warningStyle = textArea.addStyle("warn", null);
    private final static Style fineStyle = textArea.addStyle("fine", null);
    private final static Style commandStyle = textArea.addStyle("command", null);
    private static StyledDocument doc = textArea.getStyledDocument();

    public Log() {

        this.setLayout(new BorderLayout());
        textArea.setEditable(false);

        JScrollPane cons_scrollpane = new JScrollPane();
        cons_scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cons_scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        cons_scrollpane.setViewportView(textArea);

        consoleInput.setBackground(Color.BLACK);
        consoleInput.setForeground(Color.GREEN);
        consoleInput.setCaretColor(Color.GREEN);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);

        // Events manager
        consoleInput.addKeyListener(this);

        // Style constants
        StyleConstants.setForeground(warningStyle, Color.orange);
        StyleConstants.setForeground(errstyle, Color.red);
        StyleConstants.setForeground(inputstyle, Color.lightGray);
        StyleConstants.setForeground(infostyle, Color.white);
        StyleConstants.setForeground(fineStyle, Color.green);
        StyleConstants.setForeground(commandStyle, Color.blue);

        this.setBorder(BorderFactory.createBevelBorder(0));
        this.add(cons_scrollpane, BorderLayout.CENTER);
        this.add(consoleInput, BorderLayout.SOUTH);
        this.setVisible(true);



        consoleInput.requestFocus();

    }

    public static void requestTerminalFocus() {
        consoleInput.requestFocus();
    }

    public static void send(String m) {
        String dateString = "[" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":"
                + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)
                + "] > ";

        try {


            if (m.contains("Error:")) {
                doc.insertString(doc.getLength(), dateString, warningStyle);

                doc.insertString(doc.getLength(), m + "\n", errstyle);
            }
            else if (m.contains("Warning:")) {
                doc.insertString(doc.getLength(), dateString, warningStyle);
                doc.insertString(doc.getLength(), m + "\n", warningStyle);
            }
            else if (m.contains("gracefully")) {
                doc.insertString(doc.getLength(), dateString, warningStyle);
                doc.insertString(doc.getLength(), m + "\n", fineStyle);
            }
            else if (m.contains("[[link]]")) {
                String link = m.substring(m.lastIndexOf("]")+1);
                JButton linkbtn = new JButton(" => Download latest version here.");
                linkbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                linkbtn.setForeground(Color.cyan);
                linkbtn.setBorder(BorderFactory.createEtchedBorder());
                linkbtn.setToolTipText(link);
                linkbtn.setBackground(Color.black);
                linkbtn.addActionListener(e -> {
                    if(e.getSource().equals(linkbtn)){
                        try {
                            Desktop.getDesktop().browse(new URI("https://github.com/Kaeryv/muFortran/releases"));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                textArea.insertComponent(linkbtn);

                doc.insertString(doc.getLength(), "\n", fineStyle);
            }
            else {
                doc.insertString(doc.getLength(), dateString, warningStyle);
                doc.insertString(doc.getLength(), m + "\n", infostyle);
            }
            textArea.setCaretPosition(doc.getLength());


            log = log + m + "\n";
            System.out.println(m);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent arg0) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == consoleInput) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                    String command = consoleInput.getText();
                    if (command.contains("exec")) {
                        String executableName = command.substring(command.lastIndexOf("exec") + 5, command.length());
                        if (new File(Session.getWorkDir() + "/" + executableName).isFile())
                            BinaryManager.launchFile(Session.getWorkDir() + "/" + executableName);
                        else
                            Log.send("Error: Executable not found.");
                    } else if (command.contains("cd")) {
                        sendCmd(command);
                        String path = command.substring(command.lastIndexOf("cd") + 3, command.length());
                        File newPath = new File(Session.getWorkDir() + "/" + path);
                        if (newPath.isDirectory()) {
                            try {
                                Session.setWorkDir(newPath.getCanonicalPath());
                            } catch (IOException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            DirectoryViewerPane.updateTree();
                        } else {
                            Log.send("Error: invalid path.");
                        }
                    } else if (command.contains("pwd")) {

                        try {
                            sendCmd(command);
                            doc.insertString(doc.getLength(), Session.getWorkDir() + "\n", inputstyle);
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    } else if (command.contains("ls")) {
                        try {
                            File current = new File(Session.getWorkDir());
                            sendCmd(command);
                            for (File file : current.listFiles()) {
                                doc.insertString(doc.getLength(), file.getName() + "\n", inputstyle);
                            }

                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    } else if (command.contains("version")) {
                        try {
                            doc.insertString(doc.getLength(), Session.getVersion() + "\n", inputstyle);
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    } else if (command.contains("open")) {
                        String filename = command.substring(command.lastIndexOf("open") + 4).trim();
                        if (new File(Session.getWorkDir() + "/" + filename).isFile())
                            ((MainFrame) this.getTopLevelAncestor()).open(Session.getWorkDir() + "/" + filename);
                        else if (new File(Session.getWorkDir() + "/" + filename + Strings.s("application.project_extension"))
                                .isFile())
                            ((MainFrame) this.getTopLevelAncestor())
                                    .open(Session.getWorkDir() + "/" + filename + Strings.s("application.project_extension"));
                        else
                            Log.send("Error: File " + Session.getWorkDir() + "/" + filename + "not found.");
                    } else {
                        Log.send("Warning: invalid command : " + command);
                    }
                    consoleInput.setText("");
                    textArea.setCaretPosition(doc.getLength());
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    private void sendCmd(String command) {
        try {

            String dateString = "[" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":"
                    + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)
                    + "] > ";
            doc.insertString(doc.getLength(), dateString, warningStyle);
            doc.insertString(doc.getLength(), command + "\n", commandStyle);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }


}
