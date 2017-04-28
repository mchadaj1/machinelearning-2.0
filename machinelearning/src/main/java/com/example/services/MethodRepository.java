package com.example.services;


import com.example.entities.Method;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Repozytorium metod.
 * Created by Mateusz on 2016-03-03.
 */
@RepositoryRestResource(path = "/method")
public interface MethodRepository extends JpaRepository<Method, Long> {

    /**
     * Funkcja wyszukuje metodę o podanym Id.
     * @param id Id metody.
     * @return Metoda.
     */
    Method findById(Long id);

}
