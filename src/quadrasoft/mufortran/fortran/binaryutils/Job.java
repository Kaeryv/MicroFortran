package quadrasoft.mufortran.fortran.binaryutils;

import quadrasoft.mufortran.fortran.binaryutils.JobType;

import java.io.File;
import java.util.List;

public class Job {
    private String name;

    private String executableAbsolutePath;
    private List<String> arguments;
    private String context;
    private JobType nature;

    public Job(String nm, String exepath, JobType nt) {
        name = nm;
        if (new File(exepath).exists() || exepath.equals("gfortran"))
            executableAbsolutePath = exepath;
        nature = nt;
    }

    public void addArgument(String argument) {
        this.arguments.add(argument);
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getExecutableAbsolutePath() {
        return executableAbsolutePath;
    }

    public void setExecutableAbsolutePath(String executableAbsolutePath) {
        this.executableAbsolutePath = executableAbsolutePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JobType getNature() {
        return nature;
    }

    public void setNature(JobType nature) {
        this.nature = nature;
    }
}
