package quadrasoft.mufortran.app;

import quadrasoft.mufortran.app.editor.EditorFile;
import quadrasoft.mufortran.app.editor.EditorTab;
import quadrasoft.mufortran.app.forms.*;
import quadrasoft.mufortran.fortran.BinaryManager;
import quadrasoft.mufortran.general.CompilerLog;
import quadrasoft.mufortran.general.Log;
import quadrasoft.mufortran.general.Project;
import quadrasoft.mufortran.general.Session;
import quadrasoft.mufortran.resources.Resources;
import quadrasoft.mufortran.resources.Strings;
import quadrasoft.mufortran.display.MyFileView;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements MouseListener, ActionListener, WindowListener {
    private MyFileView fv = new MyFileView(null, null);
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu("File");
    private JMenu menuProject = new JMenu("Project");
    private JMenu newMenu = new JMenu("New");
    private JMenu menuOptions = new JMenu("Options");
    private JMenu menuHelp = new JMenu("Help");
    private JMenuItem newItem, newSourceItem, newEmptyItem, closeProjectItemP, buildOptionsItem, webItem, wikiItem, versionItem,
            remFileItemP, addFileItem, addFilesRecItem, closeProjectItem, apparenceItem, paramItem, openItem,
            importItem, addFilesRecItemP, addFileItemP;
    private ProjectPopup popupMenu = new ProjectPopup();
    private JDesktopPane mainBoard = new JDesktopPane();
    private JPanel botPane = new JPanel();
    private EditorTab workspace;
    private Log console = new Log();
    private CompilerLog console2 = new CompilerLog();
    private JTextPane errconsole = new JTextPane();
    private ProjectOptionMenu optn;

    private JInternalFrame editorFrame = new JInternalFrame("Editor", true, false, true, true);
    private JInternalFrame westFrame = new JInternalFrame("Files", true, false, true, true);
    private JInternalFrame consoleFrame = new JInternalFrame("Console", true, false, true, true);

    private JTabbedPane westTabs = new JTabbedPane();
    private JTabbedPane utilsPane = new JTabbedPane();
    private ProjectTreePane projectsTree;
    private DirectoryViewerPane fileTree;

    // TODO remove/rework
    private FileSelector selector;
    private List<String> candidats = new ArrayList<String>();
    private JPanel projecIndicatorP = new JPanel();
    private JLabel projecIndicatorLabelP = new JLabel(Strings.s("messages.no_selected_prj"));

    public MainFrame() {
        super(Strings.s("application.name"));
        initFrame();
        initComponents();
        finishFrame();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getModifiers() == Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_I:
                            if (workspace.editorPane.getSelectedIndex() != -1) {
                                ((EditorFile) workspace.editorPane.getSelectedComponent()).getEditor().requestFocus();
                            } else {
                                workspace.requestFocus();
                            }

                            break;
                        case KeyEvent.VK_COLON:
                            Log.requestTerminalFocus();
                            break;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource().equals(openItem)) {
            callOpenAnything();
        } else if (arg0.getSource().equals(newItem)) {
            final ProjectCreatorFrame projAssit = new ProjectCreatorFrame();
            projAssit.setLocationRelativeTo(null);
            projAssit.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent we) {
                    if (Session.getActiveProject() != null)
                        projAssit.setVisible(false);
                    if (projAssit.isConcluded()) {
                        openProject(Session.getActiveProject().getProjectFilePath());
                    }
                    projAssit.dispose();
                }
            });
        } else if (arg0.getSource().equals(newSourceItem)) {
            final SourceCreatorFrame fileAssit = new SourceCreatorFrame();
            fileAssit.setLocationRelativeTo(null);
            fileAssit.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent we) {
                    if (fileAssit.isConcluded()) {

                        if (fileAssit.isAddThisToProject()) {
                            Session.getActiveProject().getSource().add(fileAssit.getPath());

                            try {
                                Session.getActiveProject().save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        updateTrees();
                        workspace.open(fileAssit.getPath());
                    }
                    fileAssit.dispose();
                }
            });
        } else if (arg0.getSource().equals(newEmptyItem)) {
            workspace.newFile();
        } else if (arg0.getSource().equals(closeProjectItem) || arg0.getSource().equals(closeProjectItemP)) {
            projectsTree.close();
            projectsTree.getTree().addMouseListener(new PopupListener());

        } else if ((arg0.getSource().equals(addFileItem) || arg0.getSource().equals(addFileItemP))
                && Session.getActiveProject() != null) {
            String filename;
            String directory = Session.getActiveProject().getPath();
            JFileChooser chooser = new JFileChooser(directory);
            chooser.setFileView(fv);
            chooser.removeChoosableFileFilter(chooser.getFileFilter());
            FileFilter filter = new FileNameExtensionFilter("Source files", "f90", "f", "f95");
            chooser.addChoosableFileFilter(filter);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                if (chooser.getSelectedFile().getPath().replaceAll("\\\\", "/").contains(directory)) {
                    filename = chooser.getSelectedFile().getPath()
                            .substring(directory.length(), chooser.getSelectedFile().getPath().length())
                            .replaceAll("\\\\", "/");

                    Session.getActiveProject().getSource().add(filename);
                    projectsTree.rebuild();
                    projectsTree.getTree().addMouseListener(this);
                    projectsTree.getTree().addMouseListener(new PopupListener());
                } else {
                    filename = chooser.getSelectedFile().getPath().replaceAll("\\\\", "/");
                    System.out.println(filename);
                    String filecrop = filename.substring(filename.lastIndexOf("/"));
                    File newSource = new File(filename);
                    File arrival = new File(directory + filecrop);
                    try {
                        Files.copy(newSource.toPath(), arrival.toPath());
                        Session.getActiveProject().getSource().add(filecrop.substring(1));
                        projectsTree.rebuild();
                        projectsTree.getTree().addMouseListener(this);
                        projectsTree.getTree().addMouseListener(new PopupListener());
                    } catch (IOException exception) {
                        Log.send("Error: Unable to copy the file to project folder, maybe a file with the same name already exists :");
                        exception.printStackTrace();
                    }
                }
            }
            try {
                Session.getActiveProject().save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ((arg0.getSource().equals(addFilesRecItem) || arg0.getSource().equals(addFilesRecItemP))
                && Session.getActiveProject() != null) {
            String directory = Session.getActiveProject().getPath();
            JFileChooser chooser = new JFileChooser(directory);
            chooser.setFileView(fv);
            chooser.removeChoosableFileFilter(chooser.getFileFilter());
            FileFilter filter = new FileNameExtensionFilter("Source files", "f90", "f", "f95");
            chooser.addChoosableFileFilter(filter);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                if ((chooser.getSelectedFile().getPath().replaceAll("\\\\", "/") + "/").contains(directory)) {

                    loadFolder(chooser.getSelectedFile().getPath().replaceAll("\\\\", "/"));
                    selector = new FileSelector(candidats);
                    selector.setLocationRelativeTo(null);
                    selector.addWindowListener(this);
                }
            }
            try {
                Session.getActiveProject().save();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }

        } else if (arg0.getSource().equals(apparenceItem)) {
            final GeneralOptionMenu optionMenu = new GeneralOptionMenu();
            optionMenu.setLocationRelativeTo(null);
            optionMenu.theme();
            optionMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    Session.save();
                    optionMenu.dispose();
                }

            });
        } else if (arg0.getSource().equals(remFileItemP) && Session.getActiveProject() != null) {
            projectsTree.remove();
            projectsTree.getTree().addMouseListener(this);
            projectsTree.getTree().addMouseListener(new PopupListener());
        } else if (arg0.getSource().equals(buildOptionsItem) && Session.getActiveProject() != null) {
            if (Session.getActiveProject() != null)
                optn = new ProjectOptionMenu(Session.getActiveProject());
            optn.setLocationRelativeTo(null);
        } else if (arg0.getSource().equals(importItem)) {

            final ProjectImporterFrame importFrame = new ProjectImporterFrame();
            importFrame.setLocationRelativeTo(null);
            importFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent we) {
                    if (Session.getActiveProject() != null)
                        importFrame.setVisible(false);
                    if (importFrame.isConcluded()) { // if we went to the end of
                        // the import process
                        openProject(Session.getActiveProject().getProjectFilePath());
                    }
                    importFrame.dispose();
                }
            });
        } else if (arg0.getSource().equals(paramItem)) {
            final GeneralOptionMenu optionMenu = new GeneralOptionMenu();
            optionMenu.setLocationRelativeTo(null);
            optionMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent we) {
                    Session.save();
                    optionMenu.dispose();
                }
            });
        } else if (arg0.getSource().equals(wikiItem)) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("http://www.fortran90.org/"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        } else if (arg0.getSource().equals(versionItem)) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/Kaeryv/muFortran/releases"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        } else if (arg0.getSource().equals(webItem)) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/Kaeryv"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void callOpenAnything() {
        String filename;
        String directory = Session.getWorkDir();
        JFileChooser chooser = new JFileChooser(directory);
        chooser.setFileView(fv);
        chooser.removeChoosableFileFilter(chooser.getFileFilter());
        FileFilter filter = new FileNameExtensionFilter("All Handled file formats", Strings.s("application.project_extension").replace(".",""), "f90", "f", "f95", "F",
                "F90");
        chooser.addChoosableFileFilter(filter);
        filter = new FileNameExtensionFilter("Project files", Strings.s("application.project_extension").replace(".",""));

        chooser.addChoosableFileFilter(filter);
        filter = new FileNameExtensionFilter("Source files", "f90", "f", "f95", "F", "F90");
        chooser.addChoosableFileFilter(filter);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            filename = chooser.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "/");
            open(filename);
        }
    }

    public void finishFrame() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                workspace.saveAll();
                e.getWindow().dispose();
                Session.save();
                System.exit(0);
            }
        });
        this.pack();
        //this.setLocationRelativeTo(null);
        this.setLocationByPlatform(true);
        this.setVisible(true);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_OPENED));
    }

    public void initComponents() {
        addFileItem = new JMenuItem("Add existing file ...", Resources.getImageResource("icon.add"));
        addFilesRecItem = new JMenuItem("Add multiple files ...", Resources.getImageResource("icon.addmore"));
        closeProjectItem = new JMenuItem("close project", Resources.getImageResource("icon.close"));
        buildOptionsItem = new JMenuItem("Properties", Resources.getImageResource("icon.param"));

        newMenu.setIcon(Resources.getImageResource("icon.emptyfile"));
        newItem = new JMenuItem("Project", Resources.getImageResource("icon.projfile"));
        newSourceItem = new JMenuItem("Source", Resources.getImageResource("icon.sourcefile"));
        newEmptyItem = new JMenuItem("Empty", Resources.getImageResource("icon.emptyfile"));
        openItem = new JMenuItem("Open", Resources.getImageResource("icon.folder"));
        importItem = new JMenuItem("Import...", Resources.getImageResource("icon.import"));

        apparenceItem = new JMenuItem("Theme", Resources.getImageResource("icon.screen"));
        paramItem = new JMenuItem("Parameters", Resources.getImageResource("icon.settings"));

        webItem = new JMenuItem("Dev's website");
        wikiItem = new JMenuItem("Wiki");
        versionItem = new JMenuItem("Version info");

        // TODO check
        addFilesRecItemP = new JMenuItem("Add files recursively ...", Resources.getImageResource("icon.addmore"));
        closeProjectItemP = new JMenuItem("Close project", Resources.getImageResource("icon.close"));
        remFileItemP = new JMenuItem("Remove file from project", Resources.getImageResource("icon.delete"));
        addFileItemP = new JMenuItem("Add file", Resources.getImageResource("icon.add"));

        this.setIconImage((Resources.getImageResource("icon.mainicon").getImage()));

		/*
         * Events
		 */
        closeProjectItem.addActionListener(this);
        addFileItem.addActionListener(this);
        addFileItemP.addActionListener(this);
        addFilesRecItem.addActionListener(this);
        addFilesRecItemP.addActionListener(this);
        closeProjectItem.addActionListener(this);
        closeProjectItemP.addActionListener(this);
        remFileItemP.addActionListener(this);
        newEmptyItem.addActionListener(this);
        newSourceItem.addActionListener(this);
        apparenceItem.addActionListener(this);
        paramItem.addActionListener(this);

        popupMenu.add(projecIndicatorP);
        projecIndicatorP.add(projecIndicatorLabelP);
        popupMenu.add(addFileItemP);
        popupMenu.add(addFilesRecItemP);
        popupMenu.add(closeProjectItemP);
        popupMenu.add(remFileItemP);

        workspace = new EditorTab();
        workspace.setBorder(BorderFactory.createBevelBorder(0));

        fileTree = new DirectoryViewerPane(Session.getWorkDir());
        projectsTree = new ProjectTreePane();
        projectsTree.getTree().addMouseListener(new PopupListener());

        JLabel verText = new JLabel(Session.getVersion());
        JLabel quadraText = new JLabel("Quadrasoft : : . :  .");
        JLabel forText = new JLabel("");
        verText.setForeground(Color.WHITE);
        quadraText.setForeground(Color.WHITE);
        forText.setForeground(Color.WHITE);

        CompilerLog.getList().addMouseListener(this);
        botPane.setLayout(new BorderLayout());
        botPane.setBackground(Color.DARK_GRAY);
        botPane.add(verText, BorderLayout.EAST);
        botPane.add(quadraText, BorderLayout.WEST);
        botPane.add(forText, BorderLayout.CENTER);

        openItem.addActionListener(this);
        newItem.addActionListener(this);
        projectsTree.getTree().addMouseListener(this);
        fileTree.getTreeReference().addMouseListener(this);
        importItem.addActionListener(this);
        buildOptionsItem.addActionListener(this);

        wikiItem.addActionListener(this);
        versionItem.addActionListener(this);
        webItem.addActionListener(this);

        menuFile.add(newMenu);
        newMenu.add(newItem);
        newMenu.add(newSourceItem);
        newMenu.add(newEmptyItem);
        menuFile.add(openItem);
        menuFile.add(importItem);
        menuProject.add(addFileItem);
        menuProject.add(addFilesRecItem);
        menuProject.add(closeProjectItem);
        menuProject.addSeparator();
        menuProject.add(buildOptionsItem);

        menuOptions.add(apparenceItem);
        menuOptions.add(paramItem);

        menuHelp.add(wikiItem);
        menuHelp.add(versionItem);
        menuHelp.add(webItem);

        menuBar.add(menuFile);
        menuBar.add(menuProject);
        menuBar.add(menuOptions);
        menuBar.add(menuHelp);

        errconsole.setPreferredSize(new Dimension(800, 100));
        errconsole.setEditable(false);

        utilsPane.addTab(Strings.s("application.name"), console);
        utilsPane.addTab("Compiler output", console2);

        westTabs.addTab("Project Tree", projectsTree);
        westTabs.addTab("Directory", fileTree);
        westFrame.getContentPane().add(westTabs, BorderLayout.CENTER);
        westFrame.setVisible(true);
        editorFrame.getContentPane().setLayout(new BorderLayout(0, 0));
        editorFrame.getContentPane().add(workspace);
        editorFrame.setVisible(true);
        editorFrame.setFrameIcon(Resources.getImageResource("icon.editor"));
        editorFrame.setBounds(10, 10, 100, 100);


        editorFrame.setTitle("Editor");
        consoleFrame.setTitle("Utilities");
        consoleFrame.setFrameIcon(Resources.getImageResource("icon.gamepad"));
        // consoleFrame.setPreferredSize(new Dimension(600, 200));
        westFrame.setTitle("Overview");
        westFrame.setFrameIcon(Resources.getImageResource("icon.folder"));
        consoleFrame.getContentPane().add(utilsPane);
        consoleFrame.setVisible(true);
        // consoleFrame.setFrameIcon(Resources.getImageResource("icon.console"));


        // Now building the frame
        Dimension desk_Size = new Dimension(1200,700);

        editorFrame.setPreferredSize(new Dimension((int) ((desk_Size.width * 4) / 5.0), (int) (desk_Size.height*4/5.0)));
        editorFrame.setLocation((int) (desk_Size.width/5.0), 0);
        editorFrame.putClientProperty("dragMode", "fixed");
        westFrame.setLocation(0,0);
        westFrame.setPreferredSize(new Dimension((int) ((desk_Size.width) / 5.0), (desk_Size.height)));
        westFrame.putClientProperty("dragMode", "fixed");
        consoleFrame.setLocation((int) ((desk_Size.width) / 5.0),(int) ((desk_Size.height * 4) / 5.0));
        consoleFrame.setPreferredSize(new Dimension((int) ((desk_Size.width*4) / 5.0), (int) ((desk_Size.height)/5.0)));
        consoleFrame.putClientProperty("dragMode", "fixed");

        mainBoard.setDesktopManager( new NoDragDesktopManager() );
        editorFrame.pack();
        consoleFrame.pack();
        westFrame.pack();

        botPane.setPreferredSize(new Dimension(desk_Size.width,20));
        menuBar.setPreferredSize(new Dimension(desk_Size.width,25));

        mainBoard.setPreferredSize(desk_Size);
        mainBoard.add(editorFrame);
        mainBoard.add(consoleFrame);
        mainBoard.add(westFrame);
        this.add(menuBar, BorderLayout.NORTH);
        this.add(botPane, BorderLayout.SOUTH);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.

                Dimension desk_Size = e.getComponent().getSize();
                desk_Size.height = desk_Size.height-80;

                editorFrame.setPreferredSize(new Dimension((int) ((desk_Size.width * 4) / 5.0), (int) (desk_Size.height*3.6/5.0)));
                editorFrame.setLocation((int) (desk_Size.width/5.0), 0);
                westFrame.setLocation(0,0);
                westFrame.setPreferredSize(new Dimension((int) ((desk_Size.width) / 5.0), (desk_Size.height)));
                consoleFrame.setLocation((int) ((desk_Size.width) / 5.0),(int) ((desk_Size.height * 3.6) / 5.0));
                consoleFrame.setPreferredSize(new Dimension((int) ((desk_Size.width*4) / 5.0), (int) ((desk_Size.height*1.4)/5.0)));
                if(editorFrame.isMaximum()){
                    try {
                        editorFrame.setIcon(true);
                    } catch (PropertyVetoException e1) {
                        e1.printStackTrace();
                    }
                    editorFrame.setPreferredSize(desk_Size);
                    editorFrame.setLocation(0, 0);
                }
                if(consoleFrame.isMaximum()){
                    try {
                        consoleFrame.setIcon(true);
                    } catch (PropertyVetoException e1) {
                        e1.printStackTrace();
                    }
                    consoleFrame.setPreferredSize(desk_Size);
                    consoleFrame.setLocation(0, 0);
                }

                editorFrame.pack();
                consoleFrame.pack();
                westFrame.pack();
            }
        });

        getContentPane().add(mainBoard, BorderLayout.CENTER);

    }

    public void initFrame() {
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void loadFolder(String folderPath) {
        candidats = new ArrayList<String>();
        for (File file : new File(folderPath).listFiles()) {
            if (!file.isDirectory()) {
                String path = file.getAbsolutePath().replaceAll("\\\\", "/");
                ;
                if (path.contains(".f")) {
                    if (!candidats.contains(path.substring(Session.getActiveProject().getPath().length()))
                            && !Session.getActiveProject().getSource()
                            .contains(path.substring(Session.getActiveProject().getPath().length())))
                        candidats.add(path.substring(Session.getActiveProject().getPath().length()));
                }
            } else {
                String path = file.getAbsolutePath();
                loadFolder(path);
            }

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == projectsTree.getTree()) {
            if (e.getClickCount() == 2) {
                Object targetObject = ((DefaultMutableTreeNode) projectsTree.getTree()
                        .getClosestPathForLocation(e.getX(), e.getY()).getLastPathComponent()).getUserObject();
                if (targetObject instanceof TreeFile) {
                    workspace.open(((TreeFile) targetObject).getFilename());
                }
            }
        } else if (e.getSource() == fileTree.getTreeReference()) {
            if (e.getClickCount() == 2) {
                String path = fileTree.getPath();
                if (path != null) {
                    if (path.contains(".f") || path.contains(".F") || path.endsWith(".dat") || path.endsWith(".DAT")
                            || path.endsWith(".log") || path.endsWith(".mod") || path.endsWith(".txt")
                            || path.endsWith(".info")) {
                        workspace.open(path);
                    } else if (path.contains(Strings.s("application.project_extension"))) {
                        this.openProject(path);
                    } else if (path.contains(".ps")) {
                        // new PsReader(path);
                    } else if (path.contains(".exe")) {
                        BinaryManager.launchFile(path);
                    }
                }
            }
        }
        if (e.getSource().equals(CompilerLog.getList())) {
            int index = CompilerLog.getList().getSelectedIndex();
            if (e.getClickCount() == 2) {
                String temp = CompilerLog.getList().getSelectedValue();
                if (temp.contains(".f") || temp.contains(".F")) {
                    String filePath = /* Session.getActiveProject().getPath() + */temp.substring(0,
                            temp.substring(0, temp.lastIndexOf(":")).lastIndexOf(":"));
                    System.out.println(filePath);
                    filePath = filePath.substring(0, filePath.lastIndexOf(":"));
                    /*
					 * if(new File(filePath+".f90").exists()) workspace.open(filePath+".f90"); else
					 * if(new File(filePath+".f").exists()) workspace.open(filePath+".f"); else
					 * if(new File(filePath+".F").exists()) workspace.open(filePath+".F"); else
					 * if(new File(filePath+".f95").exists()) workspace.open(filePath+".f95"); else
					 * if(new File(filePath+".F90").exists()) workspace.open(filePath+".F90");
					 */
                    workspace.open(filePath);
                    String[] coords = temp.split(":");
                    int length = coords.length;
                    int line_no = Integer.parseInt(coords[length-2]);
                    int coln_no = Integer.parseInt(coords[length-1]);
                    workspace.searchLineInActiveFile(line_no, coln_no);
                }

            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {

    }

    @Override
    public void mousePressed(MouseEvent arg0) {

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }

    public void open(String filename) {
        if (filename.substring(filename.lastIndexOf(".")).equals(Strings.s("application.project_extension"))) {
            openProject(filename);
        } else
            workspace.open(filename);
    }

    public void openProject(String filename) {
        projectsTree.add(filename);
        projectsTree.rebuild();
        projectsTree.getTree().addMouseListener(this);
        projectsTree.getTree().addMouseListener(new PopupListener());
        Session.addRecent(filename);
        westTabs.setSelectedIndex(0); // On passe au projectTree
        Log.send("Opening project: \"" + filename + "\"");
    }

    public void updateTrees() {
        projectsTree.rebuild();
        projectsTree.getTree().addMouseListener(this);
        projectsTree.getTree().addMouseListener(new PopupListener());
    }

    @Override
    public void windowActivated(WindowEvent arg0) {

    }

    @Override
    public void windowClosed(WindowEvent arg0) {

    }

    @Override
    public void windowClosing(WindowEvent arg0) {

        if (selector.isConcluded()) {
            candidats = selector.getSelectedFiles();
        } else {
            candidats.removeAll(candidats);
        }
        for (String file1 : candidats) {

            String path = file1.replaceAll("\\\\", "/");
            if (path.contains(".f")) {
                Session.getActiveProject().getSource().add(path);

            }
        }
        projectsTree.rebuild();
        projectsTree.getTree().addMouseListener(this);
        projectsTree.getTree().addMouseListener(new PopupListener());
        try {
            Session.getActiveProject().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {

    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {

    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    class PopupListener extends MouseAdapter {
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int row = projectsTree.getTree().getClosestRowForLocation(e.getX(), e.getY());
                projectsTree.getTree().setSelectionRow(row);

                if (((DefaultMutableTreeNode) projectsTree.getTree().getClosestPathForLocation(e.getX(), e.getY())
                        .getLastPathComponent()).getUserObject() instanceof Project) {
                    remFileItemP.setEnabled(false);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());

                } else if (!((TreeFile) ((DefaultMutableTreeNode) projectsTree.getTree()
                        .getClosestPathForLocation(e.getX(), e.getY()).getLastPathComponent()).getUserObject())
                        .getName().equalsIgnoreCase("workspace")) {

                    remFileItemP.setEnabled(true);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
    }

    private class ProjectPopup extends JPopupMenu {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public ProjectPopup() {
            int test = 0;
            switch (test) {
                case 0:
                    break;
            }
        }

        @Override
        public void show(Component component, int x, int y) {
            if (((DefaultMutableTreeNode) projectsTree.getTree().getClosestPathForLocation(x, y).getLastPathComponent())
                    .getUserObject() instanceof Project) {
                projecIndicatorLabelP.setText("<html><b>" + Session.getActiveProject().getName() + "</b>");
                remFileItemP.setEnabled(false);
                addFileItemP.setEnabled(true);
                closeProjectItemP.setEnabled(true);

                addFilesRecItemP.setEnabled(true);
            } else {
                projecIndicatorLabelP.setText("<html><b>" + ((TreeFile) ((DefaultMutableTreeNode) projectsTree.getTree()
                        .getClosestPathForLocation(x, y).getLastPathComponent()).getUserObject()).getName() + "</b>");
                addFileItemP.setEnabled(false);
                addFilesRecItemP.setEnabled(false);
                closeProjectItemP.setEnabled(false);
            }
            super.show(component, x, y);

        }

    }
    class NoDragDesktopManager extends DefaultDesktopManager
    {
        public void beginDraggingFrame(JComponent f)
        {
            if (!"fixed".equals(f.getClientProperty("dragMode")))
                super.beginDraggingFrame(f);
        }

        public void dragFrame(JComponent f, int newX, int newY)
        {
            if (!"fixed".equals(f.getClientProperty("dragMode")))
                super.dragFrame(f, newX, newY);
        }

        public void endDraggingFrame(JComponent f)
        {
            if (!"fixed".equals(f.getClientProperty("dragMode")))
                super.endDraggingFrame(f);
        }
    }
}
