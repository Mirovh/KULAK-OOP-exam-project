package com.alchemy.IngredientConditions;
import com.alchemy.*;
import com.alchemy.IngredientConditions.Temperature;
import com.alchemy.quantity.FluidUnit;
import com.alchemy.quantity.PowderUnit;
import com.alchemy.quantity.Quantity;
import com.alchemy.quantity.Unit;

import java.util.ArrayList;
import java.util.Objects;

/**********************************************************
 * A class representing a kettle, used to mix multiple ingredients
 * defensively programmed
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
**********************************************************/
public class Kettle extends Device {
    /**********************************************************
     * Variables
     **********************************************************/
    private ArrayList<AlchemicIngredient> ingredients;

    static Temperature targetTemp = new Temperature(0,20);

    /**********************************************************
     * Constants
     **********************************************************/

    static final int pinchInSpoon = Math.round(new Quantity(1, PowderUnit.PINCH).convertToPowderUnit(PowderUnit.SPOON));
    static final int dropInSpoon = Math.round(new Quantity(1, FluidUnit.DROP).convertToFluidUnit(FluidUnit.SPOON));

    /**********************************************************
     * Constructors
     **********************************************************/

    /**********************************************************
     * Getters and Setters
     **********************************************************/
    /**
     * method used to add an ingredient to the device
     * @param container the container containing the ingredient that has to be added to the device
     */
    @Override
    public void addIngredient(IngredientContainer container){
        ingredients.add(container.getContent());
        container.destroy();
    }

    /**
     * remove the contents of the device and return the smallest possible containers each containing a different ingredient
     * @return arraylist of containers with an element for each ingredient with the smallest possible container containing the contents of the device. If quantity is too large, biggest possible
     * container is returned and rest of contents are destroyed.
     */
    public ArrayList<IngredientContainer> getContent(){
        ArrayList<IngredientContainer> containers = new ArrayList<>();
        for(AlchemicIngredient ingredient: ingredients){
            Unit containerUnit;
            if(ingredient.getState().isSolid()){
                containerUnit = ingredient.getQuantity().getSmallestPowderContainer();
            }
            else{
                containerUnit = ingredient.getQuantity().getSmallestFluidContainer();
            }
            containers.add(new IngredientContainer(ingredient,containerUnit));
        }
        ingredients.clear();
        return containers;
    }


    /**********************************************************
     * Mutators
     **********************************************************/

    /**
     * method to start the reaction
     * @effect the contents will be changed to a new ingredient with the details specified in newName(), newState(), newQuantity(), newTemp() and newStandardTemp()
     */
    @Override
    public void react() throws NotInLaboratoryException{
        IngredientType newIngredientType;
        if(!isInLaboratory()){
            throw new NotInLaboratoryException("Kettle not in Laboratory");
        }
        else{
            if(ingredients.size() <2){
                return; //no ingredients to mix
            }
            IngredientName newName = newName();
            IngredientState newState = newState();
            Quantity newQuantity = newQuantity(newState);
            Temperature newTemp = newTemp(newQuantity);
            Temperature standardTemp = newStandardTemp();
            try {
                newIngredientType = new IngredientType(newName,newTemp,newState);
            } catch (IngredientName.IllegalNameException e) {
                throw new RuntimeException(e);
            }
            AlchemicIngredient newIngredient = new AlchemicIngredient(newIngredientType,newQuantity);
            if(standardTemp.isColderThan(newTemp)){
                newIngredient.getTemperature().heat(standardTemp.differenceFrom(newTemp));
            }
            else{
                newIngredient.getTemperature().cool(standardTemp.differenceFrom(newTemp));
            }
            ingredients.clear();
            ingredients.add(newIngredient);
        }

    }

