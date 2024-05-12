package com.alchemy.Temperature;
import com.alchemy.Device;
import com.alchemy.IngredientContainer;

import java.util.Random;

/**
 * A class representing an Oven, used to heat up an ingredient
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class Oven extends Device {
    /**********************************************************
     * Variables
     **********************************************************/
    private Temperature temperature;

    /**********************************************************
     * Constructors
     **********************************************************/
    public Oven(Long coldness, Long hotness){
        this.temperature = new Temperature(0L,0L);setTemperature(coldness,hotness);
    }
    public Oven(){
        this.temperature = new Temperature(0L,20L);
    }

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
        temperature.cool(temperature.getHotness());//temperature is set to 0,0
        temperature.cool(coldness);
        temperature.heat(hotness);
    }

    /**********************************************************
     * Mutators
     **********************************************************/

    /**
     * method for adding an ingredient to the oven
     * @param container the container containing the ingredient that has to be added to the device
     * @throws DeviceFullException if there already is an ingredient in the oven, the device is considered full
     */
    @Override
    public void addIngredient(IngredientContainer container) throws DeviceFullException{
        if (ingredient != null){
            throw new DeviceFullException("Oven Full");
        }
        else{
            super.addIngredient(container);
        }

    }

    /**
     * method to start the reaction
     * @effect the temperature of the content in the oven will be heated to the temperature of the oven with a deviation of up to 5 degrees
     *if the ingredient is hotter than the temperature of the oven, nothing happens.
     */
    @Override
    public void react() throws NotInLaboratoryException{
        if(!isInLaboratory()){
            throw new NotInLaboratoryException("Oven not in Laboratory");
        }
        if(ingredient == null){
            return;
        }
        else{
            Temperature ingredientTemperature= ingredient.getTemperature();
            Long ingredientHotness = ingredientTemperature.getHotness();
            Long ingredientColdness = ingredientTemperature.getColdness();
            Long hotness = temperature.getHotness();
            Long coldness = temperature.getColdness();
            if(ingredientHotness > hotness | ingredientColdness< coldness |(ingredientHotness.equals(hotness) && ingredientColdness.equals(coldness))){
                return;
            }
            else{
                Random rand = new Random();
                int randomDeviation = rand.nextInt(11)-5;
                ingredientTemperature.heat(ingredientColdness-coldness+hotness-ingredientHotness + randomDeviation);
            }
        }
    }
}
