package quadrasoft.mufortran.fortran;

import quadrasoft.mufortran.fortran.binaryutils.Compiler;
import quadrasoft.mufortran.fortran.binaryutils.Job;
import quadrasoft.mufortran.fortran.binaryutils.JobType;
import quadrasoft.mufortran.general.CompilerLog;
import quadrasoft.mufortran.general.Log;
import quadrasoft.mufortran.general.Project;
import quadrasoft.mufortran.general.Session;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinaryManager {

    static Project boundProject;

    static Job job;
    static boolean debug_active = false;

    public static void bindProject(Project prj) {
        boundProject = prj;
    }

    private static void checkBinariesFolderExistance() {
        // the build path and object folder
        new File(boundProject.getPath() + "/bin/").mkdirs();
    }

    private static void checkLoggingFolderExistance() {
        // the build path and object folder
        new File(boundProject.getPath() + "/logs/").mkdirs();
    }

    private static void checkObjectFolderExistance() {
        // the build path and object folder
        new File(boundProject.getPath() + boundProject.getObjectPath()).mkdirs();
    }

    public static void compileFile(String path) {
        Compiler aCompiler = new Compiler();
        aCompiler.clear();
        job = new Job("Compile file" + boundProject.getName(), boundProject.getCompilerPath(), JobType.COMPILING);
        job.setContext(boundProject.getPath() + boundProject.getObjectPath());
        // We tell the compiler we are going to compile with a certain compiler
        aCompiler.setJob(job);
        // We send the source files to the compiler.
        aCompiler.input(Arrays.asList(path));
        // We specify the output object files to the compiler
        // We specify additive options
        aCompiler.option(boundProject.getExternals());

        if (debug_active)
            aCompiler.option(Arrays.asList("-Og", "-fimplicit-none", "-Wline-truncation", "-Wampersand",
                    "-Wcharacter-truncation", "-Wsurprising", "-Waliasing", "-Wimplicit-interface", "-Wconversion",
                    "-Wunused-parameter", "-fwhole-file", "-fcheck=all", "-std=f2008", "-pedantic", "-fbacktrace",
                    "-Wunused", "-Wreal-q-constant", "-Wtarget-lifetime","-Wc-binding-type",
                    "-Wintrinsics-std", "-Wintrinsic-shadow", "-Wno-tabs"
            ));
        // We init the process.
        aCompiler.init();
        checkObjectFolderExistance();
        aCompiler.start();
        if (boundProject.isPrintLog()) {
            checkLoggingFolderExistance();
            CompilerLog.save(boundProject.getPath() + "/logs/build.log");
        }
    }

    public static void compileProject() {
        Compiler aCompiler = new Compiler();
        aCompiler.clear();
        job = new Job("Compile project" + boundProject.getName(), boundProject.getCompilerPath(), JobType.COMPILING);
        job.setContext(boundProject.getPath() + boundProject.getObjectPath());
        // We tell the compiler we are goind to compile with a certain compiler
        aCompiler.setJob(job);
        // We send the source files to the compiler.
        aCompiler.input(prepareFilesForCompilation());
        // We specify the output object files to the compiler
        // We specify additive options
        aCompiler.option(boundProject.getExternals());
        if (debug_active)
            aCompiler.option(Arrays.asList("-Og", "-fimplicit-none", "-Wline-truncation", "-Wampersand",
                    "-Wcharacter-truncation", "-Wsurprising", "-Waliasing", "-Wimplicit-interface", "-Wconversion",
                    "-Wunused-parameter", "-fwhole-file", "-fcheck=all", "-std=f2008", "-pedantic", "-fbacktrace",
                    "-Wno-tabs", "-Wunused", "-Wreal-q-constant", "-Wtarget-lifetime","-Wc-binding-type",
                    "-Wintrinsics-std", "-Wintrinsic-shadow"
            ));
        // We init the process.
        aCompiler.init();
        checkObjectFolderExistance();
        aCompiler.start();
        if (boundProject.isPrintLog()) {
            checkLoggingFolderExistance();
            CompilerLog.save(boundProject.getPath() + "/logs/build.log");
        }

    }

    private static List<String> createExecutableOutput() {
        String exeName = boundProject.getPath() + "/bin/" + boundProject.getExecutableName()
                + Session.parameter("execExtension");
        List<String> ret = new ArrayList<String>();
        ret.add(exeName);
        return ret;

    }

    private static List<String> createOutputFilesList() {
        // This function takes source files list from bound project and generates the
        // expected output files names.
        List<String> files = new ArrayList<String>();

        for (String source : boundProject.getSource()) {
            if (FileTypesManager.isFortranSource(source)) {
                String fileName = source.substring(0, source.lastIndexOf("."));
                String sourceAbsPath = boundProject.getPath() + "/obj/" + fileName + ".o";
                files.add(sourceAbsPath);
            } else {
                // The file specified isn't recognized as a fortran source for some reason.
                Log.send("Error: Compiler was specified an icorrect file : " + source);
            }

        }

        return files;

    }

    public static void debug(boolean b) {
        debug_active = b;
    }

    public static void launchFile(final String file) {
        Compiler aCompiler = new Compiler();
        aCompiler.clear();
        job = new Job("Run file" + file, file, JobType.EXECUTING);
        job.setContext(Session.getWorkDir());
        aCompiler.setJob(job);
        List<String> temp = new ArrayList<String>();
        temp.add(file);
        aCompiler.input(temp);
        Log.send(file);
        aCompiler.init();
        aCompiler.start();
    }

    public static void launchProject() {
        Compiler aCompiler = new Compiler();
        aCompiler.clear();
        job = new Job("Run project" + boundProject.getName(), boundProject.getCompilerPath(), JobType.EXECUTING);
        job.setContext(boundProject.getPath() + boundProject.getExecutionPath());
        // We tell the compiler we are going to link with a certain compiler
        aCompiler.setJob(job);
        aCompiler.input(createExecutableOutput());
        aCompiler.init();
        aCompiler.start();
    }

    public static void linkProject() {
        Compiler aCompiler = new Compiler();
        aCompiler.clear();
        job = new Job("Link project" + boundProject.getName(), boundProject.getCompilerPath(), JobType.LINKING);
        job.setContext(boundProject.getPath());
        // We tell the compiler we are goind to link with a certain compiler
        aCompiler.setJob(job);
        // We send the .o files to the compiler.
        aCompiler.input(createOutputFilesList());
        // We specify the executable name to the compiler
        aCompiler.output(createExecutableOutput());
        // We init the process.
        aCompiler.init();
        checkBinariesFolderExistance();
        aCompiler.start();
        if (boundProject.isPrintLog()) {
            checkLoggingFolderExistance();
            CompilerLog.save(boundProject.getPath() + "/logs/link.log");
        }
    }

    private static List<String> prepareFilesForCompilation() {
        List<String> files = new ArrayList<String>();
        for (String source : boundProject.getSource()) {
            if (job.getNature() == JobType.COMPILING) {
                if (FileTypesManager.isFortranSource(source)) {
                    String sourceAbsPath = boundProject.getPath() + source;
                    if (new File(sourceAbsPath).exists()) {
                        files.add(sourceAbsPath);
                    } else {
                        Log.send("Error: Could not load file : " + source);
                    }
                } else {
                    Log.send("Error: Compiler was specified an icorrect file : " + source);
                }
            }
        }
        return files;
    }
}
