package com.example.hunterPreyPredator.agents;

/**
 * Typ agenta.
 * Created by mateusz on 08.04.16.
 */
public enum AgentType {
    HUNTER,
    PREY,
    PREDATOR;

    /**
     * Sprawdza, czy agenci ze sobą walczą.
     * @param agentType Typ drugiego agenta.
     * @return True, jeśli agenci ze sobą walczą (jeden z nich jest łowcą)
     */
    public boolean interacts(AgentType agentType) {
        return !((this != HUNTER) && (agentType != HUNTER));
    }

}
