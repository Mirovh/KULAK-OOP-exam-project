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
    public void react() throws NotInLaboratoryException {
        if(!isInLaboratory()){throw new NotInLaboratoryException("Transmorgrifier not in a Laboratory");}
        ingredient.switchState();
    }
    @Override
    public void addIngredient(IngredientContainer container) throws DeviceFullException{
        if (ingredient != null){
            throw new DeviceFullException("Transmorgrifier Full");
        }
        else{
            super.addIngredient(container);
        }

    }
}
