package quadrasoft.mufortran.app.forms;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class QSearcher extends JFrame implements KeyListener{
    private final int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    /**
     *
     */
    private static final long serialVersionUID = 611335538466235667L;

    JButton search;

    RSyntaxTextArea editor;
    private JTextField textField;

    public QSearcher(RSyntaxTextArea editor2) {
        super();
        this.addKeyListener(this);
        editor = editor2;
        setTitle("Search");
        setDefaultCloseOperation(0);
        setIconImage(new ImageIcon(this.getClass().getResource("icons/find-icon.png")).getImage());
        setSize(277, 147);
        setLocationRelativeTo(getParent());

        JPanel pan = new JPanel();
        this.getContentPane().add(pan);
        pan.setLayout(null);

        JLabel label_1 = new JLabel("");
        label_1.setBounds(212, 271, 287, 129);
        pan.add(label_1);

        search = new JButton("Search");
        search.setBounds(140, 65, 99, 26);
        pan.add(search);

        JLabel label_2 = new JLabel("");
        label_2.setBounds(0, 258, 287, 129);
        pan.add(label_2);

        textField = new JTextField();
        textField.setBounds(20, 34, 219, 20);
        pan.add(textField);
        textField.setColumns(10);
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!textField.getText(0, textField.getText().length()).equals("")) {
                        //editor.search(textField.getText(0, textField.getText().length()));
                        search.setText("next");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                removeAll();
                dispose();
            }
        });

        this.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getModifiers() == mask) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_TAB:
                    if(search.hasFocus()){
                        textField.requestFocus();
                    }else{
                        search.requestFocus();
                    }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}