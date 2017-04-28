package com.example.hunterPreyPredator.agents;

/**
 * Created by mateusz on 08.04.16.
 */


import com.example.hunterPreyPredator.map.Position;

import java.util.Map;

/**
 * Interfejs klasy agenta.
 */
public interface Agent {

    /**
     * Funkcja wyboru następnego kroku.
     * @param mapInfo Informacje na temat mapy.
     * @return Wybrana pozycja.
     */
    Position nextStep(Map<String, Object> mapInfo);

    /**
     * Zwraca typ agenta.
     * @return Typ agenta.
     */
    AgentType getType();

    /**
     * Funkcja wywoływana po wykonaniu wszystkich ruchów.
     * @param mapInfo Informacje na temat mapy.
     */
    void finishGame(Map<String, Object> mapInfo);

}
