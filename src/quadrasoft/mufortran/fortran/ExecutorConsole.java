package quadrasoft.mufortran.fortran;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.IOException;



@SuppressWarnings("serial")
public class ExecutorConsole extends JFrame implements KeyListener, WindowListener {

    private static JTextPane textArea = new JTextPane();

    static StyledDocument doc = textArea.getStyledDocument();

    static Style errstyle = textArea.addStyle("error", null);
    static Style inputstyle = textArea.addStyle("input", null);
    static Style infostyle = textArea.addStyle("info", null);
    private static JTextField cmdInput;
    int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    JScrollPane cons_scrollpane;
    DefaultCaret caret;
    private boolean processRunning = false;
    private BufferedWriter bw;
    private Process processReference;

    public ExecutorConsole() {
        super("µExecutor");
        this.setSize(600, 300);
        this.setIconImage((new ImageIcon(this.getClass().getResource("icons/console.png")).getImage()));
        StyleConstants.setForeground(errstyle, Color.red);
        StyleConstants.setForeground(inputstyle, Color.lightGray);
        StyleConstants.setForeground(infostyle, Color.white);
        getContentPane().setLayout(new BorderLayout());
        textArea.setEditable(false);
        caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        cons_scrollpane = new JScrollPane();
        cons_scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cons_scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        cons_scrollpane.setViewportView(textArea);

        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        getContentPane().add(cons_scrollpane, BorderLayout.CENTER);

        cmdInput = new JTextField();
        cmdInput.addKeyListener(this);
        getContentPane().add(cmdInput, BorderLayout.SOUTH);
        cmdInput.setColumns(10);

        this.addWindowListener(this);
        this.setVisible(false);
        cmdInput.requestFocus();
    }

    public static void sendError(String line) {
        try {
            doc.insertString(doc.getLength(), line + "\n", errstyle);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void sendInfo(String line) {
        if (line.contains("-clc"))
            textArea.setText("");
        else {
            try {
                doc.insertString(doc.getLength(), line + "\n", infostyle);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            textArea.setCaretPosition(doc.getLength());
        }
    }


    public void activate() {
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        cmdInput.requestFocus();
    }

    public void bindFlows(BufferedWriter bw2, Process p) {
        processReference = p;
        bw = bw2;
    }

    public void setProcessRunning(boolean processRunning) {
        this.processRunning = processRunning;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                cmdInput.requestFocus();
                if (processRunning) {
                    // Si le proc tourne et que on appuie sur enter, on souhaite entrer une commande
                    try {
                        // On la sort dans le terminal et on l'envoie au programme
                        doc.insertString(doc.getLength(), cmdInput.getText() + "\n", inputstyle);
                        bw.write(cmdInput.getText() + "\n");
                        bw.flush();
                    } catch (BadLocationException | IOException e1) {
                        e1.printStackTrace();
                    }
                    // On reset l'input
                    cmdInput.setText("");
                    // Et on y replace le focus
                    cmdInput.requestFocus();
                } else if (!processRunning) {
                    // Si il est éteint, c'est qu'on appuie pour fermer la fenètre
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                }
                break;
            case KeyEvent.VK_ESCAPE:
                // On ferme la fenètre, le manager en sera notifi� et se videra
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                break;
        }
        if (e.getModifiers() == mask) {
            switch (e.getKeyCode()) {

                case KeyEvent.VK_C:
                    if (processRunning) {
                        try {
                            doc.insertString(doc.getLength(), "killing process ..." + "\n", errstyle);
                        } catch (BadLocationException ee) {
                            ee.printStackTrace();
                        }
                        processReference.destroy();

                    }
                    break;

            }
        }
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        this.setVisible(false);
        if (processRunning) processReference.destroy();
        textArea.setText("");
        this.dispose();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        cmdInput.requestFocus();
    }
    @Override
    public void windowDeactivated(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
}
