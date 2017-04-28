package com.example.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Klasa reprezentująca encję parametru problemu.
 * Created by mateusz on 04.03.16.
 */
@Entity
@Table(name="problem_params")
@NoArgsConstructor
@ToString
public class ProblemParam {

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
    @Column(updatable = false,name="problems_id")
    private Long problemsid;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('INT', 'DOUBLE', 'STRING','BOOLEAN')")
    @Getter
    @Setter
    private Type type;
    @ManyToOne
    @JoinColumn(name = "problems_id",referencedColumnName = "id",nullable = false,updatable = false,insertable = false)
    @Getter
    @Setter
    private Problem problem;

    /**
     * Typ wyliczeniowy odpowiadający typowi parametru w bazie danych.
     */
    private enum Type {
        INT,DOUBLE,STRING,BOOLEAN;
    }

}







