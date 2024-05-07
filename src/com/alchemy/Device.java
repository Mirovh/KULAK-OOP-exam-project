package com.alchemy;
import java.util.ArrayList;
public class Device {
    protected AlchemicIngredient ingredient;
    public void addIngredient(IngredientContainer container) throws Exception{
        ingredient = container.getContents();
        container.Destroy();

    }
    public static class DeviceFullException extends Exception{
        public DeviceFullException(String e){
            super(e);
        }
    }
    public void react(){}

    public IngredientContainer getContents(){
        int size = ingredient.getQuantity(); //TODO: calculate minimal needed quantity
        IngredientContainer container = new IngredientContainer(ingredient,size);
        ingredient = null;
        return container;
    }

    public boolean canAddIngredient(AlchemicIngredient ingredient){
        return true;
    }
}
