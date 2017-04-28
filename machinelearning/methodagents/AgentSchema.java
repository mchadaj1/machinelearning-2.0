import com.example.hunterPreyPredator.agents.AgentBase;
import com.example.hunterPreyPredator.map.Position;
import com.example.hunterPreyPredator.agents.AgentType;
import java.util.Map;
import java.util.List;
import com.example.hunterPreyPredator.map.Position;
import weka.*;
#Imports

public class #ClassName extends com.example.hunterPreyPredator.agents.AgentBase {

    #Globals

    public #ClassName(Map<String, String> attributes)
    {
        super(0);
        #Constructor
    }

    @Override
    public Position nextStep(Map<String, Object> mapInfo) {

        #nextStepMethod
    }

    @Override
    public AgentType getType() {
        return AgentType.HUNTER;
    }

    @Override
    public void finishGame(Map<String, Object> mapInfo) {
        #finishGame
    }
}
