package quadrasoft.mufortran.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Strings {
    public static void load() {
        InputStream input = Strings.class.getResourceAsStream("english.strings.properties");
        _properties = new Properties();
        try {
            _properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String s(String key) {
        String prop = _properties.getProperty(key);
        if (prop == null) return key;
        return prop;
    }

    private static Properties _properties;
}
