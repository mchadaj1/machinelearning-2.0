package com.example.hunterPreyPredator.agents;


/**
 * Klasa ofiary.
 * Created by mateusz on 08.04.16.
 */
public class Prey extends AgentBase {

    public Prey(int number) {
        super(number);
    }

    @Override
    public AgentType getType() {
        return AgentType.PREY;
    }
}
