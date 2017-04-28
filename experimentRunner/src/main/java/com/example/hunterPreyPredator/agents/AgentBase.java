package com.example.hunterPreyPredator.agents;


import com.example.hunterPreyPredator.map.Position;

import java.util.Map;

/**
 * Abstrakcyjna klasa agenta.
 * Created by mateusz on 14.04.16.
 */
public abstract class AgentBase implements Agent{

    private int number;
    private boolean active = true;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Konstruktor klasy łowcy.
     * @param number Numer agenta.
     */
    public AgentBase(int number) {
        this.number = number;
    }

    /**
     * Konstruktor klasy łowcy.
     * @param map Mapa atrybutów metody.
     */
    public AgentBase(Map<String, String> map) {
    }

    /**
     * Implementacja losowo poruszającego się agenta.
     * @param mapInfo Informacje na temat mapy.
     * @return Losowo wybrana pozycja.
     */
    @Override
    public Position nextStep(Map<String, Object> mapInfo) {
        int x = 0;
        int y = 0;
        while(x == 0 && y == 0) {
            x = (int) (Math.random() * 3);
            x -= 1;
            y = (int) (Math.random() * 3);
            y -= 1;
        }

        Position myPosition = (Position)mapInfo.get("position");
        Position newPosition = new Position(myPosition);
        newPosition.setX(myPosition.getX()+x);
        newPosition.setY(myPosition.getY()+y);

        return newPosition;
    }

    public AgentType getType(){
        return null;
    }

    @Override
    public void finishGame(Map<String, Object> mapInfo) {

    }
}
