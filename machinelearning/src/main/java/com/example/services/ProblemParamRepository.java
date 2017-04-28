package com.example.services;


import com.example.entities.ProblemParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Repozytorium parametrów problemu.
 * Created by Mateusz on 2016-03-03.
 */
@RepositoryRestResource
public interface ProblemParamRepository extends JpaRepository<ProblemParam, Long> {

    /**
     * Funkcja wyszukuje parametr o podanym Id.
     * @param id Id parametru.
     * @return Parametr.
     */
    ProblemParam findById(Long id);

    /**
     * Funkcja wyszukuje parametry przypisane do problemu o podanym Id.
     * @param problemsid Id problemu.
     * @return Lista problemów.
     */
    List<ProblemParam> findByProblemsid(Long problemsid);

}
