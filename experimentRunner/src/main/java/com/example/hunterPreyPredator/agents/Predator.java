package com.example.hunterPreyPredator.agents;


/**
 * Klasa drapieżnika.
 * Created by mateusz on 08.04.16.
 */
public class Predator extends AgentBase {

    public Predator(int number) {
        super(number);
    }

    @Override
    public AgentType getType() {
        return AgentType.PREDATOR;
    }
}
