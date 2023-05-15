package quadrasoft.mufortran.general;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import quadrasoft.mufortran.fortran.Snippet;
import quadrasoft.mufortran.resources.Strings;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {
    private String name;
    private String path;
    private Path project_filepath;
    private String buildPath;
    private String compilerPath;
    private String argument;
    private String executionPath;
    private String executableName;
    private String compilerOption;
    private String Author;
    private Date lastEdit;
    private boolean printLog = false;
    private List<String> source = new ArrayList<String>();
    private List<String> externals = new ArrayList<String>();

    private boolean selected = false;

    public void save_to_xml(File file) {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        try
        {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }

        final Document document = builder.newDocument();

        final Element root = document.createElement("project");
        root.setAttribute("name", this.getName());
        document.appendChild(root);

        final Element compilationElement = document.createElement("compilation");
        compilationElement.setAttribute("gfortranCommand", compilerPath);
        compilationElement.setAttribute("gfortranBuildPath", buildPath);
        compilationElement.setAttribute("gfortranFlags", compilerOption);
        compilationElement.setAttribute("gfortranTarget", executableName);
        if (printLog)
        {
            compilationElement.setAttribute("gfortranDumpLog", "true");
        }
        else
        {
            compilationElement.setAttribute("gfortranDumpLog", "false");
        }
        root.appendChild(compilationElement);

        final Element executionElement = document.createElement("execution");
        executionElement.setAttribute("inputArguments", argument);
        executionElement.setAttribute("executionPath", executionPath);
        root.appendChild(executionElement);

        final Element sourcesElement = document.createElement("sources");
        root.appendChild(sourcesElement);
        for (String src : this.source)
        {
            final Element fileElement = document.createElement("file");
            fileElement.setAttribute("relativePath", src);
            sourcesElement.appendChild(fileElement);
        }

        final Element externalsElement = document.createElement("externals");
        root.appendChild(externalsElement);
        for (String ext : externals)
        {
            final Element fileElement = document.createElement("file");
            fileElement.setAttribute("relativePath", ext);
            externalsElement.appendChild(fileElement);
        }

        final Element authorElement = document.createElement("author");
        authorElement.setTextContent(Author);
        root.appendChild(authorElement);

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try
        {
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMSource source = new DOMSource(document);

            final StreamResult sortie = new StreamResult(file);


            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, sortie);
        } catch (TransformerException e)
        {
            e.printStackTrace();
        }
    }
    public void load_from_xml() {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse(project_filepath.toFile());

            final Element root = document.getDocumentElement();

            if (!root.getTagName().equals("project"))
            {
                throw new IOException("Bad project file.");
            }

            this.setName(root.getAttribute("name"));

            final NodeList nodes = root.getChildNodes();
            final int length = nodes.getLength();

            for (int i = 0; i < length; i++)
            {
                if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)
                {
                    final Element node = (Element) nodes.item(i);

                    if (node.getTagName().equals("compilation"))
                    {
                        compilerPath = node.getAttribute("gfortranCommand");
                        buildPath = node.getAttribute("gfortranBuildPath");
                        compilerOption = node.getAttribute("gfortranFlags");
                        executableName = node.getAttribute("gfortranTarget");

                        String temp = node.getAttribute("gfortranDumpLog");

                        if (temp.equalsIgnoreCase("true"))
                        {
                            printLog = true;
                        }
                        else if (temp.equalsIgnoreCase("false"))
                        {
                            printLog = false;
                        }
                        else
                        {
                            throw new IOException("Bad value for boolean gfortranDumpLog");
                        }
                    }
                    else if (node.getTagName().equals("sources"))
                    {
                        final NodeList sourceNodes = node.getChildNodes();
                        final int sources_count = sourceNodes.getLength();

                        for (int j = 0; j < sources_count; j++)
                        {
                            if (sourceNodes.item(j).getNodeType() == Node.ELEMENT_NODE)
                            {
                                final Element sourceFile = (Element) sourceNodes.item(j);
                                source.add(sourceFile.getAttribute("relativePath"));
                            }
                        }
                    }
                    else if (node.getTagName() == "externals")
                    {
                        final NodeList sourceNodes = node.getChildNodes();
                        final int sources_count = sourceNodes.getLength();

                        for (int j = 0; j < sources_count; j++)
                        {
                            if (sourceNodes.item(j).getNodeType() == Node.ELEMENT_NODE)
                            {
                                final Element sourceFile = (Element) sourceNodes.item(j);
                                externals.add(sourceFile.getAttribute("relativePath"));
                            }
                        }
                    }
                    else if (node.getTagName() == "execution")
                    {
                        argument = node.getAttribute("inputArguments");
                        executionPath = node.getAttribute("executionPath");
                    }
                    else if (node.getTagName() == "author")
                    {
                        Author = node.getTextContent();
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e)
        {
            e.printStackTrace();
        }
    }
    /*
        Loads a project from project filename
     */
    public Project(String project_path) {
        setProjectFilepath(project_path);

        setName(project_filepath.getFileName().toString().replaceFirst("[.][^.]+$", ""));
        setPath(project_filepath.getParent().toString() + "/"); // Path

        if (project_filepath.toFile().exists()) {
            load_from_xml();
            Session.switchActiveProject(this);
        } else {
            Log.send("Project file not available.");
        }
    }
    /*
        Creates brand new project with options.
     */
    public Project(String project_name, String project_path, String build_path, String project_compiler_path) {
        source.add("main.f90");
        setName(project_name);
        setPath(project_path + name + "/");
        printLog = false;
        setProjectFilepath(path + name + Strings.s("application.project_extension"));
        setBuildPath(build_path);
        this.setArgument("");
        setCompilerPath(project_compiler_path);
        this.setCompilerOption("-o");
        this.setExecutionPath("./");
        this.setAuthor(System.getProperty("user.name"));
        this.setExecutableName(getName());
        if (Session.getActiveProject() != null)
            Session.getActiveProject().setSelected(false);
        this.setSelected(true);
    }

    public Project(String arg0, String arg1, String arg2, String arg3, List<String> arg4) {
        setName(arg0);
        if (arg1.contains("\\")) {
            arg1 = arg1.replaceAll("\\\\", "/") + "/";
        }
        setPath(arg1 + name + "/");
        setProjectFilepath(path + name + Strings.s("application.project_extension"));
        setBuildPath(arg2);
        setCompilerPath(arg3);
        this.setArgument("");
        this.setCompilerOption("-o");
        this.setAuthor(System.getProperty("user.name"));
        this.setExecutableName(getName());
        this.setExecutionPath("/bin/");
        printLog = false;
        source = arg4;
        if (Session.getActiveProject() != null)
            Session.getActiveProject().setSelected(false);
        this.setSelected(true);
    }

    public void createStartFile() throws IOException {
        Path filepath = Paths.get(path, "main.f90");
        Snippet snip = new Snippet("program", "main");
        snip.write_to_file(filepath);
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getBuildPath() {
        return buildPath;
    }

    public void setBuildPath(String buildPath) {
        this.buildPath = buildPath;
    }

    public String getCompilerOption() {
        return compilerOption;
    }

    public void setCompilerOption(String compilerOption) {
        this.compilerOption = compilerOption;
    }

    public String getCompilerPath() {
        return compilerPath;
    }

    public void setCompilerPath(String compilerPath) {
        this.compilerPath = compilerPath;
    }

    public String getExecutableName() {
        return executableName;
    }

    public void setExecutableName(String executableName) {
        this.executableName = executableName;
    }

    public String getExecutionPath() {
        return executionPath;
    }

    public void setExecutionPath(String executionPath) {
        this.executionPath = executionPath;
    }

    public List<String> getExternals() {
        return externals;
    }

    public void setExternals(List<String> externals) {
        this.externals = externals;
    }

    public String getProjectFilePath() {
        return project_filepath.toString().replace("\\", "/");
    }

    public void setProjectFilepath(String fn) {
        this.project_filepath = Paths.get(fn);
    }

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.contains(" ")) {
            name = name.replaceAll(" ", "");
        }
        this.name = name;
    }

    public String getObjectPath() {
        return "obj/";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getSource() {
        if (source.contains("changelog.txt"))
            source.remove("changelog.txt");
        return source;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }

    public boolean isPrintLog() {
        return printLog;
    }

    public void setPrintLog(boolean printLog) {
        this.printLog = printLog;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void save() throws IOException {

        File file = project_filepath.toFile();
        if (!file.exists()) {
            new File(path).mkdirs();
            new File(path + "changelog.txt").createNewFile();
            file.createNewFile();
        }

        save_to_xml(file);
    }
}
