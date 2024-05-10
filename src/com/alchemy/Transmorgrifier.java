package com.alchemy;
/**
 * A class representing an Oven, used to heat up an ingredient
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class Transmorgrifier extends Device{
    /**********************************************************
     * Constructors
     **********************************************************/
    public Transmorgrifier(){}
    @Override
    public void react(){
        ingredient.switchState();
    }
}
