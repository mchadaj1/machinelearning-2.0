package com.example.restControllers;

import com.example.entities.ProblemConfiguration;
import com.example.services.ProblemConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Kontroler pozwalający na zarządzanie konfiguracjami problemu przez protokół HTTP.
 * Created by mateusz on 05.03.16.
 */

@RestController
@RequestMapping("/services/problem_configuration")
public class ProblemConfigurationRestController {

    private final ProblemConfigurationRepository problem_configurationRepository;

    /**
     * Konstruktor klasy zarządzającej konfiguracjami problemu.
     * @param problem_configurationRepository Repozytorium konfiguracji problemów.
     */
    @Autowired
    public ProblemConfigurationRestController(ProblemConfigurationRepository problem_configurationRepository) {
        this.problem_configurationRepository = problem_configurationRepository;
    }

    /**
     * Funkcja dostarcza listę konfiguracji problemów.
     * @return Lista konfiguracji problemów oraz status HTTP.
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ResponseEntity<List<ProblemConfiguration>> index(){
        List<ProblemConfiguration>problem_configurations = problem_configurationRepository.findAll();
        if(problem_configurations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(problem_configurations,HttpStatus.OK);
    }

    /**
     * Funkcja dostarcza konfigurację problemu.
     * @param id Id konfiguracji.
     * @return Konfiguracja problemu oraz status HTTP.
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<ProblemConfiguration> get(@PathVariable Long id)
    {
        ProblemConfiguration problem_configuration = problem_configurationRepository.findById(id);
        if(problem_configuration == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(problem_configuration,HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na aktualizację danych konfiguracji problemu przez protokół HTTP.
     * @param id Id konfiguracji.
     * @param problem_configuration Konfiguracja problemu.
     * @return Konfiguracja problemu oraz status HTTP.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProblemConfiguration> update(@PathVariable Long id, @RequestBody ProblemConfiguration problem_configuration)
    {
        ProblemConfiguration currentProblem_configuration = problem_configurationRepository.findById(id);
        if(currentProblem_configuration==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentProblem_configuration.setName(problem_configuration.getName());
        problem_configurationRepository.save(currentProblem_configuration);
        return new ResponseEntity<>(problem_configuration,HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na usunięcie konfiguracji problemu przez protokół HTTP.
     * @param id Id konfiguracji.
     * @return Dane konfiguracji oraz status HTTP.
     */
    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<ProblemConfiguration> delete(@PathVariable Long id)
    {
        ProblemConfiguration problem_configuration = problem_configurationRepository.findById(id);
        if(problem_configuration==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        problem_configurationRepository.delete(id);
        return new ResponseEntity<>(problem_configuration, HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na dodanie konfiguracji problemu.
     * @param problem_configuration Konfiguracja problemu.
     * @return Dodana konfiguracja problemu oraz status HTTP.
     */
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ResponseEntity<ProblemConfiguration> create(@RequestBody(required = false) ProblemConfiguration problem_configuration)
    {
        if(problem_configuration==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        problem_configurationRepository.save(problem_configuration);
        return new ResponseEntity<>(problem_configuration,HttpStatus.OK);
    }

    /**
     * Funkcja obsługuje wyjątki związane z błędem bazy danych.
     * @param e Wyjątek.
     * @return Status Bad Request.
     */
    @ExceptionHandler
    @ResponseBody
    private ResponseEntity<ProblemConfiguration> handleException(DataAccessException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
