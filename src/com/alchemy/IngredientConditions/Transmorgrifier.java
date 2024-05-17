package com.alchemy.IngredientConditions;

import com.alchemy.IngredientContainer;

/**
 * A class representing a transmorgrifier, used to change the state of an ingredient
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class Transmorgrifier extends Device {
    /**********************************************************
     * Constructors
     **********************************************************/
    public Transmorgrifier(){}
    /**********************************************************
     * Methods
     **********************************************************/
    /**
     * method to start the reaction
     * @throws NotInLaboratoryException if the transmorgrifier isn't in a Laboratory
     * //TODO: quantity conversion
     */
    @Override
    public void react() throws NotInLaboratoryException {
        if(!isInLaboratory()){throw new NotInLaboratoryException("Transmorgrifier not in a Laboratory");}
        ingredient.getState().switchState();
    }

    /**
     * method to add the contents of a container to the transmorgrifier, destroying the container in the process
     * @param container the container containing the ingredient that has to be added to the device
     * @throws DeviceFullException if there already is an ingredient in the transmorgrifier
     */
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
