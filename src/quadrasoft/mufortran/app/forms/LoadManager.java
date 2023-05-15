package quadrasoft.mufortran.app.forms;

import quadrasoft.mufortran.general.Log;
import quadrasoft.mufortran.general.OsUtils;
import quadrasoft.mufortran.general.Session;
import quadrasoft.mufortran.display.DisplayImage;
import quadrasoft.mufortran.display.LoadingProgressBar;
import quadrasoft.mufortran.resources.Resources;
import quadrasoft.mufortran.resources.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;

public class LoadManager extends JFrame {
    private static final long serialVersionUID = 1L;
    // private JFileChooser chooser;
    private static JProgressBar progressBar;
    final private String baseHomeDirectory = System.getProperty("user.home");
    final private String configFileName = "UserPrefs.txt";
    private DisplayImage imagePanel;

    public LoadManager() {
		/*
		 * Loading resource files.
		 */
        Resources.load();
        Strings.load();
		/*
		 * Generating GUI.
		 */
        this.getContentPane().setLayout(new BorderLayout());
        try {
            this.setImagePanel(new DisplayImage("img.loading"));
            this.getImagePanel().setLocation(0, 0);
            this.getImagePanel().setSize(697, 353);
            this.getContentPane().add(this.getImagePanel(), BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        progressBar = new LoadingProgressBar();
        getContentPane().add(progressBar, BorderLayout.SOUTH);

        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
        this.setSize(new Dimension(698, 353));

        this.setLocationRelativeTo(null);
        this.setVisible(true);

		/*
		 * Printing java info etc in console.
		 */
        this.printIdentityStack();
    }

    public final static void checkVersion() throws IOException {
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String line = new String();
                    BufferedReader br;
                    ReadableByteChannel rbc;
                    FileOutputStream fos;
                    URL website = new URL(Strings.s("application.url_version"));

                    rbc = Channels.newChannel(website.openStream());
                    fos = new FileOutputStream(Session.getAppdatasoragefolder() + "VERSION");
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.close();

                    if (new File(Session.getAppdatasoragefolder() + "VERSION").exists()) {
                        br = new BufferedReader(new FileReader(Session.getAppdatasoragefolder() + "VERSION"));
                        line = br.readLine();
                        br.close();
                        if (!line.replaceAll("\\s+", "").trim().equalsIgnoreCase(Session.getVersion())) {
                            Log.send(Strings.s("terminal.new_version") + ".");
                            Log.send("[[link]]" + Strings.s("application.url_releases"));
                            /*if (JOptionPane.showConfirmDialog(new JFrame(), Strings.s("Console:NewVersion") + " ["
                                    + line + "], " + Strings.s("Forms:GotoDL")) == JOptionPane.OK_OPTION) {

                                Desktop.getDesktop().browse(new URI("https://github.com/Kaeryv/muFortran/releases"));

                            }*/
                        } else {
                            Log.send(Strings.s("application.name") + " is up to date [" + Strings.s("version") + "].");
                        }
                    } else {
                        Log.send(Strings.s("terminal.unable_fetch_version"));
                    }
                } catch (IOException e) {
                    Log.send(Strings.s("terminal.versioncheck_dl_failed"));
                }
            }

        });
        T.start();

    }

    public static void loadTheme() {
        progressBar.setValue(40);
        // setup the look and feel properties
        Properties props = new Properties();

        props.put("logoString", Strings.s("application.name"));
        props.put("textAntiAliasing", "on");

        props.put("selectionBackgroundColor", "180 240 197");
        props.put("menuSelectionBackgroundColor", "245 184 0");

        // set your theme
        try {
            switch (Session.parameter("Theme")) {
                case "Classic":
                    com.jtattoo.plaf.fast.FastLookAndFeel.setCurrentTheme(props);
                    UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
                    break;
                case "Aluminium":
                    com.jtattoo.plaf.aluminium.AluminiumLookAndFeel.setCurrentTheme(props);
                    UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
                    break;
                case "Mint":
                    com.jtattoo.plaf.fast.FastLookAndFeel.setCurrentTheme(props);
                    UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
                    break;
                case "Leather":
                    com.jtattoo.plaf.texture.TextureLookAndFeel.setCurrentTheme(props);
                    UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
                    // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.GTKLookAndFeel");
                    // case "Fresh":
                    // com.jtattoo.plaf.texture.TextureLookAndFeel.setCurrentTheme(props);
                    // UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
                    // WebLookAndFeel.install ();
                    // WebLookAndFeel.initializeManagers();

                    break;
                default:
                    com.jtattoo.plaf.fast.FastLookAndFeel.setCurrentTheme(props);
                    UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
            }
            /*
			 * if (Session.getTheme().equals("Aluminium")) {
			 * com.jtattoo.plaf.aluminium.AluminiumLookAndFeel.setCurrentTheme(props);
			 * UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
			 *
			 * } else if (Session.getTheme().equals("Leather")) {
			 * com.jtattoo.plaf.texture.TextureLookAndFeel.setCurrentTheme(props);
			 * UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
			 *
			 * } else if (Session.getTheme().equals("Graphene")) {
			 * com.jtattoo.plaf.graphite.GraphiteLookAndFeel.setCurrentTheme(props);
			 * UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
			 *
			 * } else if (Session.getTheme().equals("Aero")) {
			 * com.jtattoo.plaf.aero.AeroLookAndFeel.setCurrentTheme(props);
			 * UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
			 *
			 * } else if (Session.getTheme().equals("Fast")) {
			 * com.jtattoo.plaf.fast.FastLookAndFeel.setCurrentTheme(props);
			 * UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel"); } else if
			 * (Session.getTheme().equals("Mint")) {
			 * com.jtattoo.plaf.fast.FastLookAndFeel.setCurrentTheme(props);
			 * UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel"); } else {
			 * com.jtattoo.plaf.fast.FastLookAndFeel.setCurrentTheme(props);
			 * UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel"); }
			 */
            progressBar.setValue(70);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        this.setVisible(false);
        this.dispose();
    }

    private void createConfiguration() {
        Log.send(Strings.s("terminal.userprefs_creation"));
        {
            // --- Warning the user we are about to create a user preferences file
            JOptionPane.showConfirmDialog(this, Strings.s("messages.first_launch_warning"),
                    "Warning", JOptionPane.OK_CANCEL_OPTION);
        }
        {
            JFileChooser chooser = new JFileChooser(baseHomeDirectory);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Select the folder where you want to store projects :");

            // --- Getting the data from Filechooser object.
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                Session.setWorkDir(chooser.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "/") + "/");
            } else if (chooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION
                    || chooser.showOpenDialog(this) == JFileChooser.ERROR_OPTION) { // Else, we take default folder in
                // home directory
                Session.setWorkDir(baseHomeDirectory);
            }
        }
        if (OsUtils.isUnix())
            Session.setParameter("execExtension", ".exe");
        else if (OsUtils.isWindows())
            Session.setParameter("execExtension", ".exe");
        Session.setParameter("Theme", "Classic");
        Session.setParameter("FontName", "Courrier New");
        Session.setParameter("FontSize", "18");
        // Ensuring that the working folder exists
        new File(Session.getWorkDir()).mkdirs();
        Session.save();
    }

    public DisplayImage getImagePanel() {
        return imagePanel;
    }

    public void setImagePanel(DisplayImage imagePanel) {
        this.imagePanel = imagePanel;
    }

    public void loadConfig() {
		/*
		 * Load configuration file.
		 *
		 */
        progressBar.setValue(10);
        // First, we check if there is a config file
        if (new File(Session.getAppdatasoragefolder() + configFileName).exists()) {
            // If it exists, we load it.
            Session.load();
            if (!new File(Session.getWorkDir()).exists()) {
                repairFolder(); // We re-ask where is that folder ...
            }
        } else {
			/*
			 * There is no configuration, we create one.
			 */
            createConfiguration();
        }
        progressBar.setValue(30);
    }

    private void printIdentityStack() {
        String osname = System.getProperty("os.name");
        String osvers = System.getProperty("os.version");
        String osbits = System.getProperty("os.arch");
        String javavd = System.getProperty("java.vendor");
        String javave = System.getProperty("java.version");
        String javabt = System.getProperty("sun.arch.data.model");
        Log.send(String.format(Strings.s("terminal.system_info"), osname, osvers, osbits));
        Log.send(String.format(Strings.s("terminal.java_info"), javave, javave, javabt));
    }

    private void repairFolder() {
        // TODO rework this part.

        {
            // --- Warning the user we are about to create a user preferences file
            JOptionPane.showConfirmDialog(this, "Unable to find project folder, so let's repair it.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        {
            JFileChooser chooser = new JFileChooser(baseHomeDirectory);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Select the folder where you want to store projects :");

            // --- Getting the data from Filechooser object.
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                Session.setWorkDir(chooser.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "/") + "/");
            } else if (chooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION
                    || chooser.showOpenDialog(this) == JFileChooser.ERROR_OPTION) { // Else, we take default folder in
                // home directory
                Session.setWorkDir(baseHomeDirectory);
            }
        }
        new File(Session.getWorkDir()).mkdirs();
        Session.save();
    }

}
