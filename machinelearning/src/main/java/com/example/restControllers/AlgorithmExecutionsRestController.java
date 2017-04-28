package com.example.restControllers;

import com.example.entities.AlgorithmExecution;
import com.example.services.AlgorithmExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler dostarczający dane wykonań algorytmów przez protokół HTTP.
 * Created by mateusz on 05.03.16.
 */
@RestController
@RequestMapping("/services/algorithm_execution")
public class AlgorithmExecutionsRestController {

    private final AlgorithmExecutionRepository algorithmExecutionRepository;

    /**
     * Konstruktor klasy wykonań algorytmów.
     * @param algorithmExecutionRepository Repozytorium wykonań algorytmów.
     */
    @Autowired
    public AlgorithmExecutionsRestController(AlgorithmExecutionRepository algorithmExecutionRepository) {
        this.algorithmExecutionRepository = algorithmExecutionRepository;
    }

    /**
     * Funkcja dostarcza listę wykonań algorytmów.
     * @return Lista wykonań algorytmów oraz status HTTP.
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ResponseEntity<List<AlgorithmExecution>> index() {
        List<AlgorithmExecution>algorithmExecutions =algorithmExecutionRepository.findAll();
        if(algorithmExecutions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(algorithmExecutions, HttpStatus.OK);
    }

    /**
     * Funkcja dostarcza dane jednego wykonania algorytmu.
     * @param id Id wykonania.
     * @return Dane wykonania algorytmu oraz status HTTP.
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<AlgorithmExecution> get(@PathVariable Long id)
    {
        AlgorithmExecution algorithmExecution = algorithmExecutionRepository.findById(id);
        if(algorithmExecution == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(algorithmExecution, HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na wprowadzenie nowego wykonania algorytmu przez protokół HTTP.
     * @param algorithmExecution Wykonanie algorytmu.
     * @return Wprowadzone dane oraz status HTTP.
     */
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ResponseEntity<AlgorithmExecution> create(@RequestBody(required = false) AlgorithmExecution algorithmExecution)
    {
        if(algorithmExecution==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        algorithmExecutionRepository.save(algorithmExecution);
        return new ResponseEntity<>(algorithmExecution, HttpStatus.OK);

    }

    /**
     * Funkcja obsługuje wyjątki związane z błędem bazy danych.
     * @param e Wyjątek.
     * @return Status Bad Request.
     */
    @ExceptionHandler
    @ResponseBody
    private ResponseEntity<AlgorithmExecution> handleException(DataAccessException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
