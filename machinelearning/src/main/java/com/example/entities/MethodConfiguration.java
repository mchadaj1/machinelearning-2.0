package com.example.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Klasa reprezentująca encję konfiguracji metody uczenia maszynowego.
 * Created by mateusz on 06.03.16.
 */
@Entity
@Table(name="method_configurations")
@NoArgsConstructor
public class MethodConfiguration {

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
    private int method_id;
    @ManyToOne(optional=false)
    @JoinColumn(name="method_id",referencedColumnName="id" ,insertable = false, updatable = false)
    @Getter
    @Setter
    private Method method;
    @Getter
    @OneToMany(mappedBy = "methodConfigurationId")
    private List<MethodParamValue> methodParamValues;
}
