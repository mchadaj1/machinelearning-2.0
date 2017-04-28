package com.example.hunterPreyPredator.map;

import com.example.hunterPreyPredator.agents.AgentType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Klasa zarządzająca pozycjami agentów w środowisku.
 * Created by mateusz on 08.04.16.
 */
public class MyMap {

    private int width;
    private int height;
    private int range;
    private int hunterPoints = 0;
    private OutputStream outputStream;
    private List<Position> positions;
    private List<Position> newPositions;

    /**
     * Konstruktor mapy.
     * @param width Szerokość mapy.
     * @param height Wysokość mapy.
     * @param range Zasięg widoczności łowcy na mapie.
     * @param outputStream Stream do logowania.
     */
    public MyMap(int width, int height, int range, OutputStream outputStream) {
        this.width = width;
        this.height = height;
        this.range = range;
        this.outputStream = outputStream;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Funkcja inicjuje wartości początkowe.
     * @param preys Liczba ofiar.
     * @param predators Liczba drapieżników.
     */
    public void init(int preys, int predators) {
        positions = new ArrayList<>(predators + preys + 1);
        newPositions = new ArrayList<>(predators + preys + 1);

        //ustaw łowcę
        placeHunter();

        //ustaw ofiary i drapieżników
        placePreysAndPredators(preys, predators);
    }

    /**
     * Funkcja ustawia łowcę w lewym górnym rogu planszy.
     */
    public void placeHunter() {
        positions.add(0, new Position(1, height, AgentType.HUNTER, 0));
        newPositions.add(0, new Position(1, height, AgentType.HUNTER, 0));
    }

    /**
     * Funkcja ustawia ofiary i drapieżników w prawym dolnym rogu planszy.
     * @param preys Liczba ofiar.
     * @param predators Liczba drapieżników
     */
    public void placePreysAndPredators(int preys, int predators) {
        int placeX = 0;
        int placeY = 1;
        int placingAgent;
        //Minimum rzędów agentów do ustawienia
        int minrows = (int) Math.ceil(Math.sqrt((double) (preys + predators)));
        //ustaw ofiary
        for (placingAgent = 1; placingAgent <= preys; placingAgent++) {
            positions.add(placingAgent, new Position(width - placeX, placeY, AgentType.PREY, placingAgent));
            newPositions.add(placingAgent, new Position(width - placeX, placeY, AgentType.PREY, placingAgent));
            if (placeX < minrows - 1)
                placeX++;
            else {
                placeX = 0;
                placeY++;
            }
        }

        //ustaw łowców
        for (; placingAgent <= predators + preys; placingAgent++) {
            positions.add(placingAgent, new Position(width - placeX, placeY, AgentType.PREDATOR, placingAgent));
            newPositions.add(placingAgent, new Position(width - placeX, placeY, AgentType.PREDATOR, placingAgent));
            if (placeX < minrows - 1)
                placeX++;
            else {
                placeX = 0;
                placeY++;
            }
        }
    }

    /**
     * Funkcja sprawdza, czy można wykonać ruch.
     * @param agentNumber Numer agenta.
     * @param newPosition Pozycja na jaką chce się ruszyć.
     * @return True, jeśli agent może stanąć na danym polu.
     */
    public boolean prepareMove(int agentNumber, Position newPosition) {
        if (newPosition.getX() < 1 || newPosition.getX() > width || newPosition.getY() < 1
                || newPosition.getY() > height) {
            return false;
        }
        Position movedAgent;
        for (int i = 0; i < find(agentNumber); i++) {
            movedAgent = newPositions.get(i);
            if (newPosition.getX() == movedAgent.getX() && newPosition.getY() == movedAgent.getY()) {
                return newPosition.getAgentType().interacts(movedAgent.getAgentType());
            }
        }
        return true;
    }

    /**
     * Funkcja wyszukuje kolizji na mapie. Jeżeli znajdzie kolizję to cofa jednego z agentów i zwraca false.
     * @return True jeśli znajdzie kolizję.
     */
    public boolean findCollisions() {
        Position firstAgent;
        Position secondAgent;
        for (int i = 0; i < newPositions.size(); i++) {
            firstAgent = newPositions.get(i);
            for (int j = i + 1; j < newPositions.size(); j++) {
                secondAgent = newPositions.get(j);
                if (firstAgent.getX() == secondAgent.getX() && firstAgent.getY() == secondAgent.getY()) {
                    if (!firstAgent.getAgentType().interacts(secondAgent.getAgentType())) {
                        newPositions.set(j, positions.get(j));
                        newPositions.set(i, positions.get(i));
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Funkcja przygotowuje informacje o mapie dla agenta o podanym numerze.
     * @param agentNumber Numer agenta.
     * @return Mapa &lt;String, Object&gt; z informacjami o mapie dla agenta.
     */
    public Map<String, Object> getMapInfo(int agentNumber) {
        Map<String, Object> returnMap = new HashMap<>();

        Position myPosition = positions.get(find(agentNumber));
        List<Position> preys = new ArrayList<>();
        List<Position> predators = new ArrayList<>();

        Position hunter = new Position(0, 0, AgentType.HUNTER, 0);

        for (Position position : positions) {
            if (position == myPosition) {
            }
            else if (myPosition.inRange(position, range)) {
                insertPosition(position, preys, predators, hunter);
            }
        }
        returnMap.put("range", range);
        returnMap.put("position", myPosition);
        returnMap.put("width", width);
        returnMap.put("height", height);
        returnMap.put("lastMovePoints", hunterPoints);

        if (!preys.isEmpty()) returnMap.put("preys", preys);
        if (!predators.isEmpty()) returnMap.put("predators", predators);
        returnMap.put("hunter", hunter);

        return returnMap;
    }

    /**
     * Funkcja wstawia pozycję w odpowiednią listę przeznaczoną do przekazania agentowi.
     * @param position Pozycja do wstawienia.
     * @param preys Lista ofiar.
     * @param predator Lista drapieżników.
     * @param Hunter Łowca.
     */
    private void insertPosition(Position position, List<Position> preys, List<Position> predator, Position Hunter) {

        switch (position.getAgentType()) {
            case PREDATOR:
                predator.add(position);
                break;
            case PREY:
                preys.add(position);
                break;
            case HUNTER:
                Hunter.setX(position.getX());
                Hunter.setY(position.getY());
                Hunter.setAgentNumber(position.getAgentNumber());
                break;
        }
    }

    /**
     * Funkcja zapisuje do pliku z logami stan planszy.
     * @throws IOException W przypadku problemu z zapisem do pliku.
     */
    public void showMapInFile() throws IOException {
        for (int i = 0; i < positions.size(); i++) {
            outputStream.write((i + " " + positions.get(i) + "\n").getBytes());
        }
        Integer agentNumber;
        for (int i = height; i > 0; i--) {
            for (int j = 1; j <= width; j++) {
                if ((agentNumber = checkPositionForShow(j, i)) != null)
                    outputStream.write((positions.get(agentNumber).getAgentNumber() + "").getBytes());
                else
                    outputStream.write(' ');
            }
            outputStream.write('\n');
        }
    }

    /**
     * Funkcja sprawdza, czy w danym miejscu znajduje się agent.
     * @param x Współrzędna x na planszy.
     * @param y Współrzędna y na planszy.
     * @return Numer agenta.
     */
    public Integer checkPositionForShow(int x, int y) {
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getX() == x && positions.get(i).getY() == y) {
                return i;
            }
        }
        return null;
    }

    /**
     * Funkcja przemieszcza agentów na mapie na nowo wybrane pozycje.
     */
    public void move() {
        for (int i = 0; i < positions.size(); i++) {
            positions.set(i, newPositions.get(i));
        }
    }

    /**
     * Funkcja ustawia nową pozycję dla agenta.
     * @param agentNumber Numer agenta.
     * @param newPosition Pozycja.
     */
    public void insertNewPosition(int agentNumber, Position newPosition) {
        newPositions.set(find(agentNumber), newPosition);
    }

    /**
     * Funkcja ustawia starą pozycję agenta jako nową.
     * @param agentNumber Numer agenta.
     */
    public void takeOldPosition(int agentNumber) {
        newPositions.set(find(agentNumber), positions.get(find(agentNumber)));
    }

    /**
     * Funkcja podsumowuje wykonane ruchy. Wyszukuje agenta stojącego na tym samym polu co Łowca.
     * @param updateStatistics True, jeśli po znalezieniu agenta należy zaktualizować punkty przyznane łowcy.
     * @return Numer agenta stojącego na tym samym miejscu co łowca, lub null.
     * @throws IOException W przypadku problemów z zapisaniem logów.
     */
    public Integer concludeStep(boolean updateStatistics) throws IOException {
        for (int i = 1; i < positions.size(); i++) {
            if (positions.get(0).getX() == positions.get(i).getX()
                    && positions.get(0).getY() == positions.get(i).getY()) {

                if (positions.get(i).getAgentType() == AgentType.PREDATOR) {
                    outputStream.write(("Predator " + positions.get(i).getAgentNumber() + " ate Hunter").getBytes());
                    if (updateStatistics) {
                        hunterPoints = 2;
                    }
                    return 0;
                } else {
                    int removedAgentNumber = positions.get(i).getAgentNumber();
                    outputStream.write(("Hunter killed " + removedAgentNumber + " Prey").getBytes());
                    if (updateStatistics) {
                        hunterPoints = 1;
                    }
                    positions.remove(i);
                    newPositions.remove(i);
                    return removedAgentNumber;
                }
            }
        }
        if (updateStatistics) {
            hunterPoints = 0;
        }
        return null;
    }

    /**
     * Funkcja wyszukuje pozycji agenta na liście pozycji.
     * @param agentNumber Numer agenta.
     * @return Numer pozycji.
     */
    public int find(int agentNumber) {
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getAgentNumber() == agentNumber) {
                return i;
            }
        }
        return -1;
    }
}
