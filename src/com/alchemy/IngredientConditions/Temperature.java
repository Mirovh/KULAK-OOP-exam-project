package com.alchemy.IngredientConditions;
import be.kuleuven.cs.som.annotate.Basic;

import java.util.ArrayList;
import java.lang.Math;
/**********************************************************
 * Temperature
 * class to keep track of temperatures, with methods to heat and cool.
 * @invar hotness and coldness can't both differ from 0 at the same time
 * totally programmed
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
**********************************************************/
public class Temperature {

    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * Variable referencing the hotness of the temperature
     */
    private Float hotness;

    /**
     * Variable referencing the coldness of the temperature
     */
    private Float coldness;

    /**
     * Variable referencing the limit of the hotness and coldness
     */
    static Float temperatureLimit = 10000F;


    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * @post initializes a new Temperature object. If both parameters differ from 0, only hotness is used, coldness is set to 0, if the hotness or coldness is higher than the temperatureLimit, the value of that variable is set to the limit
     * @param coldness the coldness of the temperature
     * @param hotness the hotness of the temperature
     */
    public Temperature(Float coldness, Float hotness){
        if(coldness != 0 && hotness != 0){
            this.hotness = hotness;
            this.coldness = 0F;
        }
        else {
            this.hotness = hotness;
            this.coldness = coldness;
        }
        if(hotness > temperatureLimit){
            hotness = temperatureLimit;
        }
        else if(coldness > temperatureLimit){
            coldness = temperatureLimit;
        }

    }

    /**
     * @post initializes a new Temperature object. If both parameters differ from 0, only hotness is used, coldness is set to 0, if the hotness or coldness is higher than the temperatureLimit, the value of that variable is set to the limit
     * @param coldness the coldness of the temperature
     * @param hotness the hotness of the temperature
     */
    public Temperature(int coldness, int hotness){this((float)coldness,(float)hotness);}


    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**
     * @return the hotness of the temperature
     */
    @Basic
    public Float getHotness(){
        return hotness;
    }

    /**
     * @return the coldness of the temperature
     */
    @Basic
    public Float getColdness(){
        return coldness;
    }

    /**
     * Returns the temperature of the object as a list of two floats, the first being the coldness, the second being the hotness
     * @return the temperature of the object as a list of two floats
     */
    @Basic
    public ArrayList<Float> getTemperature() {
        ArrayList<Float> list = new ArrayList<>();
        list.add(coldness);
        list.add(hotness);
        return list;
    }


    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * Heats the temperature by a certain amount
     * @post coldness goes down by "change" amount. If coldness is zero, hotness goes up for the rest of the amount, if the change is less than 0, temperature will be cooled instead
     * @param change the change in temperature
     */
    void heat(Float change){
        if(change <0){
            cool(change);
            return;
        }
        else{
            ArrayList<Float> newTemps = changeTemp(change,hotness,coldness);
            hotness = newTemps.getFirst();
            coldness = newTemps.getLast();
        }
    }

    /**
     * Cools the temperature by a certain amount
     * @post hotness goes down by "change" amount. If hotness is 0, coldness goes up for the rest of the change, if the change is less than 0, temperature will be heated instead
     * @param change the change in temperature
     */
    void cool(Float change){
        if(change<0){
            heat(change);

        }
        else{
            ArrayList<Float> newTemps = changeTemp(change,coldness,hotness);
            coldness = newTemps.getFirst();
            hotness = newTemps.getLast();
        }

    }

    /**
     * method that changes temperature
     * @param change the total amount the temperature changes.
     * @param higher the amount of the variable that decreases
     * @param lower the amount of the variable that increases.
     * @post lower will be lowered by change amount or until its zero, in which case higher will be raised by the rest of change, if higher ends up higher than the temperatureLimit, it will be set to the limit itself
     * @return an arraylist of the two variables that have been changed.
     */
    private ArrayList<Float> changeTemp(Float change, Float higher,Float lower){
        lower -= change;
        if(lower <0){
            higher += lower*-1;
            lower = 0F;
        }
        if(higher>temperatureLimit){
            higher = temperatureLimit;
        }
        ArrayList<Float> list = new ArrayList<>();
        list.add(higher);
        list.add(lower);
        return list;
    }

    /**
     * Method that checks if this temperature is hotter than another temperature
     * @param otherTemp the temperature that this temperature gets compared to
     * @return true if this temperature is hotter than otherTemp
     */
    public Boolean isHotterThan(Temperature otherTemp) {
        return(coldness<otherTemp.getColdness()|hotness>otherTemp.getHotness());
    }

    /**
     * Method that checks if this temperature is colder than another temperature
     * @param otherTemp the temperature that this temperature gets compared to
     * @return true if this temperature is colder than otherTemp
     */
    public Boolean isColderThan(Temperature otherTemp) {
        return(coldness>otherTemp.getColdness()|hotness<otherTemp.getHotness());
    }

    /**
     * Method that calculates the difference in temperature from another temperature
     * @param otherTemp the temperature that this temperature gets compared to
     * @return the value of the difference in temperature
     */
    public Float differenceFrom(Temperature otherTemp) {
        return(Math.abs(coldness-otherTemp.getColdness())+Math.abs(hotness-otherTemp.getHotness()));
    }
}
