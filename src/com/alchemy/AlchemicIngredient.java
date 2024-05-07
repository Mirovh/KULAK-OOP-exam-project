package com.alchemy;

import be.kuleuven.cs.som.annotate.*;

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

    public Integer getQuantity(){return null;}
    /**********************************************************
     * IngredientType - total programming
     **********************************************************/
}
