import com.example.hunterPreyPredator.agents.AgentBase;
import com.example.hunterPreyPredator.map.Position;
import com.example.hunterPreyPredator.agents.AgentType;
import java.util.Map;
import java.util.List;
import com.example.hunterPreyPredator.map.Position;
import weka.*;
import algorithms.QLearningSelector;
import environment.*;

import java.util.List;
import java.util.Map;

public class a0nuueo5or72fe1r9ughuj7tur extends com.example.hunterPreyPredator.agents.AgentBase {

    
    QLearningSelector selector;
    double epsilon;
    private HunterAction selectedAction;
    private HunterState state;
    Position position;
    int width;
    int height;
    int i = 0;
    double epsilonMultiplyer;

    private void prepareState(int range, int lastStepResult, Distance distanceToPrey, Distance distanceToPred) {
        //pred = 2; prey = 1
        if(lastStepResult == 1){
            state = new HunterState(0, 0, 1, range, new HunterEnvironment());
        } else if(lastStepResult == 2){
            state = new HunterState(0, 0, 2, range, new HunterEnvironment());
        } else if (distanceToPred == null && distanceToPrey == null) {
            state = new HunterState(0, 0, 0, range, new HunterEnvironment());
        } else if (distanceToPred == null || (distanceToPrey != null && (Math.pow(distanceToPred.getDx(), 2) + Math.pow(distanceToPred.getDy(), 2)) > (Math.pow(distanceToPrey.getDx(), 2) + Math.pow(distanceToPrey.getDy(), 2)))) {
            state = new HunterState(distanceToPrey.dx, distanceToPrey.dy, 1, range, new HunterEnvironment());
        } else {
            state = new HunterState(distanceToPred.dx, distanceToPred.dy, 2, range, new HunterEnvironment());
        }
    }

    private ActionList generateActionList(IState state) {
        ActionList actionList = new ActionList(state);
        if(position.getX() > 1) {
            actionList.add(new HunterAction(-1, 0));
        }
        if(position.getY() < height) {
            actionList.add(new HunterAction(0, 1));
        }
        if(position.getX() < width) {
            actionList.add(new HunterAction(1, 0));
        }
        if(position.getY() > 1) {
            actionList.add(new HunterAction(0, -1));
        }
        if(position.getY() > 1 && position.getX() > 1) {
            actionList.add(new HunterAction(-1, -1));
        }
        if(position.getX() < width && position.getY() > 1) {
            actionList.add(new HunterAction(1, -1));
        }
        if(position.getX() < width && position.getY() < height) {
            actionList.add(new HunterAction(1, 1));
        }
        if(position.getX() > 1 && position.getY() < height) {
            actionList.add(new HunterAction(-1, 1));
        }
        actionList.add(new HunterAction(0, 0));
        return actionList;
    }

    private void learn(HunterState previousState, HunterAction previousAction, HunterState state, double reward) {
        if(previousState != null) {
            selector.learn(previousState, state, previousAction, reward);
        }
    }

    private double calculateReward(int lastStepResult) {
        double reward = 0;
        if (lastStepResult == 0) {
            reward = -0.01;
        } else if (lastStepResult == 1) {
            reward = 1;
//            System.out.println("reward: " + reward);
        } else if (lastStepResult == 2) {
            reward = -2;
//            System.out.println("reward: " + reward);
        }
        return reward;
    }

