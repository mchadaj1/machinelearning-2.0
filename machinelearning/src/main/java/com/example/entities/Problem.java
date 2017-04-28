package com.example.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Klasa reprezentująca encję problemu.
 * Created by Mateusz on 2016-03-03.
 */
@Entity
@Table(name="problems")
@NoArgsConstructor
public class Problem {

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
    private String description;
}
