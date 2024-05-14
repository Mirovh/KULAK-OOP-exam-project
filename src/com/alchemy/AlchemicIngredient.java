package com.alchemy;
import be.kuleuven.cs.som.annotate.*;
import com.alchemy.Temperature.Temperature;
import com.alchemy.quantity.Quantity;
import com.alchemy.transmorgrify.IngredientState;

import java.util.List;
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

    private Temperature temperature;

    private IngredientState state;

    private final IngredientType standardType;



    /**********************************************************
     * Constructors TODO: Implement quantity to constructor of ALchemicIngredient
     **********************************************************/

    /**
     * Initialize a new ingredient of an ingredientType with given variables
     *
     * @param name   The name of the ingredient.
     * @param temperature The standard temperature of the ingredient
     * @param state the standard state of the Ingredient
     * @param amount The amount of the ingredient.
     * @throws Name.IllegalNameException If the given name is not a valid mixture name.
     * @effect The name of the ingredient is set to the given name.
     * @effect The amount is set to the given amount (must be valid).
     * | setSize(size)
     */
    @Raw
    public AlchemicIngredient(String name, Temperature temperature, IngredientState.State state, int amount) throws Name.IllegalNameException {
        this(new IngredientType(name,temperature,new IngredientState(state)),amount);
    }

    /**
     * Initialize a new ingredient of an ingredientType with given variables
     *
     * @param name   The name of the ingredient.
     * @param temperature The standard temperature of the ingredient
     * @param state the standard state of the Ingredient
     * @param amount The amount of the ingredient.
     * @throws Name.IllegalNameException If the given name is not a valid mixture name.
     * @effect The name of the ingredient is set to the given name.
     * @effect The amount is set to the given amount (must be valid).
     * | setSize(size)
     */
    @Raw
    public AlchemicIngredient(String name, Temperature temperature, IngredientState state, int amount) throws Name.IllegalNameException {
        this(new IngredientType(name,temperature,state),amount);
    }

    /**
     * Create a given amount of given type ingredient
     * @param standardType the type of the ingredient
     * @param amount the amount of ingredient made
     */
    public AlchemicIngredient(IngredientType standardType, int amount) {
        this.standardType = standardType;
        this.name = standardType.getName();
        this.temperature = standardType.getStandardTemperature();
        this.state = standardType.getStandardState();
    }
    /**
     * Create a given amount of standardType ingredient
     * @param amount the amount of ingredient made
     */
    public AlchemicIngredient(int amount) {
        this(new IngredientType(),amount);
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
        updateFullName();
        return name.getFullName();
    }

    public Quantity getQuantity(){return null;}//TODO: Change

    public Temperature getTemperature(){return temperature;}
    @Basic
    public IngredientState getState(){return state;}


    /**********************************************************
     * Methods
     **********************************************************/

    private void addPrefix(String prefix) {
        name.addPrefix(prefix);
    }

    private void addSuffix(String suffix) {
        name.addSuffix(suffix);
    }

    private void clearPreAndSuffixes(){name.clearPreAndSuffixes();}

    private List<String> getPrefixes() {
        return name.getPrefixes();
    }

    private List<String> getSuffixes() {
        return name.getSuffixes();
    }

    private void updateFullName(){
        clearPreAndSuffixes();
            if(temperature.isColderThan(standardType.getStandardTemperature())){addPrefix("Cooled");}
            else if(temperature.isHotterThan(standardType.getStandardTemperature())){addPrefix("Heated");
            }
    }

    /**********************************************************
     * IngredientType - total programming
     **********************************************************/
}
