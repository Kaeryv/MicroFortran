package quadrasoft.mufortran.resources;

import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.List;

public class Resources {
    private static List<Resource> resList = new ArrayList<Resource>();

    public static final ImageIcon getImageResource(String name) {
        for (Resource res : resList) {
            if (res.getName().equalsIgnoreCase(name))
                return res.getImage();
        }
        return null;
    }

    static public Theme theme;

    public static void load() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            URL resource_font = Resources.class.getResource("fonts/" + "FiraCode-Regular.ttf");
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(resource_font.getFile())));

        } catch (IOException | FontFormatException e) {
        }
        InputStream in = Resources.class.getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/" + "dark.xml");
        theme = null;
        try {
            theme = Theme.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resList.add(new Resource("img.splash.png"));
        resList.add(new Resource("icon.icon2.png"));
        resList.add(new Resource("icon.add.png"));
        resList.add(new Resource("icon.addmore.png"));
        resList.add(new Resource("icon.close.png"));
        resList.add(new Resource("icon.delete.png"));
        resList.add(new Resource("icon.document.png"));
        resList.add(new Resource("icon.folder.png"));
        resList.add(new Resource("icon.settings.png"));
        resList.add(new Resource("icon.screen.png"));
        resList.add(new Resource("icon.param.png"));
        resList.add(new Resource("icon.import.png"));
        resList.add(new Resource("icon.projfile.png"));
        resList.add(new Resource("icon.sourcefile.png"));
        resList.add(new Resource("icon.emptyfile.png"));
        resList.add(new Resource("icon.source.png"));
        resList.add(new Resource("icon.mainicon.png"));
        resList.add(new Resource("icon.mainicon32.png"));
        resList.add(new Resource("icon.editor.png"));
        resList.add(new Resource("icon.tree.png"));
        resList.add(new Resource("icon.png.png"));
        resList.add(new Resource("img.loading.png"));
        resList.add(new Resource("img.create.png"));
        resList.add(new Resource("img.import.png"));
        resList.add(new Resource("img.source.png"));
        resList.add(new Resource("icon.build.png"));
        resList.add(new Resource("icon.cancel.png"));
        resList.add(new Resource("icon.compexec.png"));
        resList.add(new Resource("icon.copy.png"));
        resList.add(new Resource("icon.cut.png"));
        resList.add(new Resource("icon.magnifier.png"));
        resList.add(new Resource("icon.flag.png"));
        resList.add(new Resource("icon.newfile.png"));
        resList.add(new Resource("icon.folder.png"));
        resList.add(new Resource("icon.paste.png"));
        resList.add(new Resource("icon.play.png"));
        resList.add(new Resource("icon.redo.png"));
        resList.add(new Resource("icon.save.png"));
        resList.add(new Resource("icon.saveas.png"));
        resList.add(new Resource("icon.undo.png"));
        resList.add(new Resource("icon.file.png"));
        resList.add(new Resource("icon.project.png"));
        resList.add(new Resource("icon.projectOpen.png"));
        resList.add(new Resource("icon.workspace.png"));
        resList.add(new Resource("icon.exec.png"));
        resList.add(new Resource("icon.mod.png"));
        resList.add(new Resource("icon.graph.png"));
        resList.add(new Resource("icon.folderTree.png"));
        resList.add(new Resource("icon.browser.png"));
        resList.add(new Resource("icon.error.png"));
        resList.add(new Resource("icon.link.png"));
        resList.add(new Resource("icon.build_file.png"));
        resList.add(new Resource("icon.flagdown.png"));
        resList.add(new Resource("icon.console.png"));
        resList.add(new Resource("icon.bug.png"));
        resList.add(new Resource("icon.redbug.png"));
        resList.add(new Resource("icon.gamepad.png"));
        resList.add(new Resource("icon.eraser.png"));
    }

    public static class Resource {
        private String name = new String("");
        private ImageIcon image;

        public Resource(String path) {
            this.setImage(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("icons/" + path))));
            this.setName(path.substring(0, path.lastIndexOf(".")));
        }

        public ImageIcon getImage() {
            return image;
        }

        public void setImage(ImageIcon image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    //static Map<String, Resource> _resources_map = new HashMap<String, Resource>();
}
