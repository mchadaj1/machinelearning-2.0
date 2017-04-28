package com.example.services;


import com.example.entities.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Repozytorium problemów.
 * Created by Mateusz on 2016-03-03.
 */
@RepositoryRestResource
public interface ProblemsRepository extends JpaRepository<Problem, Long> {
    /**
     * Funkcja wyszukuje problem o podanym Id.
     * @param id Id problemu.
     * @return Problem.
     */
    Problem findById(Long id);

}
