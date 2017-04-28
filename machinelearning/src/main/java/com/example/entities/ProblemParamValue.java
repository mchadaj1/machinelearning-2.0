package com.example.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Klasa reprezentująca encję wartości parametru problemu.
 * Created by mateusz on 06.03.16.
 */
@Entity
@Table(name="problem_params_values")
@NoArgsConstructor
public class ProblemParamValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String value;
    @Column(name="problem_configurations_id",updatable = false)
    @Getter
    @Setter
    private Long problemConfigurationId;
    @Column(name="problem_param_id",updatable = false)
    @Getter
    @Setter
    private Long problemParamId;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name="problem_param_id",referencedColumnName = "id",nullable = false,updatable = false,insertable = false)
    private ProblemParam problem_param;

    /**
     * Konstruktor wartości parametru problemu.
     * @param param_id Id parametru.
     * @param configuration_id Id konfiguracji.
     * @param value Wartość.
     */
    public ProblemParamValue(Long param_id, Long configuration_id, String value) {
        this.problemParamId = param_id;
        this.problemConfigurationId = configuration_id;
        this.value = value;
    }


}
