package com.example.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Klasa reprezentująca encję konfiguracji problemu.
 * Created by mateusz on 06.03.16.
 */
@Entity
@Table(name="problem_configurations")
@NoArgsConstructor
public class ProblemConfiguration {

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
    private int problems_id;
    @Getter
    @Setter
    @ManyToOne(optional=false)
    @JoinColumn(name="problems_id",referencedColumnName="id" ,insertable = false, updatable = false)
    private Problem problem;
    @Getter
    @Setter
    @OneToMany(mappedBy = "problemConfigurationId")
    private List<ProblemParamValue> problemParamValues;

}
