package com.example.hunterPreyPredator;


//import com.com.example.entities.Statistic;
import com.example.hunterPreyPredator.agents.AgentBase;
import com.example.hunterPreyPredator.exceptions.GameFinishedException;
import com.example.hunterPreyPredator.map.MyMap;
import com.example.hunterPreyPredator.map.Position;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by mateusz on 08.04.16.
 */

/**
 * Klasa wykonująca symulację zadania Łowca, Ofiary, Drapieżniki.
 */
public class Simulation {

    private int steps;
    private int preys;
    private int predators;
    private int width;
    private int height;
    private int preysleft;
    private int range;
//    private List<Statistic> statistics = new ArrayList<>();
    private OutputStream outputStream;
    private List<AgentBase> agents;
    private MyMap map;

    /**
     * Konstruktor klasy wykonującej pojedynczą symulację.
     * @param steps Liczba kroków w symulacji.
     * @param preys Liczba ofiar.
     * @param predators Liczba drapieżników.
     * @param width Szerokość mapy.
     * @param height Wysokość mapy.
     * @param outputStream Stream do logowania.
     * @param range Widoczność łowcy na mapie.
     */
    public Simulation(int steps, int preys, int predators, int width, int height, OutputStream outputStream, int range) {
        this.steps = steps;
        this.preys = preys;
        this.preysleft = preys;
        this.predators = predators;
        this.width = width;
        this.height = height;
        this.outputStream = outputStream;
        this.range = range;
    }

//    public List<Statistic> getStatistics() {
//        return statistics;
//    }

    /**
     * Funkcja inicjuje wartości początkowe symulacji.
     * @param agents Lista agentów.
     * @throws IOException W przypadku wystąpienia problemu z zapisem wykonania przebiegu.
     */
    public void init(List<AgentBase> agents) throws IOException {
        this.agents = agents;
        for(AgentBase agent :agents) {
            agent.setActive(true);
        }
        map = new MyMap(width,height,range, outputStream);
        map.init(preys, predators);
        map.showMapInFile();
    }

    /**
     * Funkcja wykonuje symulację.
     * @throws IOException W przypadku wystąpienia problemu z zapisem wykonania przebiegu.
     */
    public void run() throws IOException {
        AgentBase hunter = agents.get(0);
        try {
            for(int i = 1; i<= steps; i++) {
                outputStream.write("Próba przestawienia łowcy\n".getBytes());
                moveAgent(hunter);
                map.move();
                findDeletedAgent(i, true);
                map.showMapInFile();
                for (AgentBase agent : agents) {
                    //hunter został już przestawiony
                    if (agent.getNumber() != 0) {
                        moveAgent(agent);
                    }
                }
                boolean noCollisions = map.findCollisions();
                while (!noCollisions) {
                    noCollisions = map.findCollisions();
                }
                map.move();
                findDeletedAgent(i, false);
                map.showMapInFile();

            }
            agents.get(0).finishGame(map.getMapInfo(0));
        } catch (GameFinishedException e) {
            agents.get(0).finishGame(map.getMapInfo(0));
        }
    }

    /**
     * Funkcja zwraca numer agenta na liście.
     * @param AgentNumber Numer agenta w klasie.
     * @return Numer agenta na liście.
     */
    public int find(int AgentNumber) {
        for (AgentBase agentBase : agents) {
            if(agentBase.getNumber() == AgentNumber && agentBase.isActive())
                return agents.indexOf(agentBase);
        }
        return -1;
    }

    /**
     * Funkcja wyszukuje na mapie agentów do usunięcia.
     * @param step Numer kroku.
     * @param onlyHunter True, jeśli funkcja wywoływana jest po ruchu łowcy, false jeśli po ruchu reszty agentów.
     * @throws IOException W przypadku wystąpienia problemu z zapisem logów.
     */
    private void findDeletedAgent(int step, boolean onlyHunter) throws IOException, GameFinishedException {
        Integer deleteAgentNumber;
        if ((deleteAgentNumber = map.concludeStep(onlyHunter)) != null) {
//            statistics.add(new Statistic(deleteAgentNumber, step, agents.get(deleteAgentNumber).getType().toString()));
            if (deleteAgentNumber == 0) {
                throw new GameFinishedException();
            } else {
                agents.get(find(deleteAgentNumber)).setActive(false);
                preysleft--;
                if (preysleft == 0) {
//                    statistics.add(new Statistic(-1, step, "all"));
                    throw new GameFinishedException();
                }
            }
        }
    }

    /**
     * Funkcja wykonuje próbę przestawienia agenta na mapie.
     * @param agent Agent.
     * @throws IOException W przypadku wystąpienia problemu z zapisem logów.
     */
    private void moveAgent(AgentBase agent) throws IOException {
        if (agent.isActive()) {
            outputStream.write(("Próba przestawienia agenta " + agent.getNumber() + "\n").getBytes());
            Position wantToStep = agent.nextStep(map.getMapInfo(agent.getNumber()));
            if (map.prepareMove(agent.getNumber(), wantToStep)) {
                map.insertNewPosition(agent.getNumber(), wantToStep);
                outputStream.write(("Przestawiono " + agent.getNumber() + " na pozycje " + wantToStep + "\n").getBytes());
            } else {
                map.takeOldPosition(agent.getNumber());
            }
        }
    }

}
