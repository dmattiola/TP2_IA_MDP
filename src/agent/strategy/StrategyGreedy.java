package agent.strategy;

import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;
import environnement.gridworld.ActionGridworld;
import java.util.ArrayList;
/**
 * Strategie qui renvoit une action aleatoire avec probabilite epsilon, une action gloutonne (qui suit la politique de l'agent) sinon
 * Cette classe a acces a un RLAgent par l'intermediaire de sa classe mere.
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration{
	
    //VOTRE CODE
    private double epsilon;
    private Random rand=new Random();

    public StrategyGreedy(RLAgent agent,double epsilon) {
        super(agent);
	//VOTRE CODE
	this.epsilon = epsilon;
    }

    /**
     * @return action selectionnee par la strategie d'exploration
     */
    @Override
    public Action getAction(Etat _e) {
        //VOTRE CODE
        List<Action> listAction = this.getAgent().getActionsLegales(_e);
        if (listAction.isEmpty()){
            return ActionGridworld.NONE;
        } else if (listAction.size() == 1){
            return listAction.get(0);
        } else {
            if (this.rand.nextDouble() <= this.epsilon){
                return listAction.get(this.rand.nextInt(listAction.size()));
            } else {
                listAction = this.getAgent().getPolitique(_e);
                return listAction.get(this.rand.nextInt(listAction.size()));
            }
        } 
    }

    public void setEpsilon(double epsilon) {
        //VOTRE CODE
        this.epsilon = epsilon;
    }



}
