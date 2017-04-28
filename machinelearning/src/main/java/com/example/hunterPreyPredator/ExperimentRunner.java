package com.example.hunterPreyPredator;


import com.example.hunterPreyPredator.exceptions.NoHunterClassException;
import com.example.hunterPreyPredator.agents.AgentBase;
import com.example.hunterPreyPredator.agents.Predator;
import com.example.hunterPreyPredator.agents.Prey;
import com.example.entities.Statistic;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mateusz on 08.04.16.
 */

/**
 * Klasa wykonująca eksperymenty Łowca, Ofiary, Drapieżniki.
 */
public class ExperimentRunner {


    private int preys;
    private int predators;
    private int simuls;
    private int width;
    private int height;
    private int range;
    private int steps;
    private List<AgentBase> agents;
    private List<Statistic> statistics = new ArrayList<>();
    private Class<?> hunterClass;
    private OutputStream outputStream;
    private Map<String, String> methodAttributes;
    private List<Simulation> simulations = new ArrayList<>(simuls);

    public List<Statistic> getStatistics() {
        return statistics;
    }

    /**
     * Konstruktor klasy wykonującej eksperymenty.
     * @param attributes Atrybuty sterujące wykonaniem eksperymentu.
     * @param methodAttributes Atrybuty sterujące przebiegiem metody.
     * @param hunterClass Klasa łowcy.
     * @param outputStream Outputstream do logowania.
     * @throws NumberFormatException W przypadku niepoprawnej wartości jednego z parametrów.
     */
    public ExperimentRunner(Map<String, String> attributes, Map<String, String> methodAttributes, Class<?> hunterClass,
                            OutputStream outputStream) throws NumberFormatException {
        this.hunterClass = hunterClass;
        this.outputStream = outputStream;
        preys = Integer.parseInt(attributes.get("preys"));
        predators = Integer.parseInt(attributes.get("predators"));
        simuls = Integer.parseInt(attributes.get("simulations"));
        width = Integer.parseInt(attributes.get("width"));
        height = Integer.parseInt(attributes.get("height"));
        range = Integer.parseInt(attributes.get("range"));
        steps = Integer.parseInt(attributes.get("steps"));
        this.methodAttributes = methodAttributes;
    }



    /**
     * Funkcja ustawia wartości początkowe eksperymentu, tworzy agentów.
     * @throws IllegalAccessException W przypadku braku dostępu do konstruktora klasy łowcy.
     * @throws InstantiationException W przypadku braku konstruktora w klasie łowcy.
     * @throws NoHunterClassException W przypadku, gdy nie uda się utworzyć klasy łowcy.
     */
    public void init() throws IllegalAccessException, InstantiationException, NoHunterClassException {
        agents = new ArrayList<>(predators+preys+1);
        AgentBase hunter;
        try {
            hunter = (AgentBase) hunterClass.getDeclaredConstructor(Map.class).newInstance(methodAttributes);
        } catch (InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new NoHunterClassException();
        }

        agents.add(hunter);
        for (int i = 1; i <= preys; i++) {
            agents.add(new Prey(i));
        }
        for (int i = preys + 1; i <= predators+preys; i++ ) {
            agents.add(new Predator(i));
        }

    }

    /**
     * Uruchamia eksperyment. Wykonuje symulacje w ilości podanej w parametrze.
     * @throws IOException W przypadku wystąpienia problemu z zapisem do pliku z logiem.
     */
    public void run() throws IOException {

        for(int i = 1; i <= simuls ; i++ ) {
            outputStream.write(("simultaion: " + i + "\n").getBytes());
            Simulation simulation = new Simulation(steps, preys, predators, width, height, outputStream, range);
            simulation.init(agents);
            simulation.run();
            simulations.add(simulation);
            setStatistics(i, simulation);

        }
    }

    /**
     * Funkcja ustawia numer symulacji na liście statystyk.
     * @param simulationNumber Numer symulacji.
     * @param simulation Symulacja.
     */
    public void setStatistics(int simulationNumber, Simulation simulation) {
        final int number = simulationNumber;
        simulation.getStatistics().stream()
                .forEach(b -> b.setSimulation_number(number));
        statistics.addAll(simulation.getStatistics());

    }

}
