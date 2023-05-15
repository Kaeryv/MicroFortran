package quadrasoft.mufortran.gui;

import javax.swing.event.TreeSelectionListener;

public abstract class myTreeSelectionListener implements TreeSelectionListener {

    public myTreeSelectionListener() {
        super();
    }

	/*
     * public void valueChanged(TreeSelectionEvent arg0) {
	 * 
	 * DefaultMutableTreeNode f = (DefaultMutableTreeNode)
	 * arg0.getPath().getLastPathComponent(); File ff = (File) f.getUserObject();
	 * 
	 * DatFileTree mainWindow = (DatFileTree)arg0.getSource();
	 * 
	 * if(!ff.isDirectory()){ //action si ca n'est pas un r�pertoire }else{ //action
	 * si c'est un r�pertoire }
	 * 
	 * System.out.println(ff.isDirectory());
	 * System.out.println(ff.getAbsolutePath());
	 * 
	 * }
	 * 
	 */

}