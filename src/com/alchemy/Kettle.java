package com.alchemy;
import com.alchemy.Temperature.Temperature;

import java.util.ArrayList;

/**
 * A class representing an Oven, used to heat up an ingredient
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class Kettle extends Device{
    /**********************************************************
     * Variables
     **********************************************************/
    private ArrayList<AlchemicIngredient> ingredient;

    /**********************************************************
     * Constructors
     **********************************************************/

    /**********************************************************
     * Getters and Setters
     **********************************************************/
    @Override
    public void addIngredient(IngredientContainer container){
        ingredient.add(container.getContent());
        container.destroy();
    }
    //TODO: GetContent


    /**********************************************************
     * Mutators
     **********************************************************/

        /**
         * method to start the reaction
         * @effect the temperature of the content in the oven will be heated to the temperature of the oven with a deviation of up to 5 degrees
         *if the ingredient is hotter than the temperature of the oven, nothing happens.
         */
        @Override
        public void react() throws NotInLaboratoryException{
            if(!isInLaboratory()){
                throw new NotInLaboratoryException("Kettle not in Laboratory");
            }
            else{
                IngredientName newIngredientName;
                //AlchemicIngredient.IngredientState newState;
                int newQuantity;//Todo: Implement quantity
                Temperature newTemperature;

                /**ingredient.clear();
                AlchemicIngredient newIngredient = new AlchemicIngredient(newName,newQuantity,newState);
                newIngredient.getTemperature().cool(20);**/
                //wait with implementation till quantity and ingredienttype are implemented.
            }

        }


}
