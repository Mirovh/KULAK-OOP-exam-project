package com.alchemy.IngredientConditions;

import com.alchemy.Device;
import com.alchemy.IngredientContainer;

/**
 * A class representing a coolingbox, used to cool an ingredient
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class CoolingBox extends Device {
    /**********************************************************
     * Variables
     **********************************************************/
    /**
     *
     */
    private Temperature temperature;
    /**********************************************************
     * Constructors
     **********************************************************/
    /**
     * creates a CoolingBox with given coldness and hotness
     * @param coldness the coldness of the temperature of the CoolingBox
     * @param hotness the hotness of the temperature of the CoolingBox
     */
    public CoolingBox(Long coldness, Long hotness){
        this.temperature = new Temperature(0L,0L);setTemperature(coldness,hotness);
    }

    /**
     * creates a coolingbox with temperature 0,20
     */
    public CoolingBox(){
        this.temperature = new Temperature(0L,20L);
    }
    /**
     * method for adding an ingredient to the coolingbox
     * @param container the container containing the ingredient that has to be added to the device
     * @throws DeviceFullException if there already is an ingredient in the coolingbox, the device is considered full
     */

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**
     * method to set the temperature of the oven
     * @pre either hotness or coldness is 0
     * @param coldness the coldness the oven will be set to
     * @param hotness the hotness the oven will be set to
     */
    public void setTemperature(Long coldness,Long hotness){
        temperature.heat(temperature.getColdness());
        temperature.cool(temperature.getHotness());
        temperature.cool(coldness);
        temperature.heat(hotness);
    }

    /**********************************************************
     * Mutators
     **********************************************************/

    @Override
    public void addIngredient(IngredientContainer container) throws DeviceFullException{
        if (ingredient != null){
            throw new DeviceFullException("CoolingBox Full");
        }
        else{
            super.addIngredient(container);
        }

    }

    /**
     * method to start the reaction
     * @effect the temperature of the content in the coolingbox will be cooled to the temperature of the coolingbox
     *if the ingredient is colder than the temperature of the coolingbox, nothing happens.
     */

    @Override
    public void react() throws NotInLaboratoryException {
        if(!isInLaboratory()){
            throw new NotInLaboratoryException("CoolingBox not in Laboratory");
        }
        if(ingredient == null){
            return;
        }
        else{
            Temperature ingredientTemperature = ingredient.getTemperature();
            Long ingredientHotness = ingredientTemperature.getHotness();
            Long ingredientColdness = ingredientTemperature.getColdness();
            Long hotness = temperature.getHotness();
            Long coldness = temperature.getColdness();
            if(ingredientHotness < hotness | ingredientColdness > coldness){
                return;
            }
            else{
                ingredientTemperature.cool(ingredientHotness-hotness+coldness-ingredientColdness);

            }
        }
    }
}
