package com.example.services;


import com.example.entities.AlgorithmExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Repozytorium wykonań algorytmów.
 * Created by Mateusz on 2016-03-03.
 */
@RepositoryRestResource
public interface AlgorithmExecutionRepository extends JpaRepository<AlgorithmExecution, Long> {

    /**
     * Funkcja wyszukuje oczekujących, niewykonanych zadań.
     * @return Lista oczekujących zadań.
     */
    List<AlgorithmExecution> findByPendingTrueAndCompletedFalse();

    /**
     * Funkcja wyszukuje zadania o podanym id.
     * @param id Id zadania.
     * @return Zadanie.
     */
    AlgorithmExecution findById(Long id);

}
