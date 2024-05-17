package com.alchemy;

import be.kuleuven.cs.som.annotate.Raw;
import com.alchemy.IngredientConditions.Temperature;
import com.alchemy.IngredientConditions.IngredientState;

/**
 * A class representing a type for Ingredient, which stores some final variables for an ingredient
 * @invar none of the variables in IngredientType are null
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class IngredientType {
    /**********************************************************
     * Variables
     **********************************************************/
    /**
     * The standard temperature of the ingredient, also the temperature the ingredient will have on construction.
     */
    final private Temperature standardTemperature;
    /**
     * The name of the ingredient
     */
    final private IngredientName ingredientName;
    /**
     * The state the ingredient will start in
     */
    final private IngredientState standardState;
    /**********************************************************
     * Constructors //
     **********************************************************/
    /**
     * initializes an IngredientType with given parameters
     * @param name the name of the IngredientType
     * @param standardTemperature the standard Temperature of the IngredientType
     * @param standardState the standardState of the IngredientType
     * @throws IngredientName.IllegalNameException is thrown if the name is invalid
     */
    @Raw
    public IngredientType(String name, Temperature standardTemperature, IngredientState standardState) throws IngredientName.IllegalNameException {
        this.standardTemperature = standardTemperature;
        this.ingredientName = new IngredientName(name);
        this.standardState = standardState;
    }
    /**
     * initializes an IngredientType with given parameters
     * @param name the name of the IngredientType
     * @param standardTemperature the standard Temperature of the IngredientType
     * @param standardState the standardState of the IngredientType
     * @throws IngredientName.IllegalNameException is thrown if the name is invalid
     */
    @Raw
    public IngredientType(IngredientName name, Temperature standardTemperature, IngredientState standardState) throws IngredientName.IllegalNameException {
        this.standardTemperature = standardTemperature;
        this.ingredientName = name;
        this.standardState = standardState;
    }

    /**
     * this is the standardIngredient, which is Water with a standardTemperature of [0,20] and a standard liquid state
     */
    @Raw
    public IngredientType() {
        try {
            ingredientName = new IngredientName("Water");
        } catch (IngredientName.IllegalNameException e) {
            throw new RuntimeException(e);
        }
        standardTemperature = new Temperature(0L,20L);
        standardState = new IngredientState(IngredientState.State.Liquid);
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/
    public Temperature getStandardTemperature() {
        return standardTemperature;
    }

    public IngredientName getName() {
        return ingredientName;
    }

    public IngredientState getStandardState() {
        return standardState;
    }
}
