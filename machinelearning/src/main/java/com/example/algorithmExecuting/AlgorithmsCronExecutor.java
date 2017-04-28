package com.example.algorithmExecuting;

import com.example.hunterPreyPredator.ExperimentRunner;
import com.example.entities.*;
import com.example.hunterPreyPredator.exceptions.NoHunterClassException;
import com.example.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Klasa obsługująca moduł Cron Task
 */
@Service("algorithmExecutor")
public class AlgorithmsCronExecutor {

    private final AlgorithmExecutionRepository algorithmExecutionRepository;
    private final ProblemConfigurationRepository problemConfigurationRepository;
    private final ProblemParamValueRepository problemParamValueRepository;
    private final MethodParamValueRepository methodParamValueRepository;
    private final StatisticRepository statisticRepository;
    private final MethodConfigurationRepository methodConfigurationRepository;

    private OutputStream loggingStream;
    private AlgorithmExecution algorithmExecution;

    @Autowired
    public AlgorithmsCronExecutor(AlgorithmExecutionRepository algorithmExecutionRepository,
                                  ProblemConfigurationRepository problemConfigurationRepository,
                                  ProblemParamValueRepository problemParamValueRepository,
                                  MethodParamValueRepository methodParamValueRepository,
                                  StatisticRepository statisticRepository,
                                  MethodConfigurationRepository methodConfigurationRepository) {
        this.algorithmExecutionRepository = algorithmExecutionRepository;
        this.problemConfigurationRepository = problemConfigurationRepository;
        this.problemParamValueRepository = problemParamValueRepository;
        this.methodParamValueRepository = methodParamValueRepository;
        this.statisticRepository = statisticRepository;
        this.methodConfigurationRepository = methodConfigurationRepository;
    }

    /**
     * Funkcja wybierająca symulacje do wykonania z bazy danych, tworzy plik do logowania przebiegu
     * dla użytkownika i uruchamia eksperymenty.
     */
    protected void lookForPendingTask() {

        List<AlgorithmExecution> algorithmExecutions = algorithmExecutionRepository.findByPendingTrueAndCompletedFalse();
        if(!algorithmExecutions.isEmpty()) {
            try {

                algorithmExecution = algorithmExecutions.get(0);
                String filename = new BigInteger(256, new Random()).toString(32).substring(2,20)+".txt";
                loggingStream = getOutputStreamForLogging(filename);
                setAlgorithmPending(filename);
                logExecutionStarted();
                try {
                    Class<?> hunterClass = InlineCompiler.getAgent(getReplacesMap(), loggingStream);
                    runExperiments(getProblemAttributes(), getMethodAttributes(), hunterClass);
                    finishWithSuccess();
                    removeTemporaryFiles(hunterClass.getName());
                } catch (NumberFormatException e) {
                        log("Nie podano liczby eksperymentów do wykonania\n");
                        loggingStream.close();
                        finish();
                } catch (NoHunterClassException e) {
                        log("Nie udało się utworzyć klasy łowcy\n");
                        loggingStream.close();
                        finish();
                }
            } catch(IOException e) {
                System.out.println("Wystąpił problem z dostępem do pliku log!");
                e.printStackTrace();
            }
    }}

    /**
     * Funkcja tworzy OutputStream do logowania przebiegu wykonania dla użytkownika w pliku o podanej nazwie.
     * @param fileName Nazwa nowo utworzonego pliku.
     * @return OutputStream do logowania.
     */
    private OutputStream getOutputStreamForLogging(String fileName) throws IOException {
            Path path = Files.createFile(Paths.get("executionslogs/" + fileName));
            return Files.newOutputStream(path);
    }

    /**
     * Funkcja wczytuje atrybuty problemu.
     * @return Mapa atrybutów.
     * @throws IOException W przypadku wystąpienia problemu z outputstreamem do logowania.
     */
    private Map<String, String> getProblemAttributes() throws IOException {
        Map<String, String> attributes = new HashMap<>();

        ProblemConfiguration problemConfiguration = problemConfigurationRepository
                .findById(algorithmExecution.getProblemConfigurationId());
        List<ProblemParamValue> problemParamValues = problemParamValueRepository
                .findByProblemConfigurationId(problemConfiguration.getId());

        log("Argumenty problemu:\n");
        for(ProblemParamValue problemParamValue : problemParamValues) {
            log((problemParamValue.getProblem_param()
                    .getName() + " " + problemParamValue.getValue() + "\n"));
            attributes.put(problemParamValue.getProblem_param().getName(),problemParamValue.getValue());
        }
        return attributes;
    }

