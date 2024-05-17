package com.alchemy.IngredientConditions;

import com.alchemy.Device;
import com.alchemy.IngredientContainer;
import com.alchemy.quantity.FluidUnit;
import com.alchemy.quantity.PowderUnit;

/**********************************************************
 * A class representing a transmorgrifier, used to change the state of an ingredient
 * defensively programmed
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
**********************************************************/
public class Transmorgrifier extends Device {
    /**********************************************************
     * Constructors
     **********************************************************/
    /**
     * creates an empty Transmorgrifier
     */
    public Transmorgrifier(){}
    /**********************************************************
     * Methods
     **********************************************************/
    /**
     * method to start the reaction
     * @throws NotInLaboratoryException if the transmorgrifier isn't in a Laboratory
     */
    @Override
    public void react() throws NotInLaboratoryException {
        if(!isInLaboratory()){throw new NotInLaboratoryException("Transmorgrifier not in a Laboratory");}
        ingredient.getState().switchState();
        if(ingredient.getState().isSolid()) {
            ingredient.getQuantity().convertToPowderUnit(PowderUnit.PINCH);
        }
        else{
            ingredient.getQuantity().convertToFluidUnit(FluidUnit.DROP);
        }
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
