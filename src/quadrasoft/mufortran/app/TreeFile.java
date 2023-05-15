package quadrasoft.mufortran.app;

public class TreeFile {
    private String name;
    private String iconPath;
    private boolean isFile = false;
    private String path;
    private String filename;

    TreeFile(String name, String Icon) {
        this.name = name;
        this.iconPath = Icon;
    }

    TreeFile(String name, String Icon, String filename) {
        this.name = name;
        this.iconPath = Icon;
        this.filename = filename;
        isFile = true;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIcon() {
        return iconPath;
    }

    public void setIcon(String flagIcon) {
        this.iconPath = flagIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFile() {
        return this.isFile;
    }

    public void setFile(boolean file) {
        this.isFile = file;
    }
}
