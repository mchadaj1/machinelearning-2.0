package com.example.restControllers;

import com.example.entities.ProblemParam;
import com.example.services.ProblemParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Klasa pozwalająca na zarządzanie parametrami problemów przez protokół HTTP.
 * Created by Mateusz on 2016-03-03.
 */
@RestController
@RequestMapping("/services/problem_param")
public class ProblemParamRestController {


    private final ProblemParamRepository problem_paramRepository;

    /**
     * Konstruktor kontrolera restowego parametrów problemów.
     * @param problem_paramRepository Repozytorium parametrów problemów.
     */
    @Autowired
    public ProblemParamRestController(ProblemParamRepository problem_paramRepository) {
        this.problem_paramRepository = problem_paramRepository;
    }

    /**
     * Funkcja dostarczająca parametr problemu przez protokół HTTP.
     * @param id Id parametru.
     * @return Parametr problemu oraz status HTTP.
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<List<ProblemParam>> index(@PathVariable Long id){
        List<ProblemParam> problems =problem_paramRepository.findByProblemsid(id);
        if(problems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(problems,HttpStatus.OK);
    }

    /**
     * Funkcja pozwalająca na usunięcie parametru metody przez protokół HTTP.
     * @param id Id metody.
     * @return Metoda oraz status HTTP.
     */
    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<ProblemParam> delete(@PathVariable Long id)
    {
        ProblemParam problem_param = problem_paramRepository.findById(id);
        if(problem_param==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        problem_paramRepository.delete(problem_param);
        return new ResponseEntity<>(problem_param,HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na dodanie parametru metody.
     * @param problem_param Parametr metody.
     * @return Parametr metody oraz status HTTP.
     */
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ResponseEntity<ProblemParam> create(@RequestBody(required = false) ProblemParam problem_param)
    {
        if(problem_param==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        problem_paramRepository.save(problem_param);
        return new ResponseEntity<>(problem_param,HttpStatus.OK);

    }

    /**
     * Funkcja obsługuje wyjątki związane z błędem bazy danych.
     * @param e Wyjątek.
     * @return Status Bad Request.
     */
    @ExceptionHandler
    @ResponseBody
    private ResponseEntity<ProblemParam> handleException(DataAccessException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
