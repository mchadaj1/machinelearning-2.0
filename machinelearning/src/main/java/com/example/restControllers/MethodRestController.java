package com.example.restControllers;

import com.example.entities.Method;
import com.example.services.MethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Kontroler pozwalający na zarządzanie danymi metod przez protokół HTTP.
 * Created by mateusz on 05.03.16.
 */
@RestController
@RequestMapping("/services/method")
public class MethodRestController {


    private final MethodRepository methodRepository;

    @Autowired
    public MethodRestController(MethodRepository methodRepository) {
        this.methodRepository = methodRepository;
    }

    /**
     * Funkcja dostarcza listę metod przez protokół HTTP.
     * @return Lista metod oraz status HTTP.
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ResponseEntity<List<Method>> index(){
        List<Method>methods =methodRepository.findAll();
        if(methods.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(methods, HttpStatus.OK);
    }

    /**
     * Funkcja dostarcza metodę przez protokół HTTP.
     * @param id Id metody.
     * @return Metoda oraz status HTTP.
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Method> get( @PathVariable Long id)
    {
        Method method = methodRepository.findById(id);
        if(method == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(method, HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na aktualizację danych metody przez protokół HTTP.
     * @param id Id metody.
     * @param method Nowe dane metody.
     * @return Zaktualizowana metoda oraz status HTTP.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Method> update(@PathVariable Long id,@RequestBody Method method)
    {
        Method currentMethod = methodRepository.findById(id);
        if(currentMethod==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentMethod.setName(method.getName());
        currentMethod.setDescription(method.getDescription());
        currentMethod.setImports(method.getImports());
        currentMethod.setConstructorcode(method.getConstructorcode());
        currentMethod.setCode(method.getCode());
        currentMethod.setGlobals(method.getGlobals());
        currentMethod.setFinishgame(method.getFinishgame());
        methodRepository.save(currentMethod);

        return new ResponseEntity<Method>(method,HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na usunięcie metody przez protokół HTTP.
     * @param id Id metody.
     * @return Dane metody oraz status HTTP.
     */
    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Method> delete(@PathVariable Long id)
    {
        Method method = methodRepository.findById(id);
        if(method==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        methodRepository.delete(id);
        return new ResponseEntity<>(method, HttpStatus.OK);
    }

    /**
     * Funkcja pozwala na dodanie metody przez protokół HTTP.
     * @param method Metoda.
     * @return Dane metody oraz protokół HTTP.
     */
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ResponseEntity<Method> create(@RequestBody(required = false) Method method)
    {
        if(method==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        methodRepository.save(method);
        return new ResponseEntity<>(method, HttpStatus.OK);

    }

    /**
     * Funkcja obsługuje wyjątki związane z błędem bazy danych.
     * @param e Wyjątek.
     * @return Status Bad Request.
     */
    @ExceptionHandler
    @ResponseBody
    private ResponseEntity<Method> handleException(DataAccessException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
