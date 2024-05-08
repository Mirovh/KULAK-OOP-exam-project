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
    Temperature(Long hotness,Long coldness){
        this.hotness= hotness;
        this.coldness = coldness;
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
     * if the change is less than 0, temperature will be heated instead
     * @param change the change in temperature
     */
    public void heat(Long change){
        if(change <0){
            cool(change);
            return;
        }
        else{
            ArrayList<Long> newTemps = changeTemp(change,hotness,coldness);
            coldness = newTemps.getFirst();
            hotness = newTemps.getLast();
        }
    }
    /**
     * @post hotness goes down by "change" amount.If hotness is 0, coldness goes up for the rest of the change
     * if the change is less than 0, temperature will be cooled instead
     * @param change the change in temperature
     */
    public void cool(Long change){
        if(change<0){
            heat(change);
            return;
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
     * @return an arraylist of the two variables that have been changed.
     */
    private ArrayList<Long> changeTemp(Long change, Long higher,Long lower){
        if(lower >0){
            if(lower>change){
                change -= lower;
                lower = 0L;
                higher = change;
            }
            else{
                lower -= change;
            }
        }
        else{
            higher += change;
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
