package quadrasoft.mufortran.display;

import java.io.File;

public class MyFile extends File {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MyFile(String arg0) {
        super(arg0);
    }

    @Override
    public String toString() {
        return getName();
    }
}
