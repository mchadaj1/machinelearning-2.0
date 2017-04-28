package com.example.restControllers;

import com.example.StatisticDTO;
import com.example.entities.*;
import com.example.services.AlgorithmExecutionRepository;
import com.example.services.MethodConfigurationRepository;
import com.example.services.ProblemConfigurationRepository;
import com.example.services.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Kontroler dostarczający statystyki przez protokół HTTP.
 * Created by mateusz on 13.05.16.
 */
@RestController
@RequestMapping("/services/statistics")
public class StatisticsRestController {

    private final StatisticRepository statisticRepository;
    private final ProblemConfigurationRepository problemConfigurationRepository;
    private final MethodConfigurationRepository methodConfigurationRepository;
    private final AlgorithmExecutionRepository algorithmExecutionRepository;

    /**
     * Konstruktor kontrolera statystyk.
     * @param statisticRepository Repozytorium statystyk.
     * @param problemConfigurationRepository Repozytorium Konfiguracji problemów (zadań).
     * @param methodConfigurationRepository Repozytorium konfiguracji metod.
     * @param algorithmExecutionRepository Repozytorium wykonań algorytmów.
     */
    @Autowired
    public StatisticsRestController(StatisticRepository statisticRepository,
                                    ProblemConfigurationRepository problemConfigurationRepository,
                                    MethodConfigurationRepository methodConfigurationRepository,
                                    AlgorithmExecutionRepository algorithmExecutionRepository) {
        this.statisticRepository = statisticRepository;
        this.problemConfigurationRepository = problemConfigurationRepository;
        this.methodConfigurationRepository = methodConfigurationRepository;
        this.algorithmExecutionRepository = algorithmExecutionRepository;
    }

    /**
     * Współczynnik k średniej kroczącej.
     */
    private final int K = 10;

