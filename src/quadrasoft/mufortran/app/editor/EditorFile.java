package quadrasoft.mufortran.app.editor;

import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import quadrasoft.mufortran.app.forms.QSearcherReplacer;
import quadrasoft.mufortran.general.Log;
import quadrasoft.mufortran.general.Session;
import quadrasoft.mufortran.resources.Resources;
import quadrasoft.mufortran.resources.Strings;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EditorFile extends JPanel implements KeyListener, DocumentListener {

    private static int newFilesCount;
    private final int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    private final RSyntaxTextArea editor = new RSyntaxTextArea();
    boolean edited = false;

    public RSyntaxTextArea getEditor() {
        return editor;
    }
    private String path = "";
    private int editCount = 0;
    private boolean needReopen = false;

    public void reset_font(){
        int fontsize = Integer.parseInt(Session.parameter("FontSize"));
        RSyntaxTextArea rsta = (RSyntaxTextArea)editor;
        SyntaxScheme scheme = rsta.getSyntaxScheme();
        int count = scheme.getStyleCount();
        for (int i=0; i<count; i++) {
            Style ss = scheme.getStyle(i);
            if (ss!=null) {
                Font font = ss.font;
                ss.font = new Font("Fira Sans", Font.PLAIN, fontsize);
            }
        }
        rsta.setFont(new Font("Fira Sans", Font.PLAIN, fontsize));
    }
    public EditorFile(String filename) {
        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_FORTRAN);
        editor.setTabSize(4);
        editor.setPaintTabLines(true);
        editor.setTabLineColor(Color.ORANGE);
        editor.setCaretPosition(0);
        editor.setTabsEmulated(true);
        editor.setMargin(new Insets(20, 10, 10, 10));
        //editor.addHyperlinkListener(this);
        editor.requestFocusInWindow();
        editor.setMarkOccurrences(true);
        //editor.setCodeFoldingEnabled(true);
        editor.setClearWhitespaceLinesEnabled(false);
        editor.setAntiAliasingEnabled(true);
        Resources.theme.apply(editor);
        InputMap im = editor.getInputMap();
        ActionMap am = editor.getActionMap();
        //im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "decreaseFontSize");
        //am.put("decreaseFontSize", new RSyntaxTextAreaEditorKit.DecreaseFontSizeAction());
        //im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "increaseFontSize");
        //am.put("increaseFontSize", new RSyntaxTextAreaEditorKit.IncreaseFontSizeAction());
        reset_font();

        RTextScrollPane scrollPane = new RTextScrollPane(editor, true);
        scrollPane.setLineNumbersEnabled(true);
        Gutter gutter = scrollPane.getGutter();
        gutter.setBookmarkingEnabled(false);
        if (!filename.equals("")) {
            Path filepath = Paths.get(filename);
            this.setPath(filename);
            this.setName(filename.substring(filename.lastIndexOf("/") + 1));
            // Loading data from file
            File file = new File(filename);
            try {
                String content = Files.readString(filepath);
                editor.setText(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setNewFilesCount(getNewFilesCount() + 1);
            this.setName("untitled " + EditorFile.getNewFilesCount());
        }
        editor.discardAllEdits();
        System.out.println("File loaded");


        editor.addKeyListener(this);
        editor.getDocument().addDocumentListener(this);
        //editor.setBorder(BorderFactory.createEtchedBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        setEdited(false);
    }

    private static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    public static int getNewFilesCount() {
        return newFilesCount;
    }

    public static void setNewFilesCount(int newFilesCount) {
        EditorFile.newFilesCount = newFilesCount;
    }

    private void backup() {
        String storagePlace = this.getPath() + ".bak";
        Log.send(Strings.s("messages.autosaving") + " " + this.getName());

        if (!new File(storagePlace).exists()) {
            try {
                boolean file = new File(storagePlace).createNewFile();
            } catch (IOException e) {
                Log.send(Strings.s("messages.create_backup_error"));
                e.printStackTrace();
            }
        }
        save(storagePlace);
        editor.requestFocus();
    }

    public boolean callSave() {
        /*
		 * This function is called to check if the document has unsaved change, if it
		 * does, the functions asks the user to save.
		 */
        if (isEdited()) {
            Log.send(Strings.s("terminal.modified_file_closing"));
            String tellThemTheName;
            if (!getPath().equals(""))
                tellThemTheName = "The active document " + getPath().substring(getPath().lastIndexOf("/") + 1);
            else
                tellThemTheName = "A new file";
            int jp = JOptionPane.showConfirmDialog(new JFrame(),
                    tellThemTheName + " has been modified.\nDo you want save it?");
            if (jp == JOptionPane.OK_OPTION) {
                save();
                return true;
            } else if (jp == JOptionPane.CANCEL_OPTION) {
                return false;
            } else if (jp == JOptionPane.NO_OPTION) {
                return true;
            } else if (jp == JOptionPane.CLOSED_OPTION) {
                return false;
            }
            return true;
        }
        return true;
    }

    public boolean canRedo() {
        return editor.canRedo();
    }

    public boolean canUndo() {
        return editor.canUndo();
    }


    public String getPath() {
        return path;
    }

    public void setPath(String var_1) {
        path = var_1;
    }


    public boolean isEdited() {
        return edited;

    }

    public void setEdited(boolean value) {
        edited = value;
    }

    public boolean isNeedReopen() {
        return needReopen;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getModifiers() == mask) {
            switch (e.getKeyCode()) {
                /*case KeyEvent.VK_Z:
                    if (this.canUndo())
                        undo();
                    break;
                case KeyEvent.VK_Y:
                    if (this.canRedo())
                        redo();
                    break;

                 */
                case KeyEvent.VK_S:
                    this.save();
                    break;
                case KeyEvent.VK_F:
                    //new QSearcherReplacer(this.editor);
                    //this.editor.requestFocus();
                    break;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent arg0) {

    }



    public void redo() {
        editor.redoLastAction();
    }


    void save() {
		/*
		 * This function is the default save option, the file is saved at it's actual
		 * path. If the file has no proper path, the function calls saveAs()
		 */
        Log.send("Saving : \"" + getPath() + "\"");
        if (!getPath().equals("")) {
            save(getPath());
        } else
            saveAs();
    }

    public void save(String filename) {
        try {
            String text = editor.getText();
            text = text.replaceAll("\\n", "\r\n");
            // Write file content
            File file = new File(filename);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.close();
            // File is set as unedited
            setEdited(false);
            // Backup file is not needed anymore
            if (new File(this.getPath() + ".bak").exists() && !filename.contains(".bak")) {
                new File(this.getPath() + ".bak").delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAs() {
		/*
		 * Saves a document opened in �Fort in a new location. The user is prompted a
		 * location on filesystem.
		 */
        // We propose the project folder.
        String directory = Session.getWorkDir();
        JFileChooser chooser = new JFileChooser(directory);
        chooser.removeChoosableFileFilter(chooser.getFileFilter());
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            // We instantly get rid of nasty windows-only filesystem
            setPath(chooser.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "/"));
            save(getPath());
            // We re-open the file from the new path.
            needReopen = true;
        }

    }

    public void searchLine(String strLine, String text) {
        SearchContext context = new SearchContext();
        if (text.length() == 0) {
            return;
        }
        context.setSearchFor(text.split("|")[1]);
        context.setSearchForward(true);
        context.setWholeWord(false);
        SearchEngine.find(editor, context);
        //editor.search(word, Integer.parseInt(strLine));
        editor.requestFocus();
    }

    public void moveCursor(int line, int column) {
        try {
            int offset = editor.getLineStartOffset(line-1) + column;
            editor.setCaretPosition(offset);
            editor.requestFocus();
        } catch (BadLocationException e) {
            // Ignore
        }

    }

    public void undo() {
        editor.undoLastAction();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {

    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        setEdited(true);
    }
}
