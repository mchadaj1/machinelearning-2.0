package com.example;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Klasa zbioru statystyk dotyczących jednego wykonania algorytmu.
 * Created by mateusz on 16.05.16.
 */
public class StatisticDTO {

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int experiments;
    @Getter
    @Setter
    private int simulations;
    private Map<String,String> configurations = new HashMap<>();
    @Getter
    @Setter
    private List<List<Double>> preysKilled;
    @Getter
    @Setter
    private List<List<Double>> earnedPoints;

    /**
     * Funkcja wstawia do tablicy konfiguracji element.
     * @param key Nazwa parametru.
     * @param value Wartość.
     */
    public void insertParam(String key, String value) {
        configurations.put(key, value);
    }

    /**
     * Funkcja tworzy dwuwymiarową listę preysKilled o rozmiarach experiments - simulations wypełnioną zerami.
     */
    public void preparePreysKilled() {
        preysKilled = new ArrayList<>(experiments);
        for(int i = 0; i < experiments; i++) {
            preysKilled.add(new ArrayList<>(simulations));
            for(int j = 0; j < simulations; j++) {
                preysKilled.get(i).add(j,0.0);
            }
        }
    }

    /**
     * Funkcja tworzy dwuwymiarową listę earnedPoints o rozmiarach experiments - simulations wypełnioną zerami.
     */
    public void prepareEarnedPoints() {
        setEarnedPoints(new ArrayList<>(experiments));
        for(int i = 0; i < experiments; i++) {
            getEarnedPoints().add(new ArrayList<>(simulations));
            for(int j = 0; j < simulations; j++) {
                getEarnedPoints().get(i).add(j,0.0);
            }
        }
    }

    /**
     * Funkcja zamienia wartości listy na średnią kroczącą o współczynniku K.
     * @param K Współczynnik średniej kroczącej.
     * @param points True, jeśli należy uśrednić listę zawierającą zdobyte punkty, w p.p. false.
     */
    public void prepareAverage(int K, boolean points) {
        List<List<Double>> list;
        if(points) {
            list = earnedPoints;
        }
        else {
            list = preysKilled;
        }
        if(simulations > K) {
            List<List<Double>> averages = new ArrayList<>(experiments + 1);
            List<Double> averageOfAllExperiments = new ArrayList<>(simulations - K);
            for(int s = 0; s < simulations - K; s++) {
                averageOfAllExperiments.add(0.0);
            }
            for(int l = 0; l< experiments; l++) {
                List<Double> averagesInSimulation = new ArrayList<>(simulations - K);
                for(int i = K; i < simulations; i++) {
                    double sum = 0;
                    for (int j = i - K; j < i; j++) {
                        sum += list.get(l).get(j);
                    }
                    averagesInSimulation.add(sum/K);
                    averageOfAllExperiments.set(i- K, averageOfAllExperiments.get(i- K)+sum/K);
                }
                averages.add(averagesInSimulation);
            }
            final int exps = experiments;
            averageOfAllExperiments.replaceAll(b -> b/exps);
            averages.add(averageOfAllExperiments);
            if(points) {
                earnedPoints = averages;
            }
            else {
                preysKilled = averages;
            }
            simulations -= K;
            experiments += 1;
        }
    }
}
