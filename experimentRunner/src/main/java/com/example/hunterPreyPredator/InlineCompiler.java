package com.example.hunterPreyPredator;

/**
 * Created by mateusz on 18.05.16.
 */

import com.example.hunterPreyPredator.exceptions.NoHunterClassException;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;

/**
 * Klasa kompilująca w locie klasę na podstawie kodu.
 * http://stackoverflow.com/questions/21544446/how-do-you-dynamically-compile-and-load-external-java-classes
 */

public class InlineCompiler {

    public static Class<?> getAgent(Map<String, String> replaces, OutputStream outputStream) throws NoHunterClassException {
        String templateFileName = new BigInteger(256,new Random()).toString(32).substring(2,27);
        templateFileName = "a"+templateFileName;
        prepareMethodFile(templateFileName, replaces, outputStream);
        File fileToCompile = new File("methodagents/" + templateFileName + ".java");


        if (fileToCompile.getParentFile().exists() || fileToCompile.getParentFile().mkdirs()) {
            try {
                /** Compilation Requirements *********************************************************************************************/
                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

                // This sets up the class path that the compiler will use.
                // I've added the .jar file that contains the DoStuff interface within in it...
                List<String> optionList = new ArrayList<String>();
                optionList.add("-classpath");
                optionList.add(System.getProperty("java.class.path") + ";dist/AgentBase.jar" +";dist/Position.jar");
                Iterable<? extends JavaFileObject> compilationUnit
                        = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(fileToCompile));
                JavaCompiler.CompilationTask task = compiler.getTask(
                        null,
                        fileManager,
                        diagnostics,
                        optionList,
                        null,
                        compilationUnit);
                /********************************************************************************************* Compilation Requirements **/
                if (task.call()) {
                    /** Load and execute *************************************************************************************************/

                    // Create a new custom class loader, pointing to the directory that contains the compiled
                    // classes, this should point to the top of the package structure!
                    URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("methodagents/").toURI().toURL()});

                    // Load the class from the classloader by name....
                    Class<?> loadedClass = classLoader.loadClass(templateFileName);
                    return loadedClass;

                    /************************************************************************************************* Load and execute **/
                } else {
                    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                        outputStream.write(("Error on line " + diagnostic.getLineNumber() + " in " + diagnostic.getSource().toUri() + "\n").getBytes());
                    }
                }
                fileManager.close();
            } catch (IOException | ClassNotFoundException exp) {
                exp.printStackTrace();
                try {
                    outputStream.write(exp.getStackTrace().toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                throw new NoHunterClassException();
            }
        }
        throw new NoHunterClassException();
    }

    /**
     * Funkcja przygotowuje plik tymczasowy do wygenerowania klasy
     * @param templateFileName nazwa pliku
     * @param replaces mapa zmiennych do zmiany w pliku
     * @param outputStream OutputStream do pliku z logiem
     * @throws NoHunterClassException w przypadku, gdy nie można utworzyć klasy Huntera.
     */
    private static void prepareMethodFile(String templateFileName, Map<String, String> replaces,
                                          OutputStream outputStream) throws NoHunterClassException {
        try {

            Path agentSchema = Paths.get("methodagents/AgentSchema.java");
            String fileContent = new String(Files.readAllBytes(agentSchema));
            fileContent = fileContent.replace("#ClassName", templateFileName);
            for(Map.Entry<String, String> entry : replaces.entrySet()) {
                if(entry.getValue() != null) {
                    fileContent = fileContent.replace(entry.getKey(), entry.getValue());
                } else {
                    fileContent = fileContent.replace(entry.getKey(), " ");
                }
            }
            OutputStream file = Files.newOutputStream(Paths.get("methodagents/" + templateFileName + ".java"),CREATE);
            file.write(fileContent.getBytes());
            outputStream.write(("wygenerowany kod klasy:\n" + fileContent).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new NoHunterClassException();
        }
    }


}