package com.alchemy;
import java.util.ArrayList;
public abstract class Device {
    /**********************************************************
     * Variables
     **********************************************************/
    protected AlchemicIngredient ingredient;

    /**********************************************************
     * Getters and Setters
     **********************************************************/
    /**
     * remove the contents of the device and return the smallest possible container containing the contents of the device
     * @return smallest possible container containing the contents of the device. If quantity is too large, biggest possible
     * container is returned and rest of contents are destroyed.
     */
    public IngredientContainer getContents() {
        int size = ingredient.getQuantity(); //TODO: calculate minimal needed quantity
        IngredientContainer container = new IngredientContainer(ingredient, size);
        ingredient = null;
        return container;
    }

    /**********************************************************
     * Mutators
     **********************************************************/
    /**
     * @param container the container containing the ingredient that has to be added to the device
     * @throws Exception if the device is full, deviceFullException is thrown.
     */
    public void addIngredient(IngredientContainer container) throws Exception {
        ingredient = container.getContents();
        container.Destroy();

    }

    public void react() {
    }

    /**********************************************************
     * Inspectors
     **********************************************************/
    /**
     * @param ingredient Check if ingredient can be added to the device
     * @return true
     */
    public boolean canAddIngredient(AlchemicIngredient ingredient) {
        return true;
    }

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
}