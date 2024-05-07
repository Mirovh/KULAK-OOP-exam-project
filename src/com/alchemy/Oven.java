package com.alchemy;
import java.util.Random;


public class Oven extends Device{
    private Temperature temperature;
    @Override
    public void addIngredient(IngredientContainer container) throws Exception{
        if (ingredient != null){
            throw new DeviceFullException("Oven Full");
        }
        else{
            super.addIngredient(container);
        }

    }
    public void setTemperature(Long coldness,Long hotness){
        temperature.heat(temperature.getColdness());
        temperature.cool(temperature.getHotness());
        temperature.cool(coldness);
        temperature.heat(hotness);
    }
    @Override
    public void react(){
        if(ingredient == null){
            return;
        }
        else{
            Temperature ingredientTemperature= ingredient.getTemperature();
            Long ingredientHotness = ingredientTemperature.getHotness();
            Long ingredientColdness = ingredientTemperature.getColdness();
            Long hotness = temperature.getHotness();
            Long coldness = temperature.getColdness();
            if(ingredientHotness >= hotness | ingredientColdness< coldness){
                return;
            }
            else{
                Random rand = new Random();
                Integer randomDeviation = rand.nextInt(11)-5;
                ingredientTemperature.heat(ingredientColdness-coldness+hotness-ingredientHotness + randomDeviation);
            }
        }
    }
}
