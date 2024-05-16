package com.alchemy;
import com.alchemy.quantity.Unit;

import java.util.ArrayList;

public class Laboratory {
    private ArrayList<Device> devices;
    private ArrayList<AlchemicIngredient> ingredients;
    private int Storeroom;

    /**
     * Constructs a new Laboratory with the specified number of storerooms.
     * Initializes the lists of devices and alchemic ingredients.
     *
     * @param Storeroom the number of storerooms in the laboratory
     * @throws IllegalArgumentException if the number of storerooms is less than 1
     */
    public Laboratory(int Storeroom){
        if(Storeroom >= 1) {
            this.Storeroom = Storeroom;
            devices = new ArrayList<>();
            ingredients = new ArrayList<AlchemicIngredient>();
        } else{
            throw new IllegalArgumentException("amount of storerooms must be bigger than 0");
        }
    }

    /**
     * getter for the amount of total space in the lab
     * @return laboratorySpace The amount of total space in the laboratory
     */
    public int getSpace(){          //TODO: add method to calculate total sapce in lab
        return 0;                   //temp
    }

    /**
     * Calculates and returns the free space available in the storeroom.
     *
     * @return the free space in the storeroom
     */
    public int getFreeSpace() {
        int usedSpace = 0;
        for (AlchemicIngredient Ingredients : ingredients) {
            usedSpace += Ingredients.getAmount();
        }
        return Storeroom.getSpace - usedSpace;
    }

    /**
     * Calculates and returns the free space available in the storeroom.
     *
     * @return the filled space in the storeroom
     */
    public int getFilledSpace(){
        return Storeroom.getSpace - Laboratory.getFreeSpace();
    }

