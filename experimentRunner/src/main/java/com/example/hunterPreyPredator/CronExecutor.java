package com.example.hunterPreyPredator;

import com.example.hunterPreyPredator.exceptions.NoHunterClassException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mateusz on 27.04.17.
 */
public class CronExecutor {

    public void runExperiment(String classFolderName) {
        OutputStream loggingStream = null;
        try {
            loggingStream = new FileOutputStream(classFolderName);
            Class<?> hunterClass = InlineCompiler.getAgent(getReplacesMap(), loggingStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoHunterClassException e) {
            e.printStackTrace();
        }

    }
    public Map<String, String> getReplacesMap() {
        return new HashMap<>();
    }
}