    private Distance distanceToClosest(List<Position> agents, Position position) {
        double closestDist = 100000.0;
        Position closest = null;
        if(agents != null) {
            for (Position p : agents) {
                double dist = Math.pow(p.getX() - position.getX(), 2) + Math.pow(p.getY() - position.getY(), 2);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = p;
                }
            }
            if (closest != null) {
                return new Distance(closest.getX() - position.getX(), closest.getY() - position.getY());
            }
        }
        return null;
    }

    public class Distance {
        int dx;
        int dy;

        public Distance(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }
    }

    public class HunterState extends AbstractState {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private final int agentType; //1 or 2
        private final int range;
        protected int x, y;


        /**
         * @param z       first coordinate
         * @param t       second coordinate
         * @param univers the maze
         */
        public HunterState(int z, int t, int agentType, int range, HunterEnvironment univers) {
            super(univers);
            if(z > range){
                System.out.println("!!!!!!!!!!!!!!!!!!!!");
            }
            if(t > range){
                System.out.println("?????????????????????");
            }

            this.x = z;
            this.y = t;
            this.range = range;
            this.agentType = agentType;
        }


        public IState copy() {
            return new HunterState(x, y, agentType, range, (HunterEnvironment) myEnvironment);
        }


        /**
         * @return true if this state is a wall
         */

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String toString() {
            String a = null;
            if(agentType == 0){
                a = "none";
            } else if (agentType == 1){
                a = "prey";
            } else if (agentType == 2){
                a = "pred";
            }
            return "(" + x + "," + y + ") " + a;
        }

        public int hashCode() {
            return (this.x + range) * 100 + this.y * 10 + agentType;
        }

        public boolean equals(Object o) {
            if (!(o instanceof HunterState)) return false;
            HunterState el = (HunterState) o;
            return ((el.x == this.x) && (el.y == this.y) && (el.agentType == this.agentType));
        }


        /**
         * Maze is a one player problem
         */
        public boolean getTurn() {
            return false;
        }

        public int nnCodingSize() {
            return 2 * (2 * range + 1) + 1;
        }

        public double[] nnCoding() {
            int width = 2 * range + 1;
            double code[] = new double[2 * width + 1];
            code[(x + range) % width] = 1.0;
            code[((y + range) % width) + width] = 1.0;
            code[2 * width] = agentType;
            return code;
        }
    }

    public class HunterEnvironment extends AbstractEnvironmentSingle {

        @Override
        public ActionList getActionList(IState s) {
            return generateActionList(s);
        }

        @Override
        public IState successorState(IState s, IAction a) {
            System.out.println("???????????????????????");
            return null;
        }

        @Override
        public IState defaultInitialState() {
            System.out.println("----------------------------");
            return null;
        }

        @Override
        public double getReward(IState s1, IState s2, IAction a) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
            return 0;
        }

        @Override
        public boolean isFinal(IState s) {
            return false;
        }

        @Override
        public int whoWins(IState s) {
            return 0;
        }
    }

    private class HunterAction implements IAction {
        private final int dx;
        private final int dy;

        public HunterAction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public Object copy() {
            return new HunterAction(dx,dy);
        }

        @Override
        public int nnCodingSize() {
            return 9;
        }

        @Override
        public double[] nnCoding() {
            double code[] = new double[9];
            code[3 * (dx + 1) + dy + 1] = 1.0;
            return code;
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }

        @Override
        public String toString(){
            return "(" + dx + "," + dy + ")";
        }

        @Override
        public int hashCode(){
            return dx * 10000 + dy;
        }


        @Override
        public boolean equals(Object o){
            if (!(o instanceof HunterAction)) return false;
            HunterAction el = (HunterAction) o;
            return ((el.dx == this.dx) && (el.dy == this.dy));
        }

    }


    public a0nuueo5or72fe1r9ughuj7tur(Map<String, String> attributes)
    {
        super(0);
        selector = new QLearningSelector();
        epsilon = 0.9;
        
double alphaDecayPower = Double.parseDouble(attributes.get("alphaDecayPower"));
epsilon = Double.parseDouble(attributes.get("epsilon"));
double alpha = Double.parseDouble(attributes.get("alpha"));
double decay = Double.parseDouble(attributes.get("decay"));
double gamma = Double.parseDouble(attributes.get("gamma"));
selector.setEpsilon(epsilon);
        selector.setAlphaDecayPower(alphaDecayPower);
        selector.setAlpha(alpha);
        selector.setGamma(gamma);
        epsilonMultiplyer = Double.parseDouble(attributes.get("epsilonMultiplyer"));
        if (false) {
            selector.setDecay(decay);
            selector.setGeometricAlphaDecay();
        }
    }

    @Override
    public Position nextStep(Map<String, Object> mapInfo) {

        HunterState previousState = state;
        HunterAction previousAction = selectedAction;
        selectedAction = null;
        state = null;
        position = (Position) mapInfo.get("position");
        final int range = (int) mapInfo.get("range");
        width = (int) mapInfo.get("width");
        height = (int) mapInfo.get("height");
        final int lastStepResult = (int) mapInfo.get("lastMovePoints");

        Distance distanceToPrey = distanceToClosest((List<Position>) mapInfo.get("preys"), position);
        Distance distanceToPred = distanceToClosest((List<Position>) mapInfo.get("predators"), position);

        prepareState(range, lastStepResult, distanceToPrey, distanceToPred);

        learn(previousState, previousAction, state, calculateReward(lastStepResult));

        ActionList actionList = generateActionList(state);

        selectedAction = (HunterAction) selector.getChoice(actionList);
        position.setX(position.getX() + selectedAction.getDx());
        position.setY(position.getY() + selectedAction.getDy());

        return position;
    
    }

    @Override
    public AgentType getType() {
        return AgentType.HUNTER;
    }

    @Override
    public void finishGame(Map<String, Object> mapInfo) {
                HunterState previousState = state;
        HunterAction previousAction = selectedAction;
        selectedAction = null;
        state = null;
        position = (Position) mapInfo.get("position");
        final int range = (int) mapInfo.get("range");
        width = (int) mapInfo.get("width");
        height = (int) mapInfo.get("height");
        final int lastStepResult = (int) mapInfo.get("lastMovePoints");

        Distance distanceToPrey = distanceToClosest((List<Position>) mapInfo.get("preys"), position);
        Distance distanceToPred = distanceToClosest((List<Position>) mapInfo.get("predators"), position);

        prepareState(range, lastStepResult, distanceToPrey, distanceToPred);

        learn(previousState, previousAction, state, calculateReward(lastStepResult));
        state = null;
        selector.newEpisode();
        epsilon *= epsilonMultiplyer;
        selector.setEpsilon(epsilon);
        i++;
        System.out.println("simulation " + i);
//        selector.showHistogram();
        selector.printBestActions();

    }
}
