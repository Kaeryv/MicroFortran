package quadrasoft.mufortran.general;

public final class OsUtils {
    private static String OS = null;

    public static String getOsName() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    public static boolean isUnix() {
        return getOsName().contains("Linux");
    }// and so on

    public static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }
}