package com.alchemy.transmorgrify;

import be.kuleuven.cs.som.annotate.Basic;

/**********************************************************
 * Class made to define the states of the ingredient
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 **********************************************************/


public class IngredientState {
    /**********************************************************
     * Variables
     **********************************************************/
    private State state;
    /**********************************************************
     * Constructors
     **********************************************************/
    public IngredientState(boolean solid){
        if(solid){
            state = State.Powder;
        }
        else{
            state = State.Liquid;
        }
    }
    public IngredientState(State state){
        this.state = state;
    }
    /**
     * enum to keep track of the state of the ingredient
     */
    public enum State {
        Powder(true),Liquid(false);
        private final boolean solid;
        State(boolean solid){
            this.solid = solid;
        }
        public boolean isSolid(){return solid;}
    }
    /**********************************************************
     * Getters and Setters
     **********************************************************/
    public State getState(){return state;}
    /**********************************************************
     * Mutators
     **********************************************************/
    /**
     * @effect state is changed.
     */
    @Basic
    void switchState(){
        if(state.isSolid()){
            state = State.Liquid;
        }
        else{
            state = State.Powder;
        }
    }

}
