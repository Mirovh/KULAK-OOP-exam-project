package com.alchemy.IngredientConditions;

import be.kuleuven.cs.som.annotate.Basic;
import com.alchemy.IngredientConditions.Device;
import com.alchemy.IngredientContainer;

/**********************************************************
 * A class representing a coolingbox, used to cool an ingredient
 * defensively programmed
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
**********************************************************/
public class CoolingBox extends Device {
    /**********************************************************
     * Variables
     **********************************************************/
    /**
     * Variable referencing the Temperature of the coolingBox
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
    public CoolingBox(Float coldness, Float hotness){
        this.temperature = new Temperature(0F,0F);setTemperature(coldness,hotness);
    }

    /**
     * creates a coolingbox with temperature 0,20
     */
    public CoolingBox(){
        this.temperature = new Temperature(0F,20F);
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
     * method to set the temperature of the coolingBox
     * @pre either hotness or coldness is 0
     * @param coldness the coldness the oven will be set to
     * @param hotness the hotness the coolingBox will be set to
     */
    @Basic
    public void setTemperature(Float coldness,Float hotness){
        temperature.heat(temperature.getColdness());
        temperature.cool(temperature.getHotness());
        temperature.cool(coldness);
        temperature.heat(hotness);
    }

    /**
     * method to set the temperature of the coolingBox
     * @param temperature the temperature the coolingBox will be set to
     */
    @Basic
    public void setTemperature(Temperature temperature){
        Float coldness = temperature.getColdness();
        Float hotness = temperature.getHotness();
        this.temperature.heat(temperature.getColdness());
        this.temperature.cool(temperature.getHotness());
        this.temperature.cool(coldness);
        this.temperature.heat(hotness);
    }

    /**********************************************************
     * Mutators
     **********************************************************/
    /**
     * method used to add an ingredient to the device
     * @param container the container containing the ingredient that has to be added to the device
     * @throws DeviceFullException if the device is full, deviceFullException is thrown.
     */
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
     *  |if(!ingredientTemperature.isColderThan(temperature){
     *  |ingredientTemperature = temperature
     *  |}
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
            Float ingredientHotness = ingredientTemperature.getHotness();
            Float ingredientColdness = ingredientTemperature.getColdness();
            Float hotness = temperature.getHotness();
            Float coldness = temperature.getColdness();
            if(!ingredientTemperature.isColderThan(temperature)){
                ingredientTemperature.cool(ingredientHotness-hotness+coldness-ingredientColdness);
            }
        }
    }
    /**
     * method to cool coolingboxes own temperature by 10 degrees
     * @effect the temperature of coolinbox will be lowered by 10 degrees
     */
    public void lowerOwnTemp(){
        temperature.cool(10F);
    }
}
