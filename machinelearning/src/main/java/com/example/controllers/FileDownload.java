package com.example.controllers;

import com.example.entities.AlgorithmExecution;
import com.example.services.AlgorithmExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Kontroler serwujący użytkownikowi plik z opisem przebiegu symulacji
 * Created by mateusz on 18.05.16.
 */
@Controller
public class FileDownload {

    @Autowired
    AlgorithmExecutionRepository algorithmExecutionRepository;

    @RequestMapping(value = "/files/{id}", method = RequestMethod.GET)
    public void getFile(@PathVariable("id") Long id, HttpServletResponse response) {
        try {

            AlgorithmExecution algorithmExecution = algorithmExecutionRepository.findById(id);
            String fileName = algorithmExecution.getFilename();
            Files.copy(Paths.get("executionslogs/" + fileName),response.getOutputStream());
            response.flushBuffer();
            response.setContentType("text/plain; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=logs.txt");
            response.setCharacterEncoding("UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