    /**
     * Funkcja dostarcza przez protokól HTTP statystyki zabijania ofiar wykonanego algorytmu o podanym Id.
     * @param id Id wykonania algorytmu.
     * @return Obiekt zawierający statystyki oraz status.
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<StatisticDTO> getKills(@PathVariable Long id){
        StatisticDTO statisticDTO = new StatisticDTO();
        List<Statistic> statistics = statisticRepository.findByExecutionId(id);

        if(statistics == null || statistics.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AlgorithmExecution algorithmExecution = algorithmExecutionRepository.findById(id);
        ProblemConfiguration problemConfiguration = problemConfigurationRepository.findById(algorithmExecution.getProblemConfigurationId());

        //Ustaw nazwę wykonania algorytmu
        statisticDTO.setName(getStatisticsTitle(id));

        //Pobierz liczbę experymentów i symulacji
        try {
            statisticDTO.setExperiments(getProblemParamValue(problemConfiguration.getProblemParamValues(), "experiments"));
            statisticDTO.setSimulations(getProblemParamValue(problemConfiguration.getProblemParamValues(), "simulations"));
        } catch (NullPointerException e) {
            System.out.println("Nie określono parametru experiments lub simulations!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //przygotuj tablicę na statystyki
        statisticDTO.preparePreysKilled();
        //usupełnij tablicę statystyk wartościami pobranymi z bazy
        statistics.stream().filter(statistic -> Objects.equals(statistic.getEaten_agent_type(), "PREY"))
                .forEach(statistic -> {
            statisticDTO.getPreysKilled().get(statistic.getExperiment_number() - 1)
                    .set(statistic.getSimulation_number() - 1, statisticDTO.getPreysKilled()
                            .get(statistic.getExperiment_number() - 1).get(statistic.getSimulation_number() - 1) + 1);
        });

        //uśrednij wyniki
        statisticDTO.prepareAverage(K, false);
        return new ResponseEntity<>(statisticDTO, HttpStatus.OK);
    }

    /**
     * Funkcja dostarcza przez protokól HTTP statystyki zdobytych punktów wykonanego algorytmu o podanym Id.
     * @param id Id wykonania algorytmu.
     * @return Obiekt zawierający statystyki oraz status.
     */
    @RequestMapping(value = "/points/{id}",method = RequestMethod.GET)
    public ResponseEntity<StatisticDTO> getPoints(@PathVariable Long id){

        StatisticDTO statisticDTO = new StatisticDTO();
        statisticDTO.setName(getStatisticsTitle(id));

        List<Statistic> statistics = statisticRepository.findByExecutionId(id);
        if(statistics == null || statistics.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AlgorithmExecution algorithmExecution = algorithmExecutionRepository.findById(id);
        ProblemConfiguration problemConfiguration = problemConfigurationRepository
                .findById(algorithmExecution.getProblemConfigurationId());
        MethodConfiguration methodConfiguration = methodConfigurationRepository
                .findById(algorithmExecution.getMethodConfigurationId());
        List<ProblemParamValue> problemParamValues = problemConfiguration.getProblemParamValues();
        List<MethodParamValue> methodParamValues = methodConfiguration.getMethodParamValues();

        int experiments;
        int simulations;
        int steps;
        double deathPoints;
        double killPoints;
        double movePoints;
        try {
            experiments = getProblemParamValue(problemParamValues, "experiments");
            simulations = getProblemParamValue(problemParamValues, "simulations");

            deathPoints = getMethodParamValue(methodParamValues, "deathPoints");
            killPoints = getMethodParamValue(methodParamValues, "killPoints");
            movePoints = getMethodParamValue(methodParamValues, "movePoints");
            steps = getProblemParamValue(problemParamValues, "steps");
        } catch (NullPointerException e) {
            System.out.println("Wystąpił błąd z parametrem");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        statisticDTO.setExperiments(experiments);
        statisticDTO.setSimulations(simulations);
        statisticDTO.prepareEarnedPoints();
        //przygotuj tymczasowe listy
        List<List<Boolean>> died  = prepareDiedList(experiments, simulations);
        List<List<Integer>> simulationSteps = prepareSimulationsStepsList(experiments, simulations, steps);
        //nanieś punkty na listy
        applyPoints(statistics, died, statisticDTO.getEarnedPoints(), simulationSteps);
        //skoryguj liczbę punktów o punkty za zwykły ruch
        correctPoints(experiments, simulations, killPoints, deathPoints, movePoints, died, simulationSteps,
                statisticDTO.getEarnedPoints());
        //uśrednij wyniki
        statisticDTO.prepareAverage(K, true);


        return new ResponseEntity<>(statisticDTO,HttpStatus.OK);
    }

    /**
     * Funkcja wyciąga ze zbioru wartości parametrów problemów, wartość parametru o podanej nazwie.
     * @param problemParamValues Lista wartości parametrów problemów.
     * @param name Nazwa parametru.
     * @return Wartość parametru.
     */
    private Integer getProblemParamValue(List<ProblemParamValue> problemParamValues, String name) {
        for(ProblemParamValue problemParamValue : problemParamValues) {
            if(Objects.equals(problemParamValue.getProblem_param().getName(), name)) {
                return Integer.parseInt(problemParamValue.getValue());
            }
        }
        return null;
    }

    /**
     * Funkcja wyciąga ze zbioru wartości parametrów metod, wartość parametru o podanej nazwie.
     * @param methodParamValues Lista wartości parametrów metod.
     * @param name Nazwa parametru.
     * @return Wartość parametru.
     */
    private Double getMethodParamValue(List<MethodParamValue> methodParamValues, String name) {
        for(MethodParamValue methodParamValue : methodParamValues) {
            if(Objects.equals(methodParamValue.getMethod_param().getName(), name)) {
                return Double.parseDouble(methodParamValue.getValue());
            }
        }
        return null;
    }

    /**
     * Funkcja przygotowuje nazwę dla zbioru statystyk.
     * @param algorithmExecutionId Id wykonania algorytmu.
     * @return Nazwa.
     */
    private String getStatisticsTitle(Long algorithmExecutionId) {
        AlgorithmExecution algorithmExecution = algorithmExecutionRepository.findById(algorithmExecutionId);

        ProblemConfiguration problemConfiguration = problemConfigurationRepository
                .findById(algorithmExecution.getProblemConfigurationId());
        MethodConfiguration methodConfiguration = methodConfigurationRepository
                .findById(algorithmExecution.getMethodConfigurationId());

        return ("Konfiguracja problemu: " + problemConfiguration.getName() + " Konfiguracja metody: "
                + methodConfiguration.getName());
    }

    /**
     * Przygotowuje listę informującą o śmierci łowcy w symulacjach.
     * @param experiments Liczba eksperymentów.
     * @param simulations Liczba symulacji.
     * @return Lista śmierci łowcy w symulacjach z wartościami False.
     */
    private List<List<Boolean>> prepareDiedList(int experiments, int simulations) {
        List<List<Boolean>> died  = new ArrayList<>(experiments);
        for (int i = 0; i < experiments; i++) {
            List<Boolean> died2 = new ArrayList<>(simulations);
            for(int j = 0; j < simulations; j++) {
                died2.add(false);
            }
            died.add(died2);
        }
        return died;
    }

    /**
     * Funkcja czyta na podstawie listy statystyk uzupełnia listę zawierającą informację o śmierci łowcy w symulacjach,
     * ilości wykonanych kroków oraz zdobytych punktów.
     * @param statistics Lista statystyk.
     * @param died Lista zawierająca informację o śmierci łowcy w symulacjach.
     * @param points Lista zawierająca punkty zdobyte w poszczególnych symulacjach.
     * @param simulationSteps Lista zawierająca liczby kroków wykonanych w symulacjach.
     */
    private void applyPoints(List<Statistic> statistics, List<List<Boolean>> died, List<List<Double>> points,
                             List<List<Integer>> simulationSteps) {
        for(Statistic statistic : statistics) {
            if(Objects.equals(statistic.getEaten_agent_type(),"PREY")) {
                points.get(statistic.getExperiment_number()-1).set(statistic.getSimulation_number()-1,
                        points.get(statistic.getExperiment_number()-1).get(statistic.getSimulation_number()-1)+1);
            }
            if(Objects.equals(statistic.getEaten_agent_type(),"HUNTER")) {
                simulationSteps.get(statistic.getExperiment_number()-1).set(statistic.getSimulation_number()-1,
                        statistic.getStep_number());
                died.get(statistic.getExperiment_number()-1).set(statistic.getSimulation_number()-1,true);
            }
            if(Objects.equals(statistic.getEaten_agent_type(), "all")) {
                simulationSteps.get(statistic.getExperiment_number()-1).set(statistic.getSimulation_number()-1,
                        statistic.getStep_number());
            }
        }

    }

    /**
     * Przygotowuje pustą tablicę do przechowywania ilości kroków w symulacji.
     * @param experiments Liczba eksperymentów.
     * @param simulations Liczba Symulacji.
     * @param steps Liczba wszystkich kroków.
     * @return Lista zawierająca eksperymenty, a pod nimi listę kroków dla każdej symulacji
     * ustawioną na maksymalną liczbę kroków.
     */
    private List<List<Integer>> prepareSimulationsStepsList(int experiments, int simulations, int steps) {
        List<List<Integer>> simulationSteps = new ArrayList<>(experiments);
        for(int i = 0; i < experiments; i++) {
            List<Integer> stepsList = new ArrayList<>(simulations);
            for(int j = 0; j < simulations; j ++) {
                stepsList.add(steps);
            }
            simulationSteps.add(stepsList);
        }
        return simulationSteps;
    }

    /**
     * Funkcja nanosi poprawkę na zdobyte punkty - odejmuje liczbę wykonanych zwykłych kroków razy koszt wykonania kroku.
     * @param experiments Liczba eksperymentów.
     * @param simulations Liczba symulacji.
     * @param killPoints Punkty za zabicie ofiary.
     * @param deathPoints Punkty za śmierć łowcy.
     * @param movePoints Punkty za zwykły ruch.
     * @param died Lista zawierająca informację, czy hunter zginął w danej rundzie.
     * @param simulationSteps Lista zawierająca informację ile kroków zostało wykonane w danej rundzie.
     * @param earnedPoints Lista zawierająca zdobyte punkty.
     */
    private void correctPoints(int experiments, int simulations, double killPoints, double deathPoints, double movePoints,
                               List<List<Boolean>> died, List<List<Integer>> simulationSteps,
                               List<List<Double>> earnedPoints) {
        for( int i = 0; i <experiments ; i ++) {
            for (int j = 0; j < simulations ; j++) {
                double points;
                double notMoves = earnedPoints.get(i).get(j);
                points = notMoves * killPoints;
                if (died.get(i).get(j)) {
                    notMoves +=1;
                    points += deathPoints;
                    points -= movePoints;
                }
                points += movePoints * (simulationSteps.get(i).get(j) - notMoves);
                earnedPoints.get(i).set(j,points);
            }
        }
    }

    /**
     * Funkcja obsługuje wyjątki związane z błędem bazy danych.
     * @param e Wyjątek.
     * @return Status Bad Request.
     */
    @ExceptionHandler
    @ResponseBody
    private ResponseEntity<Statistic> handleException(DataAccessException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

