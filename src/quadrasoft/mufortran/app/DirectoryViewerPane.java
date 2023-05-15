package quadrasoft.mufortran.app;

import quadrasoft.mufortran.general.Session;
import quadrasoft.mufortran.display.ExplorerTreeCellRenderer;
import quadrasoft.mufortran.display.MyTreeModel;
import quadrasoft.mufortran.display.myExpensionListener;
import quadrasoft.mufortran.misc.PPMViewer;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class DirectoryViewerPane extends JPanel implements TreeSelectionListener, KeyListener, ActionListener {

    private static final long serialVersionUID = 1L;
    private static JTree myTree;
    private static DefaultTreeModel MyDefaultTree;
    private static JTextField workDirText;
    JButton openInExplorerbtn;
    JScrollPane dirsScrollPane;

    String actualPath = new String();
    JButton parcBtn;
    JButton parentBtn;

    public DirectoryViewerPane(String path) {
        initComponent(path);
    }

    static public void updateTree() {
        File test = new File(Session.getWorkDir());
        try {
            Session.setWorkDir(test.getCanonicalPath());    
        } catch (IOException e) {
            e.printStackTrace();
        }
        String workdir = Session.getWorkDir();
        if (!workdir.endsWith("/")) {
            workdir = workdir+"/";
        }
        //workDirText.setText(workdir);
        MyDefaultTree = new MyTreeModel(workdir);
        myTree.setModel(MyDefaultTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(parcBtn)) {
            JFileChooser chooser = new JFileChooser(workDirText.getText());
            chooser.removeChoosableFileFilter(chooser.getFileFilter());
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String selected_path = chooser.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "/");
                if (!selected_path.endsWith("/"))
                    selected_path += "/";
                    //selected_path = selected_path + "/";
                workDirText.setText(selected_path);
                Session.setWorkDir(selected_path);
                this.updateTree();
            }
        } else if (e.getSource().equals(parentBtn)) {
            File workingDir = new File(workDirText.getText());
            if (workingDir.exists()) {
                if (workingDir.getParentFile() != null) {
                    workDirText.setText(workingDir.getParentFile().getAbsolutePath().replaceAll("\\\\", "/"));
                    if (workingDir.getParentFile().getParentFile() != null)
                        workDirText.setText(workDirText.getText() + "/");
                }
            }
            Session.setWorkDir(workDirText.getText());
            this.updateTree();
        } else if (e.getSource().equals(openInExplorerbtn)) {
            File file = new File (Session.getWorkDir());
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public String getPath() {
        return actualPath;
    }

    public JTree getTreeReference() {
        return myTree;
    }

    private void initComponent(String path) {
        // Instantiation de la Class Jtree
        // On passe en paramètre un Model
        // MyTreeModel est une classe qui hérite de DefaultTreeModel

        MyDefaultTree = new MyTreeModel(path);
        myTree = new JTree(MyDefaultTree);
        // Ajout de l'arboressenece lorsqu'un répertoire est ouvert
        myTree.addTreeExpansionListener(new myExpensionListener());
        myTree.addTreeSelectionListener(this);
        // Nous employons un renderer spécial pour les éléments.
        myTree.setCellRenderer(new ExplorerTreeCellRenderer());
        // Instantiation de La Class JScrollPanne afin de pouvoir bénéficier des
        // ascenceurs.
        dirsScrollPane = new JScrollPane(myTree);
        // myTree.setRootVisible(false);
        myTree.setPreferredSize(new Dimension(250, 1600));
        this.setBorder(new EtchedBorder());
        this.setLayout(new BorderLayout(0, 0));
        this.add(dirsScrollPane);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        this.add(panel, BorderLayout.NORTH);


        workDirText = new JTextField();
        workDirText.setSize(new Dimension(200, 40));
        panel.add(workDirText, BorderLayout.WEST);
        //workDirText.setColumns(20);
        workDirText.addKeyListener(this);
        workDirText.setText(Session.getWorkDir());

        parcBtn = new JButton("...");
        parcBtn.setSize(new Dimension(20,20));
        parcBtn.addActionListener(this);
        panel.add(parcBtn, BorderLayout.CENTER);

        openInExplorerbtn = new JButton("E");
        openInExplorerbtn.setSize(20,20);
        openInExplorerbtn.addActionListener(this);
        panel.add(openInExplorerbtn, BorderLayout.EAST);

        parentBtn = new JButton("Parent");
        parentBtn.addActionListener(this);
        parentBtn.setSize(new Dimension(30,20));
        //panel.add(parentBtn, BorderLayout.EAST);

    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        String workDirTextContent = workDirText.getText();
        File wd_file = new File(workDirTextContent);
        if (wd_file.exists() && wd_file.isDirectory()) {
            Session.setWorkDir(workDirText.getText());
            updateTree();
        }

    }

    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
        if (myTree.getLastSelectedPathComponent() != null) {
            actualPath = ((File) ((DefaultMutableTreeNode) myTree.getLastSelectedPathComponent()).getUserObject())
                    .getAbsolutePath().replaceAll("\\\\", "/");

            if (actualPath.endsWith(".ppm") || actualPath.endsWith(".pgm")) {
                PPMViewer ppm_viewer = new PPMViewer(actualPath);
                myTree.setSelectionPath(null);

            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {}
    @Override
    public void keyTyped(KeyEvent arg0) {}
}
