package com.alchemy.IngredientConditions;

import be.kuleuven.cs.som.annotate.Basic;

/**********************************************************
 * Class made to define the states of the ingredient
 * defensively programmed
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
    /**
     * constructor to make a state based on a boolean
     * @param solid whether the state is solid
     */
    public IngredientState(boolean solid){
        if(solid){
            state = State.Powder;
        }
        else{
            state = State.Liquid;
        }
    }

    /**
     * constructor to make a state
     * @param state whether the state is solid
     */
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
    @Basic
    public State getState(){return state;}
    @Basic
    public boolean isSolid(){return state.isSolid();}
    /**********************************************************
     * Mutators
     **********************************************************/
    /**
     * method to change the state to the other one (change from fluid to solid or the other way)
     * @effect state is changed.
     */
    void switchState(){
        if(state.isSolid()){
            state = State.Liquid;
        }
        else{
            state = State.Powder;
        }
    }

}
