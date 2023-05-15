package quadrasoft.mufortran.display;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import java.io.File;
import java.util.Hashtable;
import quadrasoft.mufortran.resources.Resources;

public class MyFileView extends FileView
{
    Hashtable<String,ImageIcon> table;
    ImageIcon dirIcon;

    public MyFileView(Hashtable<String,ImageIcon> arg0,ImageIcon arg1)
    {
        this.table = new Hashtable<>();
        this.dirIcon = Resources.getImageResource("icon.folderTree");

        table.put(".xml", Resources.getImageResource("icon.mainicon32"));
        table.put(".f90", Resources.getImageResource("icon.source"));
        table.put(".f95", Resources.getImageResource("icon.source"));
        table.put(".F", Resources.getImageResource("icon.source"));
        table.put(".for", Resources.getImageResource("icon.source"));
    }

    public Icon getIcon(File f)
    {
        // Do display custom icons

        // If dir
        if(f.isDirectory())
        {
            if(dirIcon!=null) return dirIcon;
            return new ImageIcon("myfoldericon.png");
        }

        // Get the name
        String name=f.getName();
        int idx=name.lastIndexOf(".");

        if(idx>-1)
        {
            String ext=name.substring(idx);
            if(table.containsKey(ext))
                return table.get(ext);
        }

        // For other files
        return new ImageIcon("myownfileicon.png");
    }
}
