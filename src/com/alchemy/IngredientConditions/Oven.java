package com.alchemy.IngredientConditions;
import be.kuleuven.cs.som.annotate.Raw;
import com.alchemy.IngredientContainer;

import java.util.Random;

/**********************************************************
 * A class representing an Oven, used to heat up an ingredient
 * defensively programmed
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
**********************************************************/
public class Oven extends Device {

    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * Variable referencing the Temperature of the oven
     */
    private Temperature temperature;


    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Creates a new oven with a temperature with coldness and hotness as given
     * @param coldness the coldness of the temperature of the oven
     * @param hotness the hotness of the temperature of the oven
     */
    @Raw
    public Oven(Float coldness, Float hotness){
        this.temperature = new Temperature(0F,0F);
        setTemperature(coldness,hotness);
    }

    /**
     * creates an oven with temperature [0,20]
     */
    @Raw
    public Oven(){
        this.temperature = new Temperature(0F,20F);
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
    public void setTemperature(Float coldness,Float hotness) {
        temperature.heat(temperature.getColdness());
        temperature.cool(temperature.getHotness());//temperature is set to 0,0
        temperature.cool(coldness);
        temperature.heat(hotness);
    }

    /**
     * method to set the temperature of the oven
     * @param temperature the temperature the oven will be set to
     */
    public void setTemperature(Temperature temperature) {
        Float coldness = temperature.getColdness();
        Float hotness = temperature.getHotness();
        this.temperature.heat(temperature.getColdness());
        this.temperature.cool(temperature.getHotness());
        temperature.cool(coldness);
        temperature.heat(hotness);
    }


    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * method for adding an ingredient to the oven
     * @param container the container containing the ingredient that has to be added to the device
     * @throws DeviceFullException if there already is an ingredient in the oven, the device is considered full
     */
    @Override
    public void addIngredient(IngredientContainer container) throws DeviceFullException {
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
    public void react() throws NotInLaboratoryException {
        if(!isInLaboratory()){
            throw new NotInLaboratoryException("Oven not in Laboratory");
        }
        if(ingredient == null){
            return;
        }
        else{
            Temperature ingredientTemperature= ingredient.getTemperature();
            Float ingredientHotness = ingredientTemperature.getHotness();
            Float ingredientColdness = ingredientTemperature.getColdness();
            Float hotness = temperature.getHotness();
            Float coldness = temperature.getColdness();
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

    /**
     * method to higher ovens own temperature by 10 degrees
     * @effect the temperature of oven will be heated by 10 degrees
     */
    public void heatOwnTemperature() {
        temperature.heat(10F);
    }
}
