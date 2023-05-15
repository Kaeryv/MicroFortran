package quadrasoft.mufortran.fortran;

import java.io.File;

public class MuFile extends File {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    boolean projectMember;

    public MuFile(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public boolean isFortranSource() {
        return FileTypesManager.isFortranSource(this.getName());
    }

    public boolean isTextFile() {
        return FileTypesManager.isTextFile(this.getName());
    }
}
