package com.alchemy;

import com.alchemy.Temperature.Temperature;
import com.alchemy.transmorgrify.IngredientState;

/**
 * A class representing a type for Ingredient
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class IngredientType {//TODO: Documentation
    /**********************************************************
     * Variables
     **********************************************************/
    final private Temperature standardTemperature;

    final private Name name;
    final private IngredientState standardState;
    /**********************************************************
     * Constructors
     **********************************************************/
    public IngredientType(String name, Temperature standardTemperature, IngredientState standardState) throws Name.IllegalNameException {
        this.standardTemperature = standardTemperature;
        this.name = new Name(name);
        this.standardState = standardState;
    }

    public IngredientType() {
        try {
            name = new Name("Water");
        } catch (Name.IllegalNameException e) {
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

    public Name getName() {
        return name;
    }

    public IngredientState getStandardState() {
        return standardState;
    }
}
