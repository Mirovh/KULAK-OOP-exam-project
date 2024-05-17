package com.alchemy;
import com.alchemy.IngredientConditions.CoolingBox;
import com.alchemy.IngredientConditions.Kettle;
import com.alchemy.IngredientConditions.Oven;
import com.alchemy.IngredientConditions.Temperature;
import com.alchemy.recipes.Recipe;
import com.alchemy.recipes.Recipe.*;

import javax.swing.*;
import java.util.ArrayList;
public class Laboratory {
    private ArrayList<Device> devices;

    public Laboratory(){
        devices = new ArrayList<>();
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }
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
                    try {
                        bringBackToStandardTemperature(ingredient);
                    } catch (Device.DeviceFullException e) {
                        throw new RuntimeException(e);
                    }
                    //TODO: add ingredient back to Laboratory's inventory
                }
                break;
            }

        }

    }
}
