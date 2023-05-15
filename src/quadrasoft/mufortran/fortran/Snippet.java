package quadrasoft.mufortran.fortran;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Snippet {
    List<String> code_lines = new ArrayList<String>();
    public Snippet(String kind, String name) {
        code_lines.add("! ---------------------------------");
        code_lines.add("! µFortran auto generated " + kind + ".");
        code_lines.add("! Created for : " + System.getProperty("user.name"));
        code_lines.add("! Date : " + new java.util.Date());
        code_lines.add("! ---------------------------------");

        if (kind.equals("program")) {
            code_lines.add("program " + name);
            code_lines.add("    implicit none");
            code_lines.add("    print *, \"Hello world!\"");
            code_lines.add("end program " + name);
        } else if (kind.equals("module")) {
            code_lines.add("module " + name);
            code_lines.add("!   module contents go here.");
            code_lines.add("end module " + name);
        } else if (kind.equals("function")) {
            code_lines.add("function " + name + "()");
            code_lines.add("!    function code goes here.");
            code_lines.add("end function ");
        } else if (kind.equals("subroutine")) {
            code_lines.add("subroutine " + name + "(a, b, c)");
            code_lines.add("    integer, intent(in) :: a, b");
            code_lines.add("    integer, intent(out) :: c");
            code_lines.add("    c = a + b");
            code_lines.add("end subroutine " + name);
        }
    }
    public void write_to_file(Path filepath) throws IOException {
        Path fileName = filepath.getFileName();
        Path directory = filepath.getParent();
        File file = filepath.toFile();
        if (!file.exists()) {
            directory.toFile().mkdirs();
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        for(int i = 0; i < code_lines.size(); i++) {
            fileWriter.write(code_lines.get(i));
            fileWriter.write(System.getProperty("line.separator"));
        }
        fileWriter.close();
    }
}