package com.example.restControllers;

import com.example.entities.MethodConfiguration;
import com.example.services.MethodConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler dostarczający dane konfiguracji (wariantów) metod przez protokół HTTP.
 * Created by mateusz on 05.03.16.
 */

@RestController
@RequestMapping("/services/method_configuration")
public class MethodConfigurationRestController {


    private final MethodConfigurationRepository method_configurationRepository;

    /**
     * Konstruktor kontrolera konfiguracji metod.
     * @param method_configurationRepository Repozytorium konfiguracji metod.
     */
    @Autowired
    public MethodConfigurationRestController(MethodConfigurationRepository method_configurationRepository) {
        this.method_configurationRepository = method_configurationRepository;
    }

    /**
     * Funkcja dostarcza listę konfiguracji metod.
     * @return Lista konfiguracji (wariantów) metod oraz status.
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ResponseEntity<List<MethodConfiguration>> index(){
        List<MethodConfiguration>method_configurations = method_configurationRepository.findAll();
        if (method_configurations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(method_configurations,HttpStatus.OK);
    }

    /**
     * Funkcja dostarcza dane jednej konfiguracji metody.
     * @param id Id konfiguracji.
     * @return Dane konfiguracji metody oraz status HTTP.
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<MethodConfiguration> get(@PathVariable Long id)
    {
        MethodConfiguration method_configuration = method_configurationRepository.findById(id);
        if(method_configuration == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(method_configuration, HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na zaktualizowanie danych konfiguracji metody przez protokół HTTP.
     * @param id Id konfiguracji.
     * @param method_configuration Dane konfiguracji metody.
     * @return Zaktualizowana konfiguracja metody oraz status HTTP.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MethodConfiguration> update(@PathVariable Long id, @RequestBody MethodConfiguration method_configuration)
    {
        MethodConfiguration currentMethod_configuration = method_configurationRepository.findById(id);
        if(currentMethod_configuration==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentMethod_configuration.setName(method_configuration.getName());
        method_configurationRepository.save(currentMethod_configuration);
        return new ResponseEntity<>(method_configuration, HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na usunięcie konfiguracji metody przez protokół HTTP.
     * @param id Id konfiguracji.
     * @return Usunięta konfiguracja oraz status HTTP.
     */
    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<MethodConfiguration> delete(@PathVariable Long id)
    {
        MethodConfiguration method_configuration = method_configurationRepository.findById(id);
        if(method_configuration==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        method_configurationRepository.delete(id);
        return new ResponseEntity<>(method_configuration, HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na dodanie nowej konfiguracji metody przez protokół HTTP.
     * @param method_configuration Konfiguracja metody.
     * @return Dodana konfiguracja oraz status HTTP.
     */
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ResponseEntity<MethodConfiguration> create(@RequestBody(required = false) MethodConfiguration method_configuration)
    {
        if(method_configuration==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        method_configurationRepository.save(method_configuration);
        return new ResponseEntity<>(method_configuration, HttpStatus.OK);
    }

    /**
     * Funkcja obsługuje wyjątki związane z błędem bazy danych.
     * @param e Wyjątek.
     * @return Status Bad Request.
     */
    @ExceptionHandler
    @ResponseBody
    private ResponseEntity<MethodConfiguration> handleException(DataAccessException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
