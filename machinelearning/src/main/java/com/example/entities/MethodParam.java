package com.example.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Klasa reprezentująca encję parametru metody.
 * Created by mateusz on 04.03.16.
 */
@Entity
@Table(name="method_params")
@NoArgsConstructor
@ToString
public class MethodParam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String name;
    @Column(updatable = false,name="methods_id")
    @Getter
    @Setter
    private Long methodsid;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('INT', 'DOUBLE', 'STRING','BOOLEAN')")
    @Setter
    @Getter
    private Type type;
    @ManyToOne
    @JoinColumn(name = "methods_id",referencedColumnName = "id",nullable = false, updatable = false,insertable = false)
    @Getter
    @Setter
    private Method method;

    /**
     * Typ wyliczeniowy odpowiadający typowi parametru w bazie danych.
     */
    private enum Type {
        INT,DOUBLE,STRING,BOOLEAN;
    }

}