    /**
     * function used to get the new name of the mixture, which can be calculated by adding every unique ingredient with the right infixes
     * @return the new Name
     */
    private IngredientName newName(){
        ArrayList<String> strippedNames = new ArrayList<>();
        for(AlchemicIngredient ingredient: ingredients){
            for(String part:ingredient.getParts()){
                if(!strippedNames.contains(part)){
                    strippedNames.add(part);
                }
            }
        }
        if(strippedNames.size()>1){
            strippedNames.sort(String.CASE_INSENSITIVE_ORDER);
            StringBuilder name = new StringBuilder(strippedNames.getFirst() + " mixed with ");
            for (int i = 1; i < strippedNames.size() -1; i++) {
                name.append(strippedNames.get(i)).append(" , ");
            }
            name.append(strippedNames.getLast()).append(" and ");
            name.append(strippedNames.getLast());
            try {
                return new IngredientName(name.toString());
            } catch (IngredientName.IllegalNameException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                return new IngredientName(strippedNames.getFirst());
            } catch (IngredientName.IllegalNameException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * function used to get the new state of the mixture, which is the state of the ingredient closest to [0,20], in case of equals fluid wins.
     * @return the new state
     */
    private IngredientState newState(){
        Long smallestDiff = ingredients.getFirst().getTemperature().differenceFrom(targetTemp);
        IngredientState newState = new IngredientState(true); //powder is overwritten by fluid
        for(AlchemicIngredient ingredient: ingredients){
            Long diff = ingredient.getTemperature().differenceFrom(targetTemp);
            if(diff<smallestDiff){
                smallestDiff = diff;
                newState = ingredient.getState();
            } else if (diff.equals(smallestDiff) && !ingredient.getState().getState().isSolid()) {
                newState = new IngredientState(false);
            }
        }
        return newState;
    }

    /**
     * function used to get the new quantity of the mixture, here the smallest units of the ingredients of the opposite state can be destroyed if they can't fit in a full spoon
     * @param state the state the mixture will be in
     * @return the quantity of the mixture
     */
    private Quantity newQuantity(IngredientState state) {

        Quantity newQuantity;

        if(state.getState().isSolid()){
            Long pinches = 0L;
            int liquidFractions = 0;
            for(AlchemicIngredient ingredient: ingredients){
                if(ingredient.getQuantity().isGreaterThanOrEqualTo(PowderUnit.SPOON)|ingredient.getState().getState().isSolid()){
                    pinches += ingredient.getQuantity().convertToPowderUnit(PowderUnit.PINCH);
                }
                else{
                    liquidFractions += ingredient.getQuantity().convertToFluidUnit(FluidUnit.DROP);
                }
            }
            pinches += (long) ((liquidFractions -(liquidFractions%dropInSpoon))/dropInSpoon)*pinchInSpoon;
            newQuantity = new Quantity(pinches,PowderUnit.PINCH);
        }
        else{
            Long drops = 0L;
            int solidFractions = 0;
            for(AlchemicIngredient ingredient: ingredients){
                if(ingredient.getQuantity().isGreaterThanOrEqualTo(PowderUnit.SPOON)|!ingredient.getState().getState().isSolid()){
                    drops += ingredient.getQuantity().convertToFluidUnit(FluidUnit.DROP);
                }
                else{
                    solidFractions += ingredient.getQuantity().convertToPowderUnit(PowderUnit.PINCH);
                }
            }
            drops += (long) ((solidFractions - (solidFractions % pinchInSpoon)) / pinchInSpoon) *dropInSpoon;
            newQuantity = new Quantity(drops,FluidUnit.DROP);
        }
        return newQuantity;
    }

    /**
     * function used to get the new Temperature of the mixture, which is the weighted sum of the temperatures of the ingredients;
     * @param newQuantity the total quantity of the mixture
     * @return the new Temperature of the mixture
     */
    private Temperature newTemp(Quantity newQuantity){
        Long spoons = newQuantity.convertToFluidUnit(FluidUnit.SPOON);
        long temperature = 0L;
        Temperature newTemperature;
        for(AlchemicIngredient ingredient: ingredients){
            Temperature ingredientTemp = ingredient.getTemperature();
            Quantity ingredientQuantity = ingredient.getQuantity();
            if(ingredientQuantity.isGreaterThanOrEqualTo(FluidUnit.SPOON)){
                temperature += (ingredientTemp.getHotness() -ingredientTemp.getColdness())*(ingredientQuantity.convertToFluidUnit(FluidUnit.SPOON)/spoons);
            }
            else if(ingredient.getState().getState().isSolid()){
                temperature += (ingredientTemp.getHotness() -ingredientTemp.getColdness())*(ingredientQuantity.convertToPowderUnit(PowderUnit.PINCH)/(spoons*pinchInSpoon));
            }
            else{
                temperature += (ingredientTemp.getHotness() -ingredientTemp.getColdness())*(ingredientQuantity.convertToFluidUnit(FluidUnit.DROP)/(spoons*dropInSpoon));
            }
        }
        if(temperature > 0){
            newTemperature = new Temperature(0L,temperature);
        }
        else{
            newTemperature = new Temperature(-temperature,0L);
        }
        return newTemperature;
    }

    /**
     * function used to get the new standard Temperature of the mixture, which is the standardtemperature of the ingredient closest to [0,20], in case of equal closeness the hottest temperature wins
     * @return the new standard temperature of the mixture
     */
    private Temperature newStandardTemp(){
        Temperature newTemperature = ingredients.getFirst().getStandardType().getStandardTemperature();
        Long smallestDiff = ingredients.getFirst().getStandardType().getStandardTemperature().differenceFrom(targetTemp);
        for(AlchemicIngredient ingredient: ingredients){
            Temperature temp = ingredient.getStandardType().getStandardTemperature();
            Long diff = temp.differenceFrom(targetTemp);
            if(diff<smallestDiff){
                smallestDiff = diff;
                newTemperature = temp;
            } else if (diff.equals(smallestDiff) && temp.isHotterThan(newTemperature)) {
                newTemperature = temp;
            }
        }
        return newTemperature;
    }
}
