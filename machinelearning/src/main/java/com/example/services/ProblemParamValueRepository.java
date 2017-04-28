package com.example.services;


import com.example.entities.ProblemParamValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Lista wartości parametrów problemów.
 * Created by Mateusz on 2016-03-03.
 */
@RepositoryRestResource(path = "/problem_param_values")
public interface ProblemParamValueRepository extends JpaRepository<ProblemParamValue, Long> {

    /**
     * Funkcja wyszukuje wartości parametru o podanym Id przypisanym do konfiguracji o podanym Id.
     * @param problemParamId Id parametru.
     * @param problemConfigurationId Id Konfiguracji.
     * @return Wartość parametru problemu.
     */
    ProblemParamValue findByProblemParamIdAndProblemConfigurationId(Long problemParamId, Long problemConfigurationId);

    /**
     * Funkcja wyszukuje wartości parametrów dla konfiguracji o podanym Id.
     * @param id Id konfiguracji.
     * @return Lista wartości parametrów.
     */
    List<ProblemParamValue> findByProblemConfigurationId(Long id);

}
