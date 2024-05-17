package com.alchemy;
import com.alchemy.IngredientConditions.CoolingBox;
import com.alchemy.IngredientConditions.Kettle;
import com.alchemy.IngredientConditions.Oven;
import com.alchemy.IngredientConditions.Temperature;
import com.alchemy.quantity.Quantity;
import com.alchemy.recipes.Recipe;
import com.alchemy.recipes.Recipe.*;
import com.alchemy.quantity.Unit;
import java.util.ArrayList;

import static com.alchemy.quantity.FluidUnit.*;
import static com.alchemy.quantity.PowderUnit.*;


public class Laboratory {
    private ArrayList<Device> devices;
    private ArrayList<IngredientContainer> containers;
    private int Storeroom;

    /**
     * Constructs a new Laboratory with the specified number of storerooms.
     * Initializes the lists of devices and alchemic containers.
     *
     * @param Storeroom the number of storerooms in the laboratory
     * @throws IllegalArgumentException if the number of storerooms is less than 1
     */
    public Laboratory(int Storeroom){
        if(Storeroom >= 1) {
            this.Storeroom = Storeroom;
            devices = new ArrayList<>();
            containers = new ArrayList<IngredientContainer>();
        } else{
            throw new IllegalArgumentException("amount of storerooms must be bigger than 0");
        }
    }

    public double getFilledSpace() {
        double FilledSpace = 0;

        for (IngredientContainer container : containers) {
            if (container.getContent().getQuantity().isFluidUnit()){
                double amountOfFluid = container.getContainerUnit().convertTo(DROP, 50400L);
                FilledSpace += amountOfFluid;
            } else if (container.getContent().getQuantity().isPowderUnit()) {
                double amountOfPowder = container.getContainerUnit().convertTo(PINCH, 37800L);
                double powderInFluidUnit = amountOfPowder * 1.33;                           //hardcoded
                FilledSpace += powderInFluidUnit;
            }
        }
        return FilledSpace;
    }

    /**
     * Calculates and returns the free space available in the storeroom.
     *
     * @return the free space in the storeroom
     */
    public Double getFreeSpace() {
        Double FilledSpace = this.getFilledSpace();
        Double Space = this.getSpace();
        return Space - FilledSpace;
    }