    /**
     * Constructs a string representation of the contents of the laboratory.
     *
     * @return A string describing the amount, unit, and name of each ingredient in the laboratory.
     *         The string is in the format: "The lab contains: {amount} {unit} of {ingredient}, ..."
     *         If the laboratory is empty, the returned string will simply be: "The lab contains: "
     */
    public String getContents() {
        StringBuilder contents = new StringBuilder("The lab contains: ");
        for (AlchemicIngredient ingredient : ingredients) {
            contents.append(ingredient.getAmount())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" of ")
                    .append(ingredient.getBasicName())
                    .append(", ");
        }
        if (contents.length() > 0) {
            contents.setLength(contents.length() - 2);
        }
        return contents.toString();
    }
    /**
     * Constructs a string representation of a specific ingredient in the laboratory.
     *
     * @param ingredient The specific AlchemicIngredient to find in the laboratory.
     * @return A string describing the amount, unit, and name of the specified ingredient in the laboratory.
     *         The string is in the format: "{amount} {unit} of {ingredient}"
     *         If the ingredient is not found in the laboratory, the returned string will be empty.
     */
    public String getContents(AlchemicIngredient ingredient) {
        String ingredientString = "";
        for (AlchemicIngredient Ingredients : ingredients) {
            if (Ingredients.equals(ingredient)) {
                ingredientString = Ingredients.getAmount() + " " + Ingredients.getUnit() + " of " + Ingredients.getBasicName();
                break;
            }
        }
        return ingredientString;
    }

    /**
     * Checks if a given IngredientContainer can be added to the laboratory.
     *
     * @param container The IngredientContainer to be added to the laboratory.
     * @return true if the laboratory has enough free space to accommodate the amount of the ingredient in the container,
     *         false otherwise.
     */
    public boolean canAddContainer(IngredientContainer container){
        if(Laboratory.getFreeSpace() <= container.getContent().getAmount);{
            return true;
        } else{
            return false;
        }
    }

    /**
     * Adds a given IngredientContainer to the laboratory.
     *
     * @param container The IngredientContainer to be added to the laboratory.
     * @throws IllegalArgumentException if the laboratory does not have enough free space to accommodate the ingredient in the container.
     */
    public void addContainer(IngredientContainer container){
        if (canAddContainer(container)){
            ingredients.add(container.getContent());
        } else{
            throw new IllegalArgumentException("can't add container");
        }
    }

    /**
     * Adds a specified amount of an IngredientContainer to the laboratory.
     *
     * @param container The IngredientContainer to be added to the laboratory.
     * @param amount The amount of the ingredient to be added from the container.
     * @throws IllegalArgumentException if the laboratory does not have enough free space to accommodate the specified amount of the ingredient in the container,
     *                                  or if the container does not have enough of the ingredient.
     */
    public void addContainer(IngredientContainer container, int amount){                    //TODO: addReduceQuantity in containers
        if (container.getContent() != null && amount <= container.getContent().getAmount()) {
            IngredientContainer partialContainer = new IngredientContainer(container.getContent(), container.getContainerUnit());
            partialContainer.getContent().reduceQuantity(amount);
            if (canAddContainer(partialContainer)){
                containers.add(partialContainer);
                container.getContent().reduceQuantity(amount);
            } else{
                throw new IllegalArgumentException("Not enough space to add container");
            }
        }
    }

    /**
     * Removes a specified amount of an AlchemicIngredient from the laboratory and returns it in a new IngredientContainer.
     *
     * @param ingredient The AlchemicIngredient to be removed from the laboratory.
     * @param containerUnit The unit of the new IngredientContainer.
     * @param amount The amount of the ingredient to be removed.
     * @return A new IngredientContainer containing the removed ingredient.
     * @throws IngredientName.IllegalNameException If the name of the ingredient is illegal.
     * @throws IllegalArgumentException If there is not enough of the ingredient to remove or if the ingredient is not found in the laboratory.
     */
    public IngredientContainer removeIngredient(AlchemicIngredient ingredient, Unit containerUnit, int amount) throws IngredientName.IllegalNameException {
        AlchemicIngredient labIngredient = null;
        for (AlchemicIngredient ingredientInLab : ingredients) {
            if (ingredientInLab.equals(ingredient)) {
                if (ingredientInLab.getAmount() < amount && amount >= 0) {
                    throw new IllegalArgumentException("Not enough ingredient to remove");
                }
                ingredientInLab.reduceQuantity(amount);
                if (ingredientInLab.getAmount() == 0) {
                    ingredients.remove(ingredientInLab);
                }
                labIngredient = new AlchemicIngredient(ingredient.getFullName(), ingredient.getTemperature(), ingredient.getState(), amount);
                break;
            }
        }
        if (labIngredient == null) {
            throw new IllegalArgumentException("Ingredient not found");
        }
        IngredientContainer labContainer = new IngredientContainer(labIngredient, containerUnit);               // bij verwijderen van meerdere containers geen manier om deze bij te houden
        return labContainer;
    }

    /**
     * Increases the number of storerooms in the laboratory by a specified amount.
     *
     * @param storeroom The number of storerooms to be added to the laboratory.
     * @throws IllegalArgumentException if the specified number of storerooms is negative.
     */
    public void addStoreroom(int storeroom){
        if (storeroom >= 0) {
            Storeroom += storeroom;
        } else{
            throw new IllegalArgumentException("Cannot add negative Storerooms");
        }
    }

    /**
     * Removes the specified number of storerooms.
     * This method attempts to remove the specified number of storerooms. If the removal is possible
     * (i.e., the number of storerooms after the removal would not be negative and the laboratory would still have enough space),
     * it decreases the current number of storerooms. Otherwise, it throws an IllegalArgumentException.
     *
     * @param storeroom the number of storerooms to be removed
     * @throws IllegalArgumentException if the specified number of storerooms cannot be removed
     */
    public void removeStoreroom(int storeroom){
        if (canRemoveStoreRoom(storeroom)){
            Storeroom -= storeroom;
        }else{
            throw new IllegalArgumentException("Cannot remove storeroom");
        }
    }

    /**
     * Checks if the specified number of storerooms can be removed.
     * This method checks if it's possible to remove the specified number of storerooms without making the number of storerooms negative or exceeding the filled space in the laboratory.
     *
     * @param storeroom the number of storerooms to be removed
     * @return true if the specified number of storerooms can be removed, false otherwise
     */
    public Boolean canRemoveStoreRoom(int storeroom){
        return storeroom > Storeroom && storeroom > 0 && (Storeroom - storeroom).getSpace() >= Laboratory.getFilledSpace();
    }

    /**
     * Checks if the ingredients list is empty.
     * This method returns true if there are no ingredients in the list, and false otherwise.
     *
     * @return true if the ingredients list is empty, false otherwise
     */
    public Boolean isEmpty(){
        return ingredients.isEmpty();
    }

    /**
     * Retrieves the current number of storerooms.
     * This method returns the current number of storerooms in the laboratory.
     *
     * @return the current number of storerooms
     */
    public int getStoreroom(){
        return Storeroom;
    }

    /**
     * Retrieves the list of devices.
     * This method returns the list of devices currently stored. The returned list is an ArrayList of Device objects.
     *
     * @return an ArrayList of Device objects representing the devices currently stored
     */
    public ArrayList<Device> getDevices() {
        return devices;
    }

    /**
     * Adds a device to the laboratory.
     * This method attempts to add a device to the laboratory. If the addition is possible
     * (i.e., the laboratory has enough space for the device), it adds the device to the laboratory's list of devices
     * and sets the laboratory for the device. Otherwise, it throws a LaboratoryFullException.
     *
     * @param device the device to be added
     * @throws LaboratoryFullException if the laboratory does not have enough space for the device
     */
    public void addDevice(Device device) throws LaboratoryFullException {
        if(canAddDevice(device)) {
            devices.add(device);
            device.setLaboratory(this);
        }
        else{
            throw new LaboratoryFullException("Laboratory already has " + device.getClass().getSimpleName());
        }
    }

    /**
     * Checks if a device can be added to the laboratory.
     * This method checks if it's possible to add a specified device to the laboratory. It returns true if the laboratory does not already contain a device of the same class as the specified device, or if the laboratory's list of devices is null. Otherwise, it returns false.
     *
     * @param checkedDevice the device to be checked
     * @return true if the specified device can be added, false otherwise
     */
    public Boolean canAddDevice(Device checkedDevice){
        boolean valid = true;
        if(devices == null){return true;}
        for(Device device: devices){
            if (device.getClass() == checkedDevice.getClass()) {
                valid = false;
                break;
            }
        }
        return valid;
    }
    /**
     * Exception thrown when the laboratory is full.
     */
    public static class LaboratoryFullException extends Exception {
        public LaboratoryFullException(String e){super(e);}
    }
}
