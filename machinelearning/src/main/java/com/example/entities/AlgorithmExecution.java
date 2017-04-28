package com.example.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Klasa reprezentująca encję wykonania algorytmu.
 * Created by mateusz on 09.05.16.
 */
@Entity
@Table(name="algorithm_executions")
@NoArgsConstructor
@ToString
public class AlgorithmExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private boolean pending;
    @Getter
    @Setter
    private boolean completed;
    @Getter
    @Setter
    private String filename;
    @Column(name="method_configuration_id",updatable = false)
    @Getter
    @Setter
    private Long methodConfigurationId;
    @Column(name="problem_configuration_id",updatable = false)
    @Getter
    @Setter
    private Long problemConfigurationId;
    @ManyToOne
    @JoinColumn(name = "problem_configuration_id",referencedColumnName = "id",nullable = false,updatable = false,insertable = false)
    @Getter
    @Setter
    private ProblemConfiguration problemConfiguration;
    @ManyToOne
    @JoinColumn(name = "method_configuration_id",referencedColumnName = "id",nullable = false,updatable = false,insertable = false)
    @Getter
    @Setter
    private MethodConfiguration methodConfiguration;

    /**
     * Konstruktor klasy AlgorithmExecution
     * @param methodConfigurationId id Konfiguracji metody.
     * @param problemConfigurationId id Konfiguracji problemu.
     */
    public AlgorithmExecution(Long methodConfigurationId, Long problemConfigurationId) {
        this.methodConfigurationId = methodConfigurationId;
        this.problemConfigurationId = problemConfigurationId;
    }
}
