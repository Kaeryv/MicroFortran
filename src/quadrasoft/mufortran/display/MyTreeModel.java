package quadrasoft.mufortran.display;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

public class MyTreeModel extends DefaultTreeModel {

    private static final long serialVersionUID = 1L;

    public MyTreeModel(String path) {

        super(new DefaultMutableTreeNode(new MyFile(path)));
        TreeUtil.addChildren(this, (DefaultMutableTreeNode) getRoot());
    }

    @Override
    public boolean isLeaf(Object arg0) {
        return !((File) ((DefaultMutableTreeNode) arg0).getUserObject()).isDirectory();

    }
}
