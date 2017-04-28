package com.example.restControllers;

import com.example.entities.Problem;
import com.example.services.ProblemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Kontroler restowy pozwalający na zarządzanie problemami (zadaniami) przez protokół HTTP.
 * Created by Mateusz on 2016-03-03.
 */
@RestController
@RequestMapping("/services/problem")
public class ProblemsRestController {


    private final ProblemsRepository problemsRepository;

    /**
     * Konstruktor kontrolera problemów.
     * @param problemsRepository Repozytorium problemów.
     */
    @Autowired
    public ProblemsRestController(ProblemsRepository problemsRepository) {
        this.problemsRepository = problemsRepository;
    }

    /**
     * Funkcja dostarcza listę problemów (zadań) przez protokół HTTP.
     * @return Lista zadań oraz status HTTP.
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ResponseEntity<List<Problem>> index() {
        List<Problem> problems =problemsRepository.findAll();
        if(problems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(problems, HttpStatus.OK);
    }

    /**
     * Funkcja dostarcza problem o podanym Id przez protokół HTTP.
     * @param id Id problemu.
     * @return Problem oraz status HTTP.
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Problem> get(@PathVariable Long id) {
        Problem problem = problemsRepository.findById(id);
            if(problem == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(problem,HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na aktualizację danych problemu przez protokół HTTP.
     * @param id Id problemu.
     * @param problem Problem.
     * @return Problem oraz status HTTP.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Problem> update(@PathVariable Long id,@RequestBody Problem problem)
    {
        Problem currentProblem = problemsRepository.findById(id);
        if(currentProblem==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentProblem.setName(problem.getName());
        currentProblem.setDescription(problem.getDescription());
        problemsRepository.save(currentProblem);
        return new ResponseEntity<>(problem,HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na usunięcie problemu przez protokół HTTP.
     * @param id Id problemu.
     * @return Problem oraz status HTTP.
     */
    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Problem> delete(@PathVariable Long id) {
        Problem problem = problemsRepository.findById(id);
        if(problem==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        problemsRepository.delete(id);
        return new ResponseEntity<>(problem,HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na dodanie problemu przez protokół HTTP.
     * @param problem Problem.
     * @return Problem oraz status HTTP.
     */
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ResponseEntity<Problem> create(@RequestBody(required = false) Problem problem) {
        if(problem==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        problemsRepository.save(problem);
        return new ResponseEntity<>(problem,HttpStatus.OK);
    }

    /**
     * Funkcja obsługuje wyjątki związane z błędem bazy danych.
     * @param e Wyjątek.
     * @return Status Bad Request.
     */
    @ExceptionHandler
    @ResponseBody
    private ResponseEntity<Problem> handleException(DataAccessException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
