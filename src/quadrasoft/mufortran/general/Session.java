package quadrasoft.mufortran.general;

import quadrasoft.mufortran.resources.Strings;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session {
    /*
     * The application's parameters are saved in user's home folder under �Fortran
     * subfolder.
     */
    final private static String appDataSorageFolder = System.getProperty("user.home").replaceAll("\\\\", "/") + "/."
            + Strings.s("application.name") + "/";
    final private static String configFileName = "UserPrefs.txt";
    final private static String recentFileName = "recent.txt";
    private static String workDir = new String("");

    private static int autoSaveTreshold = 15;

    private static List<String> recentProjects = new ArrayList<String>();
    private static List<String> files = new ArrayList<String>();
    private static List<Project> projectsList = new ArrayList<Project>();

    private static Map<String, String> _parameters = new HashMap<>();

    private static boolean autoCheckForUpdates = true;
    private static boolean autoSave = true;

    final public static void addProject(Project project) {
        projectsList.add(project);
    }

    final public static void addRecent(String filename) {
        /*
		 * Adds this project file path to the recently opened projects list. If it is
		 * not already in the list The list has a maximum of 5 elements. Older elements
		 * are trashed to keep only 5.
		 */
        if (getRecentProjects().size() < 4 && !getRecentProjects().contains(filename))
            recentProjects.add(filename);
        else if (!getRecentProjects().contains(filename)) {
            recentProjects.remove(0);
            recentProjects.add(filename);
        }
    }

    final public static boolean autoCheck() {
        return autoCheckForUpdates;
    }

    final public static Project getActiveProject() {
		/*
		 * Returns the project actually flagged as selected
		 */
        for (Project project : projectsList) {
            if (project.isSelected())
                return project;
        }
        return null;
    }

    public static void switchActiveProject(Project p) {
        if (Session.getActiveProject() != null)
            Session.getActiveProject().setSelected(false);
        p.setSelected(true);
    }

    final public static String getAppdatasoragefolder() {
        return appDataSorageFolder;
    }

    public static int getAutoSaveTreshold() {
        return autoSaveTreshold;
    }

    public static void setAutoSaveTreshold(int autoSaveTreshold) {
        Session.autoSaveTreshold = autoSaveTreshold;
    }

    final public static List<String> getFiles() {
        return files;
    }

    final public static void setFiles(List<String> files) {
        Session.files = files;
    }

    final public static List<Project> getProjectsList() {
        return projectsList;
    }

    final public static List<String> getRecentProjects() {
        return recentProjects;
    }

    final public static void setRecentProjects(List<String> recentProjects) {
        Session.recentProjects = recentProjects;
    }

    final public static String getVersion() {
        return Strings.s("version");
    }

    final public static String getWorkDir() {
        return workDir.replaceAll("\\\\", "/");
    }

    final public static void setWorkDir(String workDir) {
        Session.workDir = workDir;
    }

    public static boolean isAutoSave() {
        return autoSave;
    }

    public static void setAutoSave(boolean autoSave) {
        Session.autoSave = autoSave;
    }

    final public static void load() {
		/*
		 * Loading parameters
		 */
        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(appDataSorageFolder + configFileName));

            String line;
            while ((line = br.readLine()) != null) {

                if (line.lastIndexOf("::") != -1) {
                    if (line.substring(0, line.lastIndexOf("::")).replaceAll("\\s+", "")
                            .equalsIgnoreCase("projectStoragefolder")) {
                        Session.setWorkDir(
                                line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")).replaceAll("\\\\", "/"));
                        if (!Session.getWorkDir().endsWith("/")) {
                            Session.setWorkDir(Session.getWorkDir() + "/");
                        }
                    } else if (line.substring(0, line.lastIndexOf("::")).replaceAll("\\s+", "")
                            .equalsIgnoreCase("FontName")) {
                        setParameter("FontName",
                                line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")).replaceAll("\\\\", "/"));
                    } else if (line.substring(0, line.lastIndexOf("::")).replaceAll("\\s+", "")
                            .equalsIgnoreCase("FontSize")) {
                        setParameter("FontSize", line.substring(line.lastIndexOf(":") + 1).trim());
                    } else if (line.substring(0, line.lastIndexOf("::")).replaceAll("\\s+", "")
                            .equalsIgnoreCase("Theme")) {
                        Session.setParameter("Theme", line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")));
                    } else if (line.substring(0, line.lastIndexOf("::")).replaceAll("\\s+", "")
                            .equalsIgnoreCase("autocheck")) {
                        if ((line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")))
                                .equalsIgnoreCase("false")) {
                            Session.setAutoCheck(false);

                        } else {
                            Session.setAutoCheck(true);
                        }
                    } else if (line.substring(0, line.lastIndexOf("::")).replaceAll("\\s+", "")
                            .equalsIgnoreCase("autoSave")) {
                        if ((line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")))
                                .equalsIgnoreCase("false")) {
                            Session.setAutoSave(false);
                        } else {
                            Session.setAutoSave(true);
                        }
                    } else if (line.substring(0, line.lastIndexOf("::")).replaceAll("\\s+", "")
                            .equalsIgnoreCase("autoSaveTreshold")) {
                        Session.setAutoSaveTreshold(
                                Integer.parseInt(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))));
                    } else if (line.substring(0, line.lastIndexOf("::")).replaceAll("\\s+", "")
                            .equalsIgnoreCase("execExtension")) {
                        setParameter("execExtension",
                                line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")).replaceAll("\\\\", "/"));
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		/*
		 * Loading recently opened projects.
		 */
        if (new File(appDataSorageFolder + recentFileName).exists()) {
            try {
                BufferedReader br;
                String line;
                br = new BufferedReader(new FileReader(appDataSorageFolder + recentFileName));
                while ((line = br.readLine()) != null) {
                    recentProjects.add(line);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String parameter(String key) {
        return _parameters.get(key);
    }

    final public static void save() {
		/*
		 * Saves the current session to filesystem.
		 */

		/*
		 * Check for workDir consistency TODO remove this check when not needed anymore
		 * (objective)
		 */
        if (!Session.getWorkDir().endsWith("/")) {
            Session.setWorkDir(Session.getWorkDir() + "/");
        }
        try {
            File file = new File(appDataSorageFolder + configFileName);
            {
                File folder = new File(appDataSorageFolder);
                folder.mkdirs();
            }
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("projectStoragefolder :: \"" + Session.getWorkDir() + "\"");
            fileWriter.write(System.getProperty("line.separator"));
            fileWriter.write("version :: \"" + Session.getVersion() + "\"");
            fileWriter.write(System.getProperty("line.separator"));
            fileWriter.write("Theme :: \"" + parameter("Theme") + "\"");
            fileWriter.write(System.getProperty("line.separator"));
            fileWriter.write("FontName :: \"" + parameter("FontName") + "\"");
            fileWriter.write(System.getProperty("line.separator"));
            fileWriter.write("FontSize :: " + parameter("FontSize"));
            fileWriter.write(System.getProperty("line.separator"));
            fileWriter.write("autoSaveTreshold :: \"" + Session.getAutoSaveTreshold() + "\"");
            fileWriter.write(System.getProperty("line.separator"));
            fileWriter.write("execExtension :: \"" + parameter("execExtension") + "\"");
            fileWriter.write(System.getProperty("line.separator"));
            if (Session.autoCheck())
                fileWriter.write("autocheck :: \"" + "true" + "\"");
            else
                fileWriter.write("autocheck :: \"" + "false" + "\"");
            fileWriter.write(System.getProperty("line.separator"));
            if (Session.isAutoSave())
                fileWriter.write("AutoSave :: \"" + "true" + "\"");
            else
                fileWriter.write("AutoSave :: \"" + "false" + "\"");
            fileWriter.write(System.getProperty("line.separator"));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		/*
		 * Recently opened project.
		 */
        try {
            File file = new File(appDataSorageFolder + recentFileName);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            for (String project : getRecentProjects()) {
                fileWriter.write(project);
                fileWriter.write(System.getProperty("line.separator"));
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    final public static void setAutoCheck(boolean arg0) {
        autoCheckForUpdates = arg0;
    }

    public static void setParameter(String key, String value) {
        _parameters.put(key, value);
    }
}
