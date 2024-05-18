package com.alchemy;
import be.kuleuven.cs.som.annotate.*;
import com.alchemy.IngredientConditions.Temperature;
import com.alchemy.quantity.*;
import com.alchemy.IngredientConditions.IngredientState;

import java.util.ArrayList;
import java.util.List;
/**
 * A class representing a certain amount of a substance used to create potions and such.
 * @invar the name, temperature, type, state and quantity aren't null
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

    /**
     * Variable referencing the name of the ingredient
     */
    private final IngredientName ingredientName;

    /**
     * Variable referencing the Temperature of the ingredient
     */
    private Temperature temperature;

    /**
     * Variable referencing the State of the ingredient
     */
    private IngredientState state;

    /**
     * Variable referencing the IngredientType of the ingredient
     */
    private final IngredientType standardType;

    /**
     * Variable referencing the Quantity of the ingredient
     */
    private final Quantity quantity;



    /**********************************************************
     * Constructors
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
        this(new IngredientType(name,temperature,new IngredientState(state)),(float) quantity);
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
    public AlchemicIngredient(String name, Temperature temperature, IngredientState.State state, float quantity) throws IngredientName.IllegalNameException {
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
        this(new IngredientType(name,temperature,state),(float) quantity);
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
    public AlchemicIngredient(String name, Temperature temperature, IngredientState state, float quantity) throws IngredientName.IllegalNameException {
        this(new IngredientType(name,temperature,state), quantity);
    }

    /**
     * Initialize a new ingredient of an ingredientType with given variables
     *
     * @param name   The name of the ingredient.
     * @param temperature The standard temperature of the ingredient
     * @param state the standard state of the Ingredient
     * @param quantity The quantity of the ingredient
     * @throws IngredientName.IllegalNameException If the given name is not a valid mixture name.
     * @effect The name of the ingredient is set to the given name.
     * @effect The amount is set to the given amount (must be valid).
     * | setSize(size)
     */
    @Raw
    public AlchemicIngredient(String name, Temperature temperature, IngredientState state, Quantity quantity) throws IngredientName.IllegalNameException {
        this(new IngredientType(name,temperature,state), quantity);
    }

    /**
     * Create a given amount of given type ingredient
     * @param standardType the type of the ingredient
     * @param quantity The quantity of the ingredient
     */
    @Raw
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
    @Raw
    public AlchemicIngredient(IngredientType standardType, float quantity) {
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
    @Raw
    public AlchemicIngredient(Quantity quantity) {
        this(new IngredientType(), quantity);
    }

    /**
     * Create a given amount of standardType ingredient
     * @param quantity the amount of ingredient made in the smallest unit of the state it is in
     */
    @Raw
    public AlchemicIngredient(Float quantity) {
        this(new IngredientType(), quantity);
    }



    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**
     * Set the special name of the ingredient
     *
     * @param specialName the special name of the ingredient
     * @throws IngredientName.IllegalSpecialNameException if the special name is not a valid special name
     */
    public void setSpecialName(String specialName) throws IngredientName.IllegalSpecialNameException {
        this.ingredientName.setSpecialName(specialName);
    }

    /**
     * Get the basic name of the ingredient
     *
     * @return the basic name of the ingredient
     */
    @Basic
    public String getBasicName() {
        return ingredientName.getBasicName();
    }

    /**
     * Get the special name of the ingredient
     *
     * @return the special name of the ingredient
     */
    @Basic
    public String getSpecialName() {
        return ingredientName.getSpecialName();
    }

    /**
     * Get the full name of the ingredient
     *
     * @return the full name of the ingredient
     */
    @Basic
    public String getFullName() {
        updateFullName();
        return ingredientName.getFullName();
    }

    /**
     * Get the quantity of the ingredient
     *
     * @return the quantity of the ingredient
     */
    @Basic
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * Get the temperature of the ingredient
     *
     * @return the temperature of the ingredient
     */
    @Basic
    public Temperature getTemperature() {
        return temperature;
    }

    /**
     * Get the state of the ingredient
     *
     * @return the state of the ingredient
     */
    @Basic
    public IngredientState getState() {
        return state;
    }

    /**
     * Get the standard type of the ingredient
     *
     * @return the standard type of the ingredient
     */
    @Basic
    public IngredientType getStandardType(){
        return standardType;
    }

    /**
     * Get the prefixes of the ingredient name
     *
     * @return the prefixes of the ingredient name
     */
    @Basic
    private List<String> getPrefixes() {
        return ingredientName.getPrefixes();
    }

    /**
     * Get the suffixes of the ingredient name
     *
     * @return the suffixes of the ingredient name
     */
    @Basic
    private List<String> getSuffixes() {
        return ingredientName.getSuffixes();
    }

    /**
     * Get the part names of the ingredient name
     *
     * @return the part names of the ingredient name
     */
    @Basic
    public ArrayList<String> getParts() {
        return ingredientName.getPartNames();
    }



    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * Add a prefix to the name of the ingredient
     *
     * @param prefix the prefix to be added
     */
    private void addPrefix(String prefix) {
        ingredientName.addPrefix(prefix);
    }

    /**
     * Add a suffix to the name of the ingredient
     *
     * @param suffix the suffix to be added
     */
    private void addSuffix(String suffix) {
        ingredientName.addSuffix(suffix);
    }

    /**
     * Clear the prefixes and suffixes of the ingredient name
     */
    private void clearPreAndSuffixes() {
        ingredientName.clearPreAndSuffixes();
    }

    /**
     * Update the prefixes and suffixes of the ingredient name based on the temperature
     */
    @Raw
    private void updateFullName(){
        clearPreAndSuffixes();
        if (temperature.isColderThan(standardType.getStandardTemperature())) {
            addPrefix("Cooled ");
        } else if (temperature.isHotterThan(standardType.getStandardTemperature())) {
            addPrefix("Heated ");
        }
    }
}
