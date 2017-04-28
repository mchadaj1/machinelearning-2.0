package com.example.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Klasa reprezentująca encję Statystyki.
 * Created by mateusz on 11.05.16.
 */
@Entity
@Table(name = "statistics")
@NoArgsConstructor
@ToString
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private int eaten_agent_number;
    @Getter
    @Setter
    private int experiment_number;
    @Getter
    @Setter
    private int simulation_number;
    @Getter
    @Setter
    private int step_number;
    @Getter
    @Setter
    private String eaten_agent_type;
    @Getter
    @Setter
    @Column(name = "execution_id")
    private Long executionId;

    /**
     * Konstruktor klasy statystyka.
     * @param eaten_agent_number Id pokonanego agenta.
     * @param step_number Numer kroku.
     * @param eaten_agent_type Typ pokonanego agenta.
     */
    public Statistic(int eaten_agent_number, int step_number, String eaten_agent_type) {
        this.eaten_agent_number = eaten_agent_number;
        this.step_number = step_number;
        this.eaten_agent_type = eaten_agent_type;
    }

    /**
     * Konstruktor klasy statystyka.
     * @param eaten_agent_number Id pokonanego agenta.
     * @param step_number Numer kroku.
     */
    public Statistic(int eaten_agent_number, int step_number) {
        this.eaten_agent_number = eaten_agent_number;
        this.step_number = step_number;
    }
}
