package com.alchemy.IngredientConditions;
import com.alchemy.*;
import com.alchemy.quantity.FluidUnit;
import com.alchemy.quantity.PowderUnit;
import com.alchemy.quantity.Quantity;
import java.util.ArrayList;

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
    private ArrayList<AlchemicIngredient> ingredients = new ArrayList<>();

    static Temperature targetTemp = new Temperature(0,20);

    /**********************************************************
     * Constants
     **********************************************************/

    static final int pinchInSpoon = Math.round(new Quantity(1, PowderUnit.SPOON).convertTo(PowderUnit.PINCH));
    static final int dropInSpoon = Math.round(new Quantity(1, FluidUnit.SPOON).convertTo(FluidUnit.DROP));

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
     * remove the contents of the device and return the smallest possible container of the last added ingredient
     * @return arraylist of containers with an element for each ingredient with the smallest possible container containing the contents of the device. If quantity is too large, biggest possible
     * container is returned and rest of contents are destroyed.
     */
    @Override
    public IngredientContainer getContents(){
        if(ingredients ==null){
            return null;
        }
        else if( ingredients.isEmpty()){
            return null;
        }
        AlchemicIngredient toAdd = ingredients.getLast();
        IngredientContainer containers;
        if(toAdd.getQuantity().isPowderUnit()) {
            containers = new IngredientContainer(toAdd, toAdd.getQuantity().getSmallestPowderContainer());
        }
        else{
            containers = new IngredientContainer(toAdd, toAdd.getQuantity().getSmallestFluidContainer());
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
                newIngredientType = new IngredientType(newName,standardTemp,newState);
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
        strippedNames.sort(String.CASE_INSENSITIVE_ORDER);

        String name;
        if(strippedNames.size() > 1){
            name = String.join(" mixed with ", strippedNames);
        } else {
            name = strippedNames.getFirst();
        }

        try {
            System.out.println(strippedNames.getFirst());
            IngredientName newName = new IngredientName(strippedNames.getFirst());
            newName.setName(name);
            return newName;
        } catch (IngredientName.IllegalNameException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * function used to get the new state of the mixture, which is the state of the ingredient closest to [0,20], in case of equals fluid wins.
     * @return the new state
     */
    private IngredientState newState(){
        Float smallestDiff = ingredients.getFirst().getTemperature().differenceFrom(targetTemp);
        IngredientState newState = new IngredientState(true); //powder is overwritten by fluid
        for(AlchemicIngredient ingredient: ingredients){
            Float diff = ingredient.getTemperature().differenceFrom(targetTemp);
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
            Float pinches = 0F;
            int liquidFractions = 0;
            for(AlchemicIngredient ingredient: ingredients){
                if(ingredient.getQuantity().isPowderUnit()){
                    pinches += ingredient.getQuantity().convertTo(PowderUnit.PINCH);
                }
                else if(ingredient.getQuantity().isGreaterThanOrEqualTo(PowderUnit.SPOON, 1F)){
                    pinches += ingredient.getQuantity().convertToPowderUnit(PowderUnit.PINCH);
                }
                else{
                    liquidFractions += ingredient.getQuantity().convertTo(FluidUnit.DROP);
                }
            }
            pinches += (float) ((liquidFractions -(liquidFractions%dropInSpoon))/dropInSpoon)*pinchInSpoon;
            int pinchesRounded = (int)(pinches +.5); //rounding
            newQuantity = new Quantity(pinchesRounded,PowderUnit.PINCH);
        }
        else{
            Float drops = 0F;
            int solidFractions = 0;
            for(AlchemicIngredient ingredient: ingredients){
                if(!ingredient.getQuantity().isPowderUnit()){
                    drops += ingredient.getQuantity().convertTo(FluidUnit.DROP);
                }
                else if(ingredient.getQuantity().isGreaterThanOrEqualTo(PowderUnit.SPOON, 1F)){
                    drops += ingredient.getQuantity().convertToFluidUnit(FluidUnit.DROP);
                }
                else{
                    solidFractions += ingredient.getQuantity().convertTo(PowderUnit.PINCH);
                }
            }
            drops += (float) ((solidFractions - (solidFractions % pinchInSpoon)) / pinchInSpoon) *dropInSpoon;
            int dropsRounded = (int) (drops +.5); //rounding
            newQuantity = new Quantity(dropsRounded,FluidUnit.DROP);
        }
        return newQuantity;
    }

    /**
     * function used to get the new Temperature of the mixture, which is the weighted sum of the temperatures of the ingredients;
     * @param newQuantity the total quantity of the mixture
     * @return the new Temperature of the mixture
     */
    private Temperature newTemp(Quantity newQuantity){
        Float spoons;
        if(newQuantity.isPowderUnit()) {
             spoons = newQuantity.convertTo(PowderUnit.SPOON);
        }
        else{
            spoons = newQuantity.convertTo(FluidUnit.SPOON);
        }
        float temperature = 0F;
        Temperature newTemperature;
        for(AlchemicIngredient ingredient: ingredients){
            Temperature ingredientTemp = ingredient.getTemperature();
            Quantity ingredientQuantity = ingredient.getQuantity();
            if(ingredientQuantity.isGreaterThanOrEqualTo(FluidUnit.SPOON, 1F)){
                if(ingredientQuantity.isPowderUnit()){
                    temperature += (ingredientTemp.getHotness() -ingredientTemp.getColdness())*(ingredientQuantity.convertTo(PowderUnit.SPOON)/spoons);
                }
                else{
                    temperature += (ingredientTemp.getHotness() -ingredientTemp.getColdness())*(ingredientQuantity.convertTo(FluidUnit.SPOON)/spoons);
                }
            }
            else if(ingredient.getQuantity().isPowderUnit()){
                temperature += (ingredientTemp.getHotness() -ingredientTemp.getColdness())*(ingredientQuantity.convertTo(PowderUnit.PINCH)/(spoons*pinchInSpoon));
            }
            else{
                temperature += (ingredientTemp.getHotness() -ingredientTemp.getColdness())*(ingredientQuantity.convertTo(FluidUnit.DROP)/(spoons*dropInSpoon));
            }
        }

        if(temperature > 0){
            newTemperature = new Temperature(0F,temperature);
        }
        else{
            newTemperature = new Temperature(-temperature,0F);
        }
        return newTemperature;
    }

    /**
     * function used to get the new standard Temperature of the mixture, which is the standardtemperature of the ingredient closest to [0,20], in case of equal closeness the hottest temperature wins
     * @return the new standard temperature of the mixture
     */
    private Temperature newStandardTemp(){
        Temperature newTemperature = ingredients.getFirst().getStandardType().getStandardTemperature();
        Float smallestDiff = ingredients.getFirst().getStandardType().getStandardTemperature().differenceFrom(targetTemp);
        for(AlchemicIngredient ingredient: ingredients){
            Temperature temp = ingredient.getStandardType().getStandardTemperature();
            Float diff = temp.differenceFrom(targetTemp);
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
