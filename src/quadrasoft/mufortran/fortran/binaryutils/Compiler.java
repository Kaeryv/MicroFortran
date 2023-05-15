package quadrasoft.mufortran.fortran.binaryutils;

import quadrasoft.mufortran.fortran.ExecutorConsole;
import quadrasoft.mufortran.fortran.binaryutils.Job;
import quadrasoft.mufortran.general.CompilerLog;
import quadrasoft.mufortran.general.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Compiler extends Thread {
    public BufferedReader bri;
    public BufferedReader bre;
    public BufferedWriter bw;
    MagicBoxStatus state;
    ProcessBuilder builder;
    ExecutorConsole console = new ExecutorConsole();
    private Job job;
    private List<String> commands = new ArrayList<String>();
    private List<String> inputFiles = new ArrayList<String>();
    private List<String> outputFiles = new ArrayList<String>();
    private List<String> options = new ArrayList<String>();

    private void cleanUp() {
        commands.clear();
        outputFiles.clear();
    }

    public void clear() {
        inputFiles.clear();
        outputFiles.clear();
        options.clear();
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void init() {
        // init prepares the command buffer by filling it with basic options and files
        commands.clear();
        if (job.getNature() == JobType.COMPILING) {
            commands.add(job.getExecutableAbsolutePath());
            commands.add("-c");
            commands.addAll(inputFiles);
            // commands.add("-o");
            // commands.addAll(outputFiles);
            commands.addAll(options);
        } else if (job.getNature() == JobType.LINKING) {
            commands.add(job.getExecutableAbsolutePath());
            commands.add("-o");
            commands.addAll(outputFiles);
            commands.addAll(inputFiles);
        } else if (job.getNature() == JobType.EXECUTING) {
            commands.addAll(inputFiles);
        }
        System.out.println(commands);

        // Then we create the process
        builder = new ProcessBuilder(commands);

        builder.directory(new File(job.getContext()));
        // We like to get a separate error stream, easy to detect fails.
        builder.redirectErrorStream(false);

    }

    public void input(List<String> f) {
        inputFiles.addAll(f);
    }

    public void option(List<String> f) {
        options.addAll(f);
    }

    public void output(List<String> f) {
        outputFiles.addAll(f);
    }

    @Override
    public void run() {
        if (job.getNature() == JobType.EXECUTING) {
            // Starting timestamp
            long time1 = System.currentTimeMillis();
            // We wake up the console
            console.activate();
            try {
                // Let's start the process
                Process p = builder.start();
                // Creating error and info streams. Creating also commands stream.
                bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
                bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                // We bind the commands flow and the process control to the console.
                console.bindFlows(bw, p);
                console.setProcessRunning(true);
                // Now we fetch the program's output.
                String line;
                while (p.isAlive()) {
                    while ((line = bri.readLine()) != null) {
                        ExecutorConsole.sendInfo(line);
                    }
                    while ((line = bre.readLine()) != null) {
                        ExecutorConsole.sendError(line);
                    }
                }
                // The program has ended.
                console.setProcessRunning(false);
                // We clear the runnable.
                this.cleanUp();
                // Ending timestamp
                long time2 = System.currentTimeMillis();
                // We notify the user within the console.
                ExecutorConsole.sendInfo("Process terminated in " + String.valueOf(time2 - time1) + " ms");

            } catch (IOException e) {
                Log.send(
                        "Error: Could not start specified compiler. Consider checking it's path in the project options.");
                e.printStackTrace();
            }
        } else {
            // Then we compile or link.

            // We get starting timestamp
            long time1 = System.currentTimeMillis();
            state = MagicBoxStatus.WORKING;
            try {
                Process p = builder.start();
                // Check if it started gracefully
                if (!p.isAlive()) {
                    Log.send("Error: Could not start compiler.");
                }
                // Creating error and info streams.
                BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                // Ensuring the compiler log is empty.
                CompilerLog.reset();
                // Gathering output.
                String line;
                while ((line = bri.readLine()) != null) { // Classic pipe
                    CompilerLog.send(line);
                }
                state = MagicBoxStatus.FINE;
                while ((line = bre.readLine()) != null) { // Error and warning pipe
                    state = MagicBoxStatus.WARNING;
                    if (line.contains("Error:")) {
                        state = MagicBoxStatus.FAILED;
                    }
                    CompilerLog.send(line);
                }
                long time2 = System.currentTimeMillis();

                if (state == MagicBoxStatus.FINE) {
                    Log.send("Operation executed gracefully in " + String.valueOf(time2 - time1) + " ms");
                } else if (state == MagicBoxStatus.WARNING) {
                    Log.send("Warning: Operation executed with warnings" + String.valueOf(time2 - time1) + " ms"
                            + ", check build log");
                } else if (state == MagicBoxStatus.FAILED) {
                    Log.send("Error: Operation failed in " + String.valueOf(time2 - time1) + " ms");
                }

                // Clearing all the streams
                bre.close();
                bri.close();
                // We wait for the process to end.
                p.waitFor();
                p.destroy();

                this.cleanUp();
                state = MagicBoxStatus.IDLE;

            } catch (IOException | InterruptedException e) {
                Log.send("Error: Could not start compiler.");
                e.printStackTrace();
            }
        }

    }

}
