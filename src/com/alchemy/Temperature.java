package com.alchemy;
import java.util.ArrayList;
/**
 * Temperature - total programming
 *
 * class to keep track of temperatures, with methods to heat and cool.
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class Temperature {
    /**********************************************************
     * Variables
     **********************************************************/
    private Long hotness;
    private Long coldness;
    static Long temperatureLimit = 10000L;
    /**********************************************************
     * Constructors
     **********************************************************/
    /**
     * @post initializes a new Temperature object. If both parameters differ from 0, only hotness is taken, coldness is set to 0
     * if the hotness or coldness is higher than the temperatureLimit, the value of that variable is set to the limit
     * @param coldness the coldness of the temperature
     * @param hotness the hotness of the temperature
     */
    public Temperature(Long coldness, Long hotness){
        if(coldness != 0 && hotness != 0){
            this.hotness = hotness;
            this.coldness = 0L;
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
    /**********************************************************
     * Getters and Setters
     **********************************************************/

    public Long getHotness(){
        return hotness;
    }
    public Long getColdness(){
        return coldness;
    }
    public ArrayList<Long> getTemperature(){
    ArrayList<Long> list = new ArrayList<>();
    list.add(coldness);
    list.add(hotness);
    return list;
    }
    /**********************************************************
     * Manipulators
     **********************************************************/
    /**
     * @post coldness goes down by "change" amount.If coldness is zero, hotness goes up for the rest of the amount.
     * if the change is less than 0, temperature will be cooled instead
     * @param change the change in temperature
     */
    public void heat(Long change){
        if(change <0){
            cool(change);
            return;
        }
        else{
            ArrayList<Long> newTemps = changeTemp(change,hotness,coldness);
            hotness = newTemps.getFirst();
            coldness = newTemps.getLast();
        }
    }
    /**
     * @post hotness goes down by "change" amount.If hotness is 0, coldness goes up for the rest of the change
     * if the change is less than 0, temperature will be heated instead
     * @param change the change in temperature
     */
    public void cool(Long change){
        if(change<0){
            heat(change);

        }
        else{
            ArrayList<Long> newTemps = changeTemp(change,coldness,hotness);
            coldness = newTemps.getFirst();
            hotness = newTemps.getLast();
        }

    }

    /**
     * method that changes temperature
     *
     * @param change the total amount the temperature changes.
     * @param higher the amount of the variable that decreases
     * @param lower the amount of the variable that increases.
     * @post lower will be lowered by change amount or until its zero, in which case higher will be raised by the rest of change
     * if higher ends up higher than the temperatureLimit, it will be set to the limit itself
     * @return an arraylist of the two variables that have been changed.
     */
    private ArrayList<Long> changeTemp(Long change, Long higher,Long lower){
        lower -= change;
        if(lower <0){
            higher += lower*-1;
            lower = 0L;
        }
        if(higher>temperatureLimit){
            higher = temperatureLimit;
        }
        ArrayList<Long> list = new ArrayList<>();
        list.add(higher);
        list.add(lower);
        return list;
    }
}
