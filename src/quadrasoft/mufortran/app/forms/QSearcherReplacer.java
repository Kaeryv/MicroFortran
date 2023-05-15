package quadrasoft.mufortran.app.forms;


import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;

public class QSearcherReplacer extends JFrame implements KeyListener{

    /**
     *
     */
    private static final long serialVersionUID = 7771114457322109765L;
    private final int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    JButton search;

    JButton replace;
    RSyntaxTextArea editor;
    JTextField text;
    JTextField replacement;

    public QSearcherReplacer(RSyntaxTextArea editor2) {
        super();
        this.setAlwaysOnTop(true);
        this.getContentPane().setLayout(new FlowLayout());
        //this.setSize(380, 200);
        this.setLocationRelativeTo(getParent());
        this.setTitle("Search & Replace");
        this.setDefaultCloseOperation(0);
        this.setIconImage(new ImageIcon(this.getClass().getResource("icons/find-icon.png")).getImage());

        // Editor reference
        editor = editor2;

        text = new JTextField();
        text.setColumns(16);
        text.setText(editor.getSelectedText());
        text.addKeyListener(this);
        replacement = new JTextField();
        replacement.addKeyListener(this);
        replacement.setColumns(16);
        replace = new JButton("Replace");
        search = new JButton("Search");
        this.getContentPane().add(text);
        this.getContentPane().add(search);
        this.getContentPane().add(replacement);
        this.getContentPane().add(replace);

        this.pack();



        replace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onReplace();
            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSearch();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        this.setVisible(true);
    }

    private void onSearch(){
        try {

            //if (!text.getText().equals(""))
                //editor.search(text.getText());
            search.setText("next");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void onReplace(){
        try {
            String s = text.getText();
            int l = s.length();
            String selected = editor.getSelectedText() == null ? "" : editor.getSelectedText();
            if (l != 0) {
                if (!selected.equals(s)) {
                    //editor.search(s);
                }
                selected = editor.getSelectedText() == null ? "" : editor.getSelectedText();
                if (selected.equals(s)) {
                    editor.replaceSelection(replacement.getText());
                    //editor.colorise(false);
                }
                //editor.search(s);
                search.setText("next");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void onClose(){
        removeAll();
        dispose();
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_TAB:
                if(replacement.hasFocus()){
                    text.requestFocus();
                }else{
                    replacement.requestFocus();
                }
                break;
            case KeyEvent.VK_ENTER:
                if(e.getSource().equals(text)){
                    onSearch();
                }else if(e.getSource().equals((replacement))){
                    onReplace();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                onClose();

                break;
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
}
