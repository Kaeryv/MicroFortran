package quadrasoft.mufortran.fortran;

import quadrasoft.mufortran.resources.Strings;

public class FileTypesManager {
    private static final String[] fortranExtensions = {"f", "F", "f90", "F90", "f95", "F95", "for"};
    private static final String[] textFileExtensions = {"txt"};

    public static boolean isFortranSource(String path) {
        for (String ext : fortranExtensions) {
            if (path.endsWith("." + ext)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isObjectFile(String path) {

        if (path.endsWith(".o"))
            return true;

        return false;
    }

    public static boolean isProjectFile(String path) {
        if (path.endsWith(Strings.s("application.project_extension")))
            return true;
        return false;
    }

    public static boolean isTextFile(String path) {
        for (String ext : textFileExtensions) {
            if (path.endsWith("." + ext)) {
                return true;
            }
        }
        return false;
    }
}
