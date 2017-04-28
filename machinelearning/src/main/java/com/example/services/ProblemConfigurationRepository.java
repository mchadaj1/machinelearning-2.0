package com.example.services;


import com.example.entities.ProblemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Repozytorium problemów.
 * Created by Mateusz on 2016-03-03.
 */
@RepositoryRestResource(path = "/problemconfiguration")
public interface ProblemConfigurationRepository extends JpaRepository<ProblemConfiguration, Long> {

    /**
     * Funkcja wyszukuje konfigurację problemu o podanym Id.
     * @param id Id konfiguracji.
     * @return Konfiguracja Problemu.
     */
    ProblemConfiguration findById(Long id);

}