    public Double getSpace(){
        int AmountOfStoreroom = this.getStoreroom();
        return (Double) (double) (AmountOfStoreroom * 50400);                                //hardcoded
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
        for (IngredientContainer container : containers) {
            if (container.getContent().getQuantity().isFluidUnit()) {
                contents.append(container.getContainerUnit().convertTo(DROP, 50400L));
                        .append(" drops of ")
                        .append(container.getContent().getBasicName())
                        .append(", ");
            }else{
                contents.append(container.getContainerUnit().convertTo(PINCH, 50400L));
                        .append(" drops of ")
                        .append(container.getContent().getBasicName())
                        .append(", ");
            }
        }
        if (!contents.isEmpty()) {
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
        for (IngredientContainer container : containers) {
            if (container.getContent().equals(ingredient)) {
                if (container.getContent().getQuantity().isFluidUnit()) {
                    ingredientString = container.getContent().getQuantity().getUnit().convertTo(DROP, 50400L) + " drops of " + container.getContent().getBasicName();
                    break;
                }else {
                    ingredientString = container.getContent().getQuantity().getUnit().convertTo(PINCH, 50400L) + " drops of " + container.getContent().getBasicName();
                    break;
                }
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
         return container.getContent().getQuantity().isSmallerThanOrEqualTo(new Quantity(Laboratory.getFreeSpace(), DROP));
    }

    /**
     * Adds a given IngredientContainer to the laboratory.
     *
     * @param container The IngredientContainer to be added to the laboratory.
     * @throws IllegalArgumentException if the laboratory does not have enough free space to accommodate the ingredient in the container.
     */
    public void addContainer(IngredientContainer container){
        if (canAddContainer(container)){
            containers.add(container);
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
    public void addContainer(IngredientContainer container, Long amount) throws IngredientName.IllegalNameException {
        if (container.getContent().getQuantity().isFluidUnit()) {
            if (container.getContent() != null && container.getContent().getQuantity().isSmallerThanOrEqualTo(DROP, amount)) {
                container.getContent().bringBackToStandardTemperature();
                AlchemicIngredient PartialIngredient = new AlchemicIngredient(container.getContent().getFullName(),container.getContent().getTemperature(), container.getContent().getState() , (container.getContent().getQuantity().convertTo(DROP) - amount);
                AlchemicIngredient labIngredient = new AlchemicIngredient(container.getContent().getFullName(), container.getContent().getTemperature(), container.getContent().getState(), amount);
                IngredientContainer partialContainer = new IngredientContainer(PartialIngredient, container.getContainerUnit());
                IngredientContainer labContainer = new IngredientContainer(labIngredient, container.getContainerUnit());
                if (canAddContainer(labContainer)) {
                    containers.add(labContainer);
                } else {
                    throw new IllegalArgumentException("Not enough space to add container");
                }
            }
        }else {
            if (container.getContent() != null && container.getContent().getQuantity().isSmallerThanOrEqualTo(PINCH, amount)) {
                container.getContent().bringBackToStandardTemperature();
                AlchemicIngredient PartialIngredient = new AlchemicIngredient(container.getContent().getFullName(),container.getContent().getTemperature(), container.getContent().getState() , (container.getContent().getQuantity().convertTo(DROP) - amount);
                AlchemicIngredient labIngredient = new AlchemicIngredient(container.getContent().getFullName(), container.getContent().getTemperature(), container.getContent().getState(), amount);
                IngredientContainer partialContainer = new IngredientContainer(PartialIngredient, container.getContainerUnit());
                IngredientContainer labContainer = new IngredientContainer(labIngredient, container.getContainerUnit());
                if (canAddContainer(labContainer)) {
                    containers.add(labContainer);
                } else {
                    throw new IllegalArgumentException("Not enough space to add container");
                }
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
    public IngredientContainer removeIngredient(AlchemicIngredient ingredient, Unit containerUnit, Long amount) throws IngredientName.IllegalNameException {
        AlchemicIngredient labIngredient = null;
        for (IngredientContainer container : containers) {
            if (container.getContent().equals(ingredient)) {
                if (container.getContent().getQuantity().isFluidUnit()) {
                if (container.getContent().getQuantity().isSmallerThan(DROP, amount) < amount && amount >= 0) {
                    throw new IllegalArgumentException("Not enough ingredient to remove");
                }
                if (container.getContent().getQuantity().isEqualTo(new Quantity(0, DROP)){
                    ingredient = new AlchemicIngredient(ingredient.getFullName(), ingredient.getTemperature(), ingredient.getState(), container.getContent().getQuantity().getUnit().convertTo(DROP,50400L) - amount );
                    containers.remove(container);
                }
                labIngredient = new AlchemicIngredient(ingredient.getFullName(), ingredient.getTemperature(), ingredient.getState(), amount);
                IngredientContainer labContainer = new IngredientContainer(ingredient, ingredient.getQuantity().getSmallestFluidContainer());
                containers.add(labContainer);
                break;
            }
                //TODO: else for Powder
            }
        }
        if (labIngredient == null) {
            throw new IllegalArgumentException("Ingredient not found");
        }
        return new IngredientContainer(labIngredient, containerUnit);
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
        return storeroom > Storeroom && storeroom > 0 && (Storeroom - storeroom)* 50400 >= Laboratory.getFilledSpace();
    }

    /**
     * Checks if the containers list is empty.
     * This method returns true if there are no containers in the list, and false otherwise.
     *
     * @return true if the containers list is empty, false otherwise
     */
    public Boolean isEmpty(){
        return containers.isEmpty();
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
            //TODO: empty device and add contents to the inventory of the laboratory
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
    public static class LaboratoryMissingDeviceException extends Exception {
        public LaboratoryMissingDeviceException(String e){super(e);}
    }
    private AlchemicIngredient bringBackToStandardTemperature(AlchemicIngredient ingredient) throws Device.DeviceFullException, LaboratoryMissingDeviceException {
        boolean changed = false;
        Temperature targetTemp = ingredient.getStandardType().getStandardTemperature();
        if(ingredient.getTemperature().isColderThan(targetTemp)){
            for(Device device: devices){
                if(device.getClass() == Oven.class){
                    if(ingredient.getState().isSolid()) {
                        ((Oven) device).addIngredient(new IngredientContainer(ingredient, ingredient.getQuantity().getSmallestPowderContainer()));
                    }
                    else{
                        ((Oven) device).addIngredient(new IngredientContainer(ingredient, ingredient.getQuantity().getSmallestPowderContainer()));
                    }
                    try {
                        device.react();
                        changed = true;
                        return(device.getContents().getContent());
                    } catch (Device.NotInLaboratoryException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        else{
            for(Device device: devices){
                if(device.getClass() == CoolingBox.class){
                    if(ingredient.getState().isSolid()) {
                        ((CoolingBox) device).addIngredient(new IngredientContainer(ingredient, ingredient.getQuantity().getSmallestPowderContainer()));
                    }
                    else{
                        ((CoolingBox) device).addIngredient(new IngredientContainer(ingredient, ingredient.getQuantity().getSmallestPowderContainer()));
                    }
                    try {
                        device.react();
                        changed = true;
                        return(device.getContents().getContent());
                    } catch (Device.NotInLaboratoryException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        throw new LaboratoryMissingDeviceException("Missing oven or coolingbox");
    }

    /**
     * function made to execute a recipe
     * @param recipe the recipe that will be followed
     * @throws LaboratoryMissingDeviceException
     */
    public void executeRecipe(Recipe recipe) throws LaboratoryMissingDeviceException {
        boolean stopRecipe = false;
        ArrayList<AlchemicIngredient> usedIngredients = new ArrayList<>();
        ActionType[] actions = recipe.getActions();
        AlchemicIngredient[] ingredients = recipe.getIngredients();
        AlchemicIngredient currentIngredient;
        for(ActionType action: actions){
            switch (action){
                case MIX:
                Kettle kettle = null;
                for(Device device: devices){
                    if(device.getClass() == Kettle.class){
                        kettle = (Kettle) device;
                    }
                }
                if(kettle == null){
                    stopRecipe = true;
                    break;
                }
                for(AlchemicIngredient ingredient: usedIngredients){
                    if(ingredient.getState().isSolid()){
                        kettle.addIngredient(new IngredientContainer(ingredient,ingredient.getQuantity().getSmallestPowderContainer()));
                    }
                    else{
                        kettle.addIngredient(new IngredientContainer(ingredient,ingredient.getQuantity().getSmallestFluidContainer()));
                    }
                }
                try {
                    kettle.react();
                } catch (Device.NotInLaboratoryException e) {
                    throw new RuntimeException(e);
                }
                usedIngredients.clear();
                usedIngredients.add(kettle.getContent().getFirst().getContent());
                break;
                case ADD:
                    //TODO: Remove certain quantity ingredient from lab and add to usedIngredients
                    break;
                case COOL:
                    currentIngredient = usedIngredients.getLast();
                    CoolingBox coolingBox = null;
                    for(Device device: devices){
                        if(device.getClass() == CoolingBox.class){
                            coolingBox = (CoolingBox) device;
                        }
                    }
                    if(coolingBox == null){
                        stopRecipe = true;
                        break;
                    }
                    if(currentIngredient.getState().isSolid()){
                        try {
                            coolingBox.addIngredient(new IngredientContainer(currentIngredient,currentIngredient.getQuantity().getSmallestPowderContainer()));
                        } catch (Device.DeviceFullException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        try {
                            coolingBox.addIngredient(new IngredientContainer(currentIngredient,currentIngredient.getQuantity().getSmallestFluidContainer()));
                        } catch (Device.DeviceFullException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    coolingBox.setTemperature(currentIngredient.getTemperature());
                    coolingBox.lowerOwnTemp();
                    try {
                        coolingBox.react();
                    } catch (Device.NotInLaboratoryException e) {
                        throw new RuntimeException(e);
                    }
                    usedIngredients.remove(usedIngredients.getLast());
                    usedIngredients.add(coolingBox.getContents().getContent());
                    break;
                case HEAT:
                    AlchemicIngredient ingredient = usedIngredients.getLast();
                    Oven oven = null;
                    for(Device device: devices){
                        if(device.getClass() == Oven.class){
                            oven = (Oven) device;
                        }
                    }
                    if(oven == null){
                        stopRecipe = true;
                        break;
                    }
                    if(ingredient.getState().isSolid()){
                        try {
                            oven.addIngredient(new IngredientContainer(ingredient,ingredient.getQuantity().getSmallestPowderContainer()));
                        } catch (Device.DeviceFullException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        try {
                            oven.addIngredient(new IngredientContainer(ingredient,ingredient.getQuantity().getSmallestFluidContainer()));
                        } catch (Device.DeviceFullException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    oven.setTemperature(ingredient.getTemperature());
                    oven.heatOwnTemperature();
                    try {
                        oven.react();
                    } catch (Device.NotInLaboratoryException e) {
                        throw new RuntimeException(e);
                    }
                    usedIngredients.remove(usedIngredients.getLast());
                    usedIngredients.add(oven.getContents().getContent());


            }
            if(stopRecipe){
                for(AlchemicIngredient ingredient: usedIngredients){
                    IngredientContainer labContainer = null;
                    try {
                        bringBackToStandardTemperature(ingredient);
                    } catch (Device.DeviceFullException e) {
                        throw new RuntimeException(e);
                    }
                    if(!ingredient.getState().isSolid()) {
                        Unit Unit = ingredient.getQuantity().getSmallestFluidContainer();
                        labContainer = new IngredientContainer(ingredient, Unit);
                    } else if (ingredient.getState().isSolid()) {
                        Unit Unit = ingredient.getQuantity().getSmallestPowderContainer();
                        labContainer = new IngredientContainer(ingredient, Unit);
                    }
                    this.addContainer(labContainer);
                }
                break;
            }

        }

    }
}
