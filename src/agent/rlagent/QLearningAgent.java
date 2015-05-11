package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
import environnement.gridworld.ActionGridworld;
import environnement.gridworld.EtatGrille;
import java.util.Map;
/**
 * 
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent{
	
    //VOTRE CODE
    private Map<Etat,Map<Action,Double>> QV;

    /**
     * @param alpha
     * @param gamma
     * @param Environnement
     */
    public QLearningAgent(double alpha, double gamma, Environnement _env) {
        super(alpha, gamma,_env);
        //VOTRE CODE
        //...
        this.QV = new HashMap<>();
        reset();
    }
	
    /**
    * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e  
    *  renvoi liste vide si aucunes actions possibles dans l'etat 
    */
    @Override
    public ArrayList<Action> getPolitique(Etat e) {
        //VOTRE CODE
        ArrayList<Action> listAction = new ArrayList<>();
        double max = Double.MIN_VALUE;
        for(Action a : getEnv().getActionsPossibles(e)){
            double valeur = getQValeur(e, a);
            if (valeur > max){
                listAction.clear();
                listAction.add(a);
                max = valeur;
            } else if (valeur == max){
                listAction.add(a);
            }
        }
        return listAction;
    }
	
    /**
     * @return la valeur d'un etat
     */
    @Override
    public double getValeur(Etat e) {
        //VOTRE CODE
        double vmax = Double.MIN_VALUE;
        for (Action a : getEnv().getActionsPossibles(e)){
            vmax = Math.max(vmax,getQValeur(e, a));
        }
        return vmax;
    }

    /**
     * 
     * @param e
     * @param a
     * @return Q valeur du couple (e,a)
     */
    @Override
    public double getQValeur(Etat e, Action a) {
        //VOTRE CODE
        if (this.QV.containsKey(e)){
            if (this.QV.get(e).containsKey(a)){
                return this.QV.get(e).get(a);
            }
        }
        return 0.0;
    }
	
    /**
     * setter sur Q-valeur
     */
    @Override
    public void setQValeur(Etat e, Action a, double d) {
        //VOTRE CODE
        if (this.QV.containsKey(e)){
            this.QV.get(e).put(a, d);
        } else {
            Map<Action,Double> hm = new HashMap<>();
            hm.put(a,d);
            this.QV.put(e, hm);
       }

        //mise a jour vmin et vmax pour affichage gradient de couleur
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (Etat et : this.QV.keySet()){
            for (double valeur : this.QV.get(et).values()){
                min = Math.min(min,valeur);
                max = Math.max(max,valeur);
            }
        }
        super.vmax = max;
        super.vmin = min;

        this.notifyObs();	
    }
	
	
    /**
     * mise a jour de la Q-valeur du couple (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
     * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
     * @param e
     * @param a
     * @param esuivant
     * @param reward
     */
    @Override
    public void endStep(Etat e, Action a, Etat esuivant, double reward) {
        //VOTRE CODE
        if (a != ActionGridworld.EXIT){
            double val = (1-getAlpha()) * this.getQValeur(e, a) + getAlpha() * (reward + getGamma() * this.getValeur(esuivant));
            setQValeur(e, a, val);
        }
    }

    @Override
    public Action getAction(Etat e) {
        this.actionChoisie = this.stratExplorationCourante.getAction(e);
        return this.actionChoisie;
    }

    /**
     * reinitialise les Q valeurs
     */
    @Override
    public void reset() {
        this.episodeNb =0;
        //VOTRE CODE
        this.QV.clear();
        super.vmax = Double.MIN_VALUE;
        super.vmin = Double.MAX_VALUE;
    }



	


}
