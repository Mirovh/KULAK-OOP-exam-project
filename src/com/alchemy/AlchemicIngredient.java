package com.alchemy;
import be.kuleuven.cs.som.annotate.*;
import org.junit.Before;

/**
 * A class representing a certain amount of a substance used to create potions and such.
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 2.0
 */
public class AlchemicIngredient {

    /**********************************************************
     * Variables
     **********************************************************/

    private final Name name;

    private Temperature temperature;//TODO: set standardTemperature on construction

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new ingredient with a given amount and name.
     *
     * @param name   The name of the ingredient.
     * @param amount The amount of the ingredient.
     * @throws IllegalNameException If the given name is not a valid mixture name.
     * @effect The name of the ingredient is set to the given name.
     * @effect The amount is set to the given amount (must be valid).
     *         | setSize(size)
     */
    @Raw
    public AlchemicIngredient(String name, int amount) throws Name.IllegalNameException {
        this.name = new Name(name);
        temperature = new Temperature(0L, 20L);//TODO: Change to standardTemperature
        //setAmount(amount);
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    public void setName(String name) throws Name.IllegalNameException {
        this.name.setName(name);
    }

    public void setSpecialName(String specialName) throws Name.IllegalSpecialNameException {
        this.name.setSpecialName(specialName);
    }

    public String getBasicName() {
        return name.getBasicName();
    }

    public String getSpecialName() {
        return name.getSpecialName();
    }

    public String getFullName() {
        return name.getFullName();
    }

    public Integer getQuantity(){return 0;}//TODO: Change

    public Temperature getTemperature(){
        return temperature;
    }
    /**********************************************************
     * ingredientState
     **********************************************************/
    private IngredientState state;
    public enum IngredientState {
        Powder(true),Liquid(false);
        private final boolean solid;
        IngredientState(boolean solid){
            this.solid = solid;
        }
        public boolean isSolid(){return solid;}
    }
    @Basic
    public void switchState(){
        if(state.isSolid()){
            state = IngredientState.Liquid;
        }
        else{
            state = IngredientState.Powder;
        }
    }
    @Basic
    public IngredientState getState(){return this.state;}
    /**********************************************************
     * IngredientType - total programming
     **********************************************************/
}
