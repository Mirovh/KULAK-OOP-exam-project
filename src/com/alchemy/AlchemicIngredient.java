package com.alchemy;
import be.kuleuven.cs.som.annotate.*;
import com.alchemy.IngredientConditions.Temperature;
import com.alchemy.quantity.*;
import com.alchemy.IngredientConditions.IngredientState;

import java.util.ArrayList;
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

    private final IngredientName ingredientName;

    private Temperature temperature;

    private IngredientState state;

    private final IngredientType standardType;

    private final Quantity quantity;



    /**********************************************************
     * Constructors TODO: Implement quantity to constructor of ALchemicIngredient
     **********************************************************/

    /**
     * Initialize a new ingredient of an ingredientType with given variables
     *
     * @param name   The name of the ingredient.
     * @param temperature The standard temperature of the ingredient
     * @param state the standard state of the Ingredient
     * @param quantity The amount of the ingredient.
     * @throws IngredientName.IllegalNameException If the given name is not a valid mixture name.
     * @effect The name of the ingredient is set to the given name.
     * @effect The amount is set to the given amount (must be valid).
     * | setSize(size)
     */
    @Raw
    public AlchemicIngredient(String name, Temperature temperature, IngredientState.State state, int quantity) throws IngredientName.IllegalNameException {
        this(new IngredientType(name,temperature,new IngredientState(state)),(long) quantity);
    }

    /**
     * Initialize a new ingredient of an ingredientType with given variables
     *
     * @param name   The name of the ingredient.
     * @param temperature The standard temperature of the ingredient
     * @param state the standard state of the Ingredient
     * @param quantity The amount of the ingredient.
     * @throws IngredientName.IllegalNameException If the given name is not a valid mixture name.
     * @effect The name of the ingredient is set to the given name.
     * @effect The amount is set to the given amount (must be valid).
     * | setSize(size)
     */
    @Raw
    public AlchemicIngredient(String name, Temperature temperature, IngredientState.State state, long quantity) throws IngredientName.IllegalNameException {
        this(new IngredientType(name,temperature,new IngredientState(state)), quantity);
    }

    /**
     * Initialize a new ingredient of an ingredientType with given variables
     *
     * @param name   The name of the ingredient.
     * @param temperature The standard temperature of the ingredient
     * @param state the standard state of the Ingredient
     * @param quantity The amount of the ingredient in the smallest unit of the state it is in.
     * @throws IngredientName.IllegalNameException If the given name is not a valid mixture name.
     * @effect The name of the ingredient is set to the given name.
     * @effect The amount is set to the given amount (must be valid).
     * | setSize(size)
     */
    @Raw
    public AlchemicIngredient(String name, Temperature temperature, IngredientState state, int quantity) throws IngredientName.IllegalNameException {
        this(new IngredientType(name,temperature,state),(long) quantity);
    }

    /**
     * Initialize a new ingredient of an ingredientType with given variables
     *
     * @param name   The name of the ingredient.
     * @param temperature The standard temperature of the ingredient
     * @param state the standard state of the Ingredient
     * @param quantity The amount of the ingredient in the smallest unit of the state it is in.
     * @throws IngredientName.IllegalNameException If the given name is not a valid mixture name.
     * @effect The name of the ingredient is set to the given name.
     * @effect The amount is set to the given amount (must be valid).
     * | setSize(size)
     */
    @Raw
    public AlchemicIngredient(String name, Temperature temperature, IngredientState state, long quantity) throws IngredientName.IllegalNameException {
        this(new IngredientType(name,temperature,state), quantity);
    }

    /**
     * Create a given amount of given type ingredient
     * @param standardType the type of the ingredient
     * @param quantity The quantity of the ingredient
     */
    public AlchemicIngredient(IngredientType standardType, Quantity quantity) {
        this.standardType = standardType;
        this.ingredientName = standardType.getName();
        this.temperature = standardType.getStandardTemperature();
        this.state = standardType.getStandardState();
        this.quantity = quantity;
    }
    /**
     * Create a given amount of given type ingredient
     * @param standardType the type of the ingredient
     * @param quantity The amount of the ingredient in the smallest unit of the state it is in.
     */
    public AlchemicIngredient(IngredientType standardType, long quantity) {
        this.standardType = standardType;
        this.ingredientName = standardType.getName();
        this.temperature = standardType.getStandardTemperature();
        this.state = standardType.getStandardState();
        if(standardType.getStandardState().getState().isSolid()){
            this.quantity = new Quantity(quantity, PowderUnit.PINCH);
        }
        else{
            this.quantity = new Quantity(quantity, FluidUnit.DROP);
        }
    }

    /**
     * Create a given amount of standardType ingredient
     * @param quantity the amount of ingredient made
     */
    public AlchemicIngredient(Quantity quantity) {
        this(new IngredientType(), quantity);
    }
    /**
     * Create a given amount of standardType ingredient
     * @param quantity the amount of ingredient made in the smallest unit of the state it is in
     */
    public AlchemicIngredient(Long quantity) {
        this(new IngredientType(), quantity);
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    public void setName(String name) throws IngredientName.IllegalNameException {
        this.ingredientName.setName(name);
    }

    public void setSpecialName(String specialName) throws IngredientName.IllegalSpecialNameException {
        this.ingredientName.setSpecialName(specialName);
    }

    public String getBasicName() {
        return ingredientName.getBasicName();
    }

    public String getSpecialName() {
        return ingredientName.getSpecialName();
    }

    public String getFullName() {
        updateFullName();
        return ingredientName.getFullName();
    }

    public Quantity getQuantity(){return quantity;}//TODO: Change

    public Temperature getTemperature(){return temperature;}
    @Basic
    public IngredientState getState(){return state;}

    public IngredientType getStandardType(){
        return standardType;
    }


    /**********************************************************
     * Methods
     **********************************************************/

    private void addPrefix(String prefix) {
        ingredientName.addPrefix(prefix);
    }

    private void addSuffix(String suffix) {
        ingredientName.addSuffix(suffix);
    }

    private void clearPreAndSuffixes(){
        ingredientName.clearPreAndSuffixes();}

    private List<String> getPrefixes() {
        return ingredientName.getPrefixes();
    }

    private List<String> getSuffixes() {
        return ingredientName.getSuffixes();
    }

    public ArrayList<String> getParts(){return ingredientName.getPartNames();}

    private void updateFullName(){
        clearPreAndSuffixes();
            if(temperature.isColderThan(standardType.getStandardTemperature())){addPrefix("Cooled ");}
            else if(temperature.isHotterThan(standardType.getStandardTemperature())){addPrefix("Heated ");
            }
    }

    /**********************************************************
     * IngredientType - total programming
     **********************************************************/
}
