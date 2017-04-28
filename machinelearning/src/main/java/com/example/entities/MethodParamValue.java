package com.example.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Klasa reprezentująca encję wartości parametrów metody.
 * Created by mateusz on 06.03.16.
 */
@Entity
@Table(name="method_param_values")
@NoArgsConstructor
@ToString
public class MethodParamValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String param_name;
    @Getter
    @Setter
    private String value;
    @Getter
    @Setter
    @Column(name="method_configurations_id",updatable = false)
    private Long methodConfigurationId;
    @Getter
    @Setter
    @Column(name="method_param_id",updatable = false)
    private Long methodParamId;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name="method_param_id",referencedColumnName = "id",nullable = false,updatable = false,insertable = false)
    private MethodParam method_param;

    /**
     * Konstruktor klasy wartości parametru metody.
     * @param param_id Id parametru.
     * @param configuration_id Id konfiguracji.
     * @param value Wartość.
     */
    public MethodParamValue(Long param_id, Long configuration_id, String value) {
        this.methodParamId = param_id;
        this.methodConfigurationId = configuration_id;
        this.value = value;
    }

}
