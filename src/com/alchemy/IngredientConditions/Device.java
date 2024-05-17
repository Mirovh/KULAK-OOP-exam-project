package com.alchemy.IngredientConditions;

import com.alchemy.AlchemicIngredient;
import com.alchemy.IngredientContainer;
import com.alchemy.Laboratory;
import com.alchemy.quantity.FluidUnit;
import com.alchemy.quantity.PowderUnit;
import com.alchemy.quantity.Quantity;
import com.alchemy.quantity.Unit;

/**
 * An abstract class used to define the standard methods of a device
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public abstract class Device {
    /**********************************************************
     * Variables
     **********************************************************/
    protected AlchemicIngredient ingredient;

    private Laboratory laboratory;

    /**********************************************************
     * Getters and Setters
     **********************************************************/
    /**
     * remove the contents of the device and return the smallest possible container containing the contents of the device
     * @return smallest possible container containing the contents of the device. If quantity is too large, biggest possible
     * container is returned and rest of contents are destroyed.
     */
    public IngredientContainer getContents() {
        if(ingredient == null){
            return null;
        }
        Unit containerUnit;
        if(ingredient.getState().isSolid()){
            containerUnit = ingredient.getQuantity().getSmallestPowderContainer();
        }
        else{
            containerUnit = ingredient.getQuantity().getSmallestFluidContainer();
        }

        IngredientContainer container = new IngredientContainer(ingredient, containerUnit);
        ingredient = null;
        return container;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    /**********************************************************
     * Mutators
     **********************************************************/
    /**
     * method used to add an ingredient to the device
     * @param container the container containing the ingredient that has to be added to the device
     * @throws DeviceFullException if the device is full, deviceFullException is thrown.
     */
    protected void addIngredient(IngredientContainer container) throws DeviceFullException {
        ingredient = container.getContent();
        container.destroy();
    }

    public void react() throws NotInLaboratoryException {
    }

    /**********************************************************
     * Inspectors
     **********************************************************/
    /**
     * @param ingredient Check if ingredient can be added to the device
     * @return true
     */
    public boolean canAddIngredient(AlchemicIngredient ingredient) {return true;}

    /**
     * Check if device is in a Laboratory
     * @return true if laboratory exists
     */
    public boolean isInLaboratory(){return laboratory!=null;}
    /**********************************************************
     * Exceptions
     **********************************************************/
    /**
     * Exception for when a device is full
     */
    public static class DeviceFullException extends Exception {
        public DeviceFullException(String e) {
            super(e);
        }
    }

    public static class NotInLaboratoryException extends Exception {
        public NotInLaboratoryException(String e){super(e);}
    }
}