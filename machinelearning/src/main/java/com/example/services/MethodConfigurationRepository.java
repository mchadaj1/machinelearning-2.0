package com.example.services;


import com.example.entities.MethodConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Repozytorium konfiguracji metod.
 * Created by Mateusz on 2016-03-03.
 */
@RepositoryRestResource(path = "/methodconfiguration")
public interface MethodConfigurationRepository extends JpaRepository<MethodConfiguration, Long> {

    /**
     * Funkcja wyszukuje konfigurację metody o podanym Id.
     * @param id Id konfiguracji metody.
     * @return Konfiguracja metody.
     */
    MethodConfiguration findById(Long id);


}
