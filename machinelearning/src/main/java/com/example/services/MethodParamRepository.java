package com.example.services;


import com.example.entities.MethodParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Repozytorium parametrów metody.
 * Created by Mateusz on 2016-03-03.
 */
@RepositoryRestResource
public interface MethodParamRepository extends JpaRepository<MethodParam, Long> {

    /**
     * Funkcja wyszukuje parametr o podanym Id.
     * @param id Id parametru.
     * @return Parametr.
     */
    MethodParam findById(Long id);

    /**
     * Funkcja wyszukuje parametry metody o podanym Id.
     * @param methodid Id metody.
     * @return Lista parametrów metody.
     */
    List<MethodParam> findByMethodsid(Long methodid);

}
