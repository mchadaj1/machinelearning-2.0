package com.example.services;


import com.example.entities.MethodParamValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Repozytorium wartości parametrów metod.
 * Created by Mateusz on 2016-03-03.
 */
@RepositoryRestResource(path = "/method_param_values")
public interface MethodParamValueRepository extends JpaRepository<MethodParamValue, Long> {

    /**
     * Funkcja wyszukuje wartość parametru o podanym Id w konfiguracji o podanym Id.
     * @param methodParamId Id parametru.
     * @param methodConfigurationId Id konfiguracji.
     * @return Wartość parametru metody.
     */
    MethodParamValue findByMethodParamIdAndMethodConfigurationId(Long methodParamId, Long methodConfigurationId);

    /**
     * Funkcja wyszukuje listę wartości parametrów konfiguracji o podanym Id.
     * @param methodConfigurationId Id konfiguracji.
     * @return Lista wartości parametrów.
     */
    List<MethodParamValue> findByMethodConfigurationId(Long methodConfigurationId);

}