    /**
     * Funkcja wczytuje atrybuty metody.
     * @return Mapa atrybutów.
     * @throws IOException W przypadku wystąpienia problemu z outputstreamem do logowania.
     */
    private Map<String, String> getMethodAttributes() throws IOException {
        Map<String, String> attributes = new HashMap<>();

        List<MethodParamValue> methodParamValues = methodParamValueRepository
                .findByMethodConfigurationId(algorithmExecution.getMethodConfigurationId());
        log("Argumenty metody:\n");
        for (MethodParamValue methodParamValue : methodParamValues) {
            log((methodParamValue.getMethod_param().getName() + " " + methodParamValue.getValue() + "\n"));
            attributes.put(methodParamValue.getMethod_param().getName(), methodParamValue.getValue());
        }
        return attributes;
    }

    /**
     * Funkcja wczytuje mapę znaczników do zamiany w pliku z klasą agenta.
     * @return Mapa atrybutów.
     */
    private Map<String, String> getReplacesMap() {
        MethodConfiguration methodConfiguration = methodConfigurationRepository
                .findById(algorithmExecution.getMethodConfigurationId());
        Method method = methodConfiguration.getMethod();
        Map<String, String> replaces = new HashMap<>();
        replaces.put("#Globals",method.getGlobals());
        replaces.put("#nextStepMethod",method.getCode());
        replaces.put("#Imports",method.getImports());
        replaces.put("#Constructor",method.getConstructorcode());
        replaces.put("#finishGame",method.getFinishgame());
        return replaces;
    }

    /**
     * Funkcja zapisuje statystyki w bazie danych.
     * @param expNumber numer eksperymentu.
     * @param statistics lista statystyk.
     */
    private void saveStatistics(int expNumber, List<Statistic> statistics) {

        final int experimentNumber = expNumber;
        statistics.stream()
                .forEach(b -> b.setExperiment_number(experimentNumber));
        statistics.stream()
                .forEach(b -> b.setExecutionId(algorithmExecution.getId()));

        statisticRepository.save(statistics);
    }

    /**
     * Funkcja wywołuje zapisanie zakończenia działania algorytmu oraz loguje o zakończeniu
     * i zamyka outputStream do logowania.
     * @throws IOException W przypadku wystąpienia problemu z outputstreamem do logowania.
     */
    private void finishWithSuccess() throws IOException {
        finish();
        log("Zakończono wykonywanie algorytmu\n");
        loggingStream.close();
    }

    /**
     * Funkcja zapisuje zakończenie działania algorytmu.
     */
    private void finish() {
        algorithmExecution.setCompleted(true);
        algorithmExecutionRepository.save(algorithmExecution);
    }

    /**
     * Funkcja zmienia status wykonania algorytmu na oczekujący oraz ustawia nazwę pliku z logiem przebiegu.
     * @param fileName nazwa pliku.
     */
    private void setAlgorithmPending(String fileName) {
        algorithmExecution.setPending(false);
        algorithmExecution.setFilename(fileName);
        algorithmExecutionRepository.save(algorithmExecution);
    }

    /**
     * Funkcja zapisuje do logu informację o rozpoczęciu wykonywania algorytmu i ładowania argumentów.
     * @throws IOException W przypadku wystąpienia problemu z outputstreamem do logowania.
     */
    private void logExecutionStarted() throws IOException {
        log(("Wybrano polecenie wykonania algorytmu o id "
                + algorithmExecution.getId().toString() + "\n"));
        log("Rozpoczęto ładowanie argumentów: \n");
    }

    /**
     * Funkcja uruchamia wykonanie algorytmu.
     * @param attributes Atrybuty problemu.
     * @param methodAttributes Atrybuty metody.
     * @param hunterClass Klasa łowcy
     * @throws IOException W przypadku wystąpienia problemu z outputstreamem do logowania.
     */
    private void runExperiments(Map<String, String> attributes, Map<String, String> methodAttributes,
                                Class hunterClass) throws IOException, NoHunterClassException {
        int experiments = Integer.parseInt(attributes.get("experiments"));
        ExperimentRunner experimentRunner;
        for(int i = 1; i <= experiments; i++) {
            experimentRunner = new ExperimentRunner(attributes, methodAttributes, hunterClass, loggingStream);
            try {
                experimentRunner.init();
                experimentRunner.run();
                log("Zakończono wykonywanie eksperymentu nr " + i + "\n");
                saveStatistics(i, experimentRunner.getStatistics());
            } catch (IllegalAccessException | InstantiationException e) {
                log(e.getStackTrace().toString());
                finish();
            } catch(NumberFormatException ex) {
                log("Wystąpił problem z wartością parametru\n");
            }
        }
    }

    /**
     * Funkcja zapisuje podany string do logu.
     * @param string String do zapisania
     * @throws IOException W przypadku wystąpienia problemu z outputstreamem do logowania.
     */
    private void log(String string) throws IOException {
        loggingStream.write(string.getBytes());
    }
    /**
     * Funkcja usuwa pliki utworzone w celu stworzenia klasy.
     * @param temporaryName Nazwa klasy tymczasowej.
     * @throws IOException
     */
    private static void removeTemporaryFiles(String temporaryName) throws IOException {
        Files.walkFileTree(Paths.get("methodagents/"), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if(file.getFileName().toString().startsWith(temporaryName)) {
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}