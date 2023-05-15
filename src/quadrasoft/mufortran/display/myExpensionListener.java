package quadrasoft.mufortran.display;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class myExpensionListener implements TreeExpansionListener {

    public myExpensionListener() {
        super();
        // TODO Auto-generated constructor stub
    }

    // on n'effectue aucune action lorsque qu'un r�pertoire est ferm�
    @Override
    public void treeCollapsed(TreeExpansionEvent arg0) {

    }

    // S'ex�cute lorsque lorsque qu'un dossier est ouvert
    @Override
    public void treeExpanded(TreeExpansionEvent arg0) {
        // Appele de la fonction addChildren de la classe TreeUtil
        // Elle demande en param�tre DefaultTreeModel, DefaultMutableTreeNode
        TreeUtil.addChildren((DefaultTreeModel) ((JTree) arg0.getSource()).getModel(),
                (DefaultMutableTreeNode) arg0.getPath().getLastPathComponent());
    }

}
