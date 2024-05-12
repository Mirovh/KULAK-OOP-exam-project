package com.alchemy;
import be.kuleuven.cs.som.annotate.*;
import com.alchemy.Temperature.Temperature;
import com.alchemy.transmorgrify.IngredientState;
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

    private final Name name;

    private Temperature temperature;//TODO: set standardTemperature on construction

    private IngredientState state;

    private final IngredientType standardType;



    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new ingredient with a given amount and name.
     *
     * @param name   The name of the ingredient.
     * @param amount The amount of the ingredient.
     * @throws Name.IllegalNameException If the given name is not a valid mixture name.
     * @effect The name of the ingredient is set to the given name.
     * @effect The amount is set to the given amount (must be valid).
     * | setSize(size)
     */
    @Raw
    public AlchemicIngredient(String name, Temperature temperature, IngredientState.State state, int amount) throws Name.IllegalNameException {
        this(new IngredientType(name,temperature,new IngredientState(state)));
    }
    @Raw
    public AlchemicIngredient(String name, Temperature temperature, IngredientState state, int amount) throws Name.IllegalNameException {
        this(new IngredientType(name,temperature,state));
    }


    public AlchemicIngredient(IngredientType standardType) {
        this.standardType = standardType;
        this.name = standardType.getName();
        this.temperature = standardType.getStandardTemperature();
        this.state = standardType.getStandardState();
    }
    public AlchemicIngredient() {
        this(new IngredientType());
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

    private void removePrefix(String prefix) {
        name.removePrefix(prefix);
    }

    private void removeSuffix(String suffix) {
        name.removeSuffix(suffix);
    }

    private List<String> getPrefixes() {
        return name.getPrefixes();
    }

    private List<String> getSuffixes() {
        return name.getSuffixes();
    }

    /**********************************************************
     * IngredientType - total programming
     **********************************************************/
}
