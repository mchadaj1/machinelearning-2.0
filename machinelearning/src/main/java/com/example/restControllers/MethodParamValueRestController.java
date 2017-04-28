package com.example.restControllers;

import com.example.entities.MethodParamValue;
import com.example.services.MethodParamValueRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler pozwalający na zarządzanie danymi wartości parametrów metod przez protokół HTTP.
 * Created by Mateusz on 2016-03-03.
 */
@RestController
@RequestMapping("/resources/methods/configurations/")
public class MethodParamValueRestController {


    private final MethodParamValueRepository method_param_valueRepository;

    /**
     * Konstruktor klasy udostępniającej wartości parametrów metody przez protokół HTTP.
     * @param method_param_valueRepository Repozytorium wartości parametrów metod.
     */
    @Autowired
    public MethodParamValueRestController(MethodParamValueRepository method_param_valueRepository) {
        this.method_param_valueRepository = method_param_valueRepository;
    }

    /**
     * Funkcja dostarcza wartość parametru metody przez protokół HTTP.
     * @param configuration_id Id konfiguracji metody.
     * @param param_id Id parametru metody.
     * @return Wartość parametru metody.
     */
    @RequestMapping(value = "/{configuration_id}/params/{param_id}/value",method = RequestMethod.GET)
    public ResponseEntity<MethodParamValue> get(@PathVariable Long configuration_id, @PathVariable Long param_id) {

        MethodParamValue value = method_param_valueRepository.findByMethodParamIdAndMethodConfigurationId(param_id,
                configuration_id);
        if (value == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(value,HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na aktualizację wartości parametrów metod przez protokół HTTP.
     * @param configuration_id Id konfiguracji.
     * @param param_id Id parametru.
     * @param value Nowa wartość.
     * @return Zaktualizowana wartość parametru oraz status HTTP.
     */
    @RequestMapping(value = "/{configuration_id}/params/{param_id}/value",method = RequestMethod.PUT)
    public ResponseEntity<MethodParamValue> update(@PathVariable Long configuration_id, @PathVariable Long param_id,
                                                   @RequestBody String value){
        MethodParamValue newParamValue;
        MethodParamValue existingParamValue = method_param_valueRepository.findByMethodParamIdAndMethodConfigurationId(param_id,
                configuration_id);
        if(existingParamValue==null) {
            newParamValue = method_param_valueRepository.save(new MethodParamValue(param_id, configuration_id, value));
        } else {
            existingParamValue.setValue(value);
            newParamValue = method_param_valueRepository.save(existingParamValue);
        }
        return new ResponseEntity<>(newParamValue, HttpStatus.OK);
    }

    /**
     * Funkcja obsługuje wyjątki związane z błędem bazy danych.
     * @param e Wyjątek.
     * @return Status Bad Request.
     */
    @ExceptionHandler
    @ResponseBody
    private ResponseEntity<MethodParamValue> handleException(DataAccessException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
