package quadrasoft.mufortran.app;

import quadrasoft.mufortran.general.Log;
import quadrasoft.mufortran.general.Project;
import quadrasoft.mufortran.general.Session;
import quadrasoft.mufortran.display.ProjectTreeCellRenderer;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class ProjectTreePane extends JPanel implements TreeSelectionListener {

    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private JTree jtree;
    private JScrollPane jsp;

    private String actualPath = new String("");

    public ProjectTreePane() {

        root = new DefaultMutableTreeNode(new TreeFile("workspace", "icon.workspace"));
        model = new DefaultTreeModel(root);
        jtree = new JTree(model);
        jsp = new JScrollPane(jtree);

        jtree.setRootVisible(true);
        jtree.setPreferredSize(new Dimension(250, 800));
        jtree.addTreeSelectionListener(this);

        this.setLayout(new BorderLayout(0, 0));
        this.add(jsp);
    }

    public void add(String filename) {
        Session.addProject(new Project(filename));
    }

    public void close() {
        Session.getProjectsList().remove(Session.getActiveProject());
        this.rebuild();
    }

    private DefaultMutableTreeNode findChild(DefaultMutableTreeNode parent, Project project) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
            if (child.getUserObject() instanceof TreeFile) {

                return null;
            } else if (child.getUserObject() instanceof Project) {
                if (project.getName().equals(((Project) child.getUserObject()).getName()))
                    return child;
            }
        }
        return null;
    }

    private DefaultMutableTreeNode findChild(DefaultMutableTreeNode parent, TreeFile treeFile) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
            if (child.getUserObject() instanceof TreeFile) {
                if (treeFile.getName().equals(((TreeFile) child.getUserObject()).getName()))
                    return child;

            } else if (child.getUserObject() instanceof Project) {
                if (treeFile.getName().equals(((Project) child.getUserObject()).getName()))
                    return child;
            }
        }
        return null;
    }

    public String getPath() {
        return actualPath;
    }

    public JTree getTree() {
        return jtree;
    }

    public void rebuild() {
        root.removeAllChildren();

        for (Project proj : Session.getProjectsList()) {
            List<String> temp = proj.getSource();
            temp.add("changelog.txt");

            for (String project_path : temp) {

                DefaultMutableTreeNode currentParent = root;
                String[] pathComponents = (proj.getName() + "/" + project_path).split("/");

                for (String comp : pathComponents) {

                    DefaultMutableTreeNode child = findChild(currentParent, new TreeFile(comp, "icon.source"));
                    if (child == null) {
                        child = findChild(currentParent, proj);
                        if (child == null) {
                            if (currentParent.equals(root)) {
                                child = new DefaultMutableTreeNode(proj);
                            } else {
                                if (project_path.endsWith(comp)) {
                                    child = new DefaultMutableTreeNode(
                                            new TreeFile(comp, "icon.source", proj.getPath() + project_path));
                                } else {
                                    child = new DefaultMutableTreeNode(new TreeFile(comp, "icon.folder"));
                                }
                            }
                            currentParent.add(child);
                        }
                    }
                    currentParent = child;
                }
            }
            DefaultMutableTreeNode child = findChild(root, proj);
            if (child == null) {
                root.add(new DefaultMutableTreeNode(proj));
            }


        }

        model = new DefaultTreeModel(root);
        this.setLayout(new BorderLayout());
        //jtree = null;
        //jtree = new JTree(model);
        jtree.setModel(model);
        jtree.setCellRenderer(new ProjectTreeCellRenderer());
        jtree.addTreeSelectionListener(this);
        //jsp.setViewportView(jtree);

        this.add(jsp, BorderLayout.CENTER);
    }

    public void remove() {
        Session.getActiveProject().getSource()
                .remove(actualPath.substring(Session.getActiveProject().getPath().length(), actualPath.length()));
        Log.send(
                "removing:" + actualPath.substring(Session.getActiveProject().getPath().length(), actualPath.length()));

        try {
            Session.getActiveProject().save();
            rebuild();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeProjectSelection(Project prj) {
        actualPath = prj.getProjectFilePath();
        Session.switchActiveProject(prj);
    }
    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
        DefaultMutableTreeNode  last_selected = (DefaultMutableTreeNode) jtree.getLastSelectedPathComponent();
        if (last_selected != null)
            if (last_selected.getUserObject() instanceof Project) {
                Project selected_project = (Project) last_selected.getUserObject();
                changeProjectSelection(selected_project);
            } else if (last_selected.getUserObject() instanceof TreeFile) {
                TreeFile selected_treefile = (TreeFile) last_selected.getUserObject();
                if (selected_treefile.isFile()) {
                    actualPath = selected_treefile.getFilename();
                }
                // If the file is inside a project, select the project.
                if (last_selected.getParent() instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode selected_parent = (DefaultMutableTreeNode) last_selected.getParent();
                    if (selected_parent.getUserObject() instanceof Project) {
                        Project parent_project = (Project) selected_parent.getUserObject();
                        changeProjectSelection(parent_project);
                    }

                }
            }
        jtree.repaint();
    }

}
