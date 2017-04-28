package com.example.restControllers;

import com.example.entities.ProblemParamValue;
import com.example.services.ProblemParamValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler pozwalający na zarządzanie wartościami parametrów problemów.
 * Created by Mateusz on 2016-03-03.
 */
@RestController
@RequestMapping("/services/problem_param_value")
public class ProblemParamValueRestController {


    private final ProblemParamValueRepository problem_param_valueRepository;

    /**
     * Konstruktor kontrolera restowego wartości parametrów problemów.
     * @param problem_param_valueRepository Repozytorium wartości parametrów problemów.
     */
    @Autowired
    public ProblemParamValueRestController(ProblemParamValueRepository problem_param_valueRepository) {
        this.problem_param_valueRepository = problem_param_valueRepository;
    }

    /**
     * Funkcja dostarcza wartość parametru problemu.
     * @param configuration_id Id konfiguracji.
     * @param param_id Id parametru.
     * @return Wartość parametru oraz status HTTP.
     */
    @RequestMapping(value = "/{configuration_id}/{param_id}",method = RequestMethod.GET)
    public ResponseEntity<ProblemParamValue> get(@PathVariable Long configuration_id, @PathVariable Long param_id)
    {
        ProblemParamValue value = problem_param_valueRepository.findByProblemParamIdAndProblemConfigurationId(param_id,configuration_id);
            return new ResponseEntity<>(value,HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na aktualizację wartości parametru problemu.
     * @param configuration_id Id konfiguracji.
     * @param param_id Id parametru.
     * @param value Nowa wartość.
     * @return Wartość parametru problemu oraz status HTTP.
     */
    @RequestMapping(value = "/{configuration_id}/{param_id}",method = RequestMethod.PUT)
    public ResponseEntity<ProblemParamValue> update(@PathVariable Long configuration_id, @PathVariable Long param_id, @RequestBody String value){
        ProblemParamValue paramValue;
        ProblemParamValue existingParamValue = problem_param_valueRepository.findByProblemParamIdAndProblemConfigurationId(param_id,configuration_id);
        if(existingParamValue==null) {
            paramValue = problem_param_valueRepository.save(new ProblemParamValue(param_id, configuration_id, value));
        } else {
            existingParamValue.setValue(value);
            paramValue = problem_param_valueRepository.save(existingParamValue);
        }
        return new ResponseEntity<>(paramValue,HttpStatus.OK);
    }

    /**
     * Funkcja obsługuje wyjątki związane z błędem bazy danych.
     * @param e Wyjątek.
     * @return Status Bad Request.
     */
    @ExceptionHandler
    @ResponseBody
    private ResponseEntity<ProblemParamValue> handleException(DataAccessException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
