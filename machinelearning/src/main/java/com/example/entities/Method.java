package com.example.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Klasa reprezentująca encję metody uczenia maszynowego.
 * Created by mateusz on 05.03.16.
 */
@Entity
@Table(name="methods")
@NoArgsConstructor
public class Method {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private Long user_id;
    @Getter
    @Setter
    private Long problem_id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private boolean ispublic;
    @Getter
    @Setter
    private String code;
    @Getter
    @Setter
    private String globals;
    @Getter
    @Setter
    private String imports;
    @Getter
    @Setter
    private String constructorcode;
    @Getter
    @Setter
    private String finishgame;
    @ManyToOne(optional=false)
    @JoinColumn(name="problem_id",referencedColumnName="id" ,insertable = false, updatable = false)
    @Getter
    @Setter
    private Problem problem;

}
