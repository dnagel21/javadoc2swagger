package com.spirit21.swagger.converter.loader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.github.javaparser.ParseException;
import com.google.inject.internal.util.Strings;
import com.spirit21.swagger.converter.Regex;
import com.spirit21.swagger.converter.models.JavaFile;
import com.spirit21.swagger.converter.models.Method;

import util.NoCommentJavacodeParser;

/**
 * Provides methods to load java files from a Maven project
 * 
 * @author dsimon
 *
 */
public class JavaFileLoader extends AbstractLoader {
	
	private ClassJavadocLoader classJavadocLoader;
	private ClassAnnotationLoader classAnnotationLoader;
	private MethodLoader methodLoader;
	private ApiJavadocLoader apiJavadocLoader;
	private ImportLoader importLoader;
	
    public JavaFileLoader(Log log) {
        super(log);
    }

    /**
     * Loads all java files from a Maven project and writes each of them to a
     * list of {@link JavaFile} objects
     * 
     * @param project
     *            Maven Project
     * @return List of java files
     * @throws JavaFileLoadException
     *             An error occured while loading the Java files
     * @throws ParseException 
     */
    public List<JavaFile> getJavaFiles(MavenProject project) throws JavaFileLoadException, ParseException {
        try {
            List<Path> javaSourceFiles = new ArrayList<>();
            List<?> rootDirectories = project.getCompileSourceRoots();
            for (Object raw : rootDirectories) {
                String rootDirectory = String.class.cast(raw);
                javaSourceFiles.addAll(getJavaFileNames(Paths.get(rootDirectory)));
            }
            return getInformationFromJavaFiles(javaSourceFiles);
        } catch (IOException ex) {
            throw new JavaFileLoadException("Error while loading the java files!", ex);
        }
    }

    /**
     * Finds all pathes to Java files in the root directory recursively and
     * writes them to a list
     * 
     * @param fileNames
     *            list of pathes
     * @param dir
     *            root directory
     * @return list of pathes
     * @throws IOException
     */
    private List<Path> getJavaFileNames(Path dir) throws IOException {
        List<Path> fileNames = new ArrayList<>();
        DirectoryStream<Path> stream = null;
        try {
            stream = Files.newDirectoryStream(dir);
            for (Path path : stream) {
                if (path.toFile().isDirectory()) {
                    fileNames.addAll(getJavaFileNames(path));
                } else if (path.getFileName().toString().endsWith(".java")) {
                    fileNames.add(path);
                }
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return fileNames;
    }

    /**
     * create Loader structure
     */
    private void createLoaders() {
    	this.classJavadocLoader = new ClassJavadocLoader(log);
    	this.classAnnotationLoader = new ClassAnnotationLoader(log);
    	this.methodLoader = new MethodLoader(log);
    	this.apiJavadocLoader = new ApiJavadocLoader(log);
    	this.importLoader = new ImportLoader(log);
    }
    
	/**
	 * Reads the given Java files and writes javadoc, annotations and imports for
	 * each file to an object
	 * 
	 * @param files
	 *            pathes to Java files
	 * @return list of list of strings
	 * @throws IOException
	 * @throws ParseException 
	 */
	private List<JavaFile> getInformationFromJavaFiles(List<Path> files) throws IOException, ParseException {
		createLoaders();
//		eraseComments(files);
//		String withoutComments = "";
//		for (Map.Entry<String, String> entry : eraseComments(files).entrySet()) {
//			withoutComments += entry.getValue() + "\n" + entry.getKey(); 
//		}
//		log.info(withoutComments); // <- XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		
		List<JavaFile> javaFiles = new ArrayList<>();

		for (Path file : files) {

			String fileString = NoCommentJavacodeParser.parseToJavaCodeWithJavaDoc(file);

			if (ignoreJavaFile(fileString)) {
                continue;
            }

            JavaFile javaFile = new JavaFile();
            String packageName = getPackageNameFromFile(fileString);
            List<String> imports = importLoader.getImportsFromFile(fileString);
            List<Method> methods = methodLoader.getMethodsFromJavaFile(fileString);
            String apiJavadoc = apiJavadocLoader.getApiJavadocFromJavaFile(fileString);
            List<String> classAnnotations = classAnnotationLoader.getClassAnnotationsFromJavaFile(fileString);
            String classJavadoc = classJavadocLoader.getClassJavadocFromJavaFile(fileString);
            if (imports != null) {
                javaFile.setImports(imports);
            }
            if (classJavadoc != null) {
                javaFile.setClassJavadoc(removeJavadocCharactersFromString(classJavadoc));
            }
            if (classAnnotations.isEmpty()) {
                javaFile.setClassAnnotations(classAnnotations);
            }
            if (apiJavadoc != null) {
                javaFile.setApiJavadoc(removeJavadocCharactersFromString(apiJavadoc));
            }
            javaFile.setPackageName(packageName);
            javaFile.setFileName(file.getFileName().toString());
            javaFile.setFunctions(methods);
            javaFiles.add(javaFile);
        }
        return javaFiles;
    }

    /**
     * Returns true, if the given fileString contains the regex for ignoring
     * javafile
     * 
     * @param fileString
     *            the class as file string
     * @return true, if the given fileString contains the regex for ignoring
     *         javafile
     */
    private boolean ignoreJavaFile(String fileString) {

        Pattern pattern = Pattern.compile(Regex.IGNORE_JAVAFILE);
        Matcher matcher = pattern.matcher(fileString);
        return matcher.find();
    }

    /**
     * Gets the package name of a java file
     * 
     * @param fileString
     *            file as string
     * @return package or null
     */
    private String getPackageNameFromFile(String fileString) {
    	
        Pattern pattern = Pattern.compile(Regex.PACKAGE);
        Matcher matcher = pattern.matcher(fileString);
        if (matcher.find()) {
            return fileString.substring(matcher.start() + 8, matcher.end() - 1);
        }
        return null;
    }

    /**
     * Converts the content of a file from line by line to a single string.
     * 
     * @param file
     * @return string
     */
    private String stringWithoutNewLines(String file) {
    	String[] byLine = file.split("\\n");
    	String ret = "";
        for (String line : byLine) {
            ret = ret.concat(line + " ");
        }
        return ret;
    }
    
//    /**
//     * Creates a Hashmap that maps each Method to its javadoc comments
//     * @param files
//     * @return
//     * @throws IOException
//     * @throws ParseException
//     */
//    public Map<String, String> eraseComments(List<Path> files) throws IOException, ParseException {
//    	Map<String, String> map = new HashMap<>();
//    	
//    	for (Path file : files) {
//    		String fileString = fileAsString(Files.readAllLines(file));
//    		
//    			String noComment = NoCommentJavacodeParser
//    					.parse(new String(Files.readAllBytes(file), StandardCharsets.UTF_8));
//  
//    		String classJavadoc = classJavadocLoader.getClassJavadocFromJavaFile(fileString);
//    		map.put(noComment.toString(), classJavadoc);
////   		for (Map.Entry<String, String> entry : map.entrySet()) {
////   		    log.info("key=" + entry.getKey() + ", value=" + entry.getValue());
////    		}
//    	}
//		return map;
//    }
}
