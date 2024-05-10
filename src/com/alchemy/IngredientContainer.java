package com.alchemy;
//TODO: Major overhaul, currently just a placeholder
public class IngredientContainer {
    private AlchemicIngredient ingredient;

    public IngredientContainer(AlchemicIngredient ingredient,Integer size){
        this.ingredient=ingredient;
        this.size=size;
    }
    public IngredientContainer(Integer size){
        this.ingredient = null;
        this.size = size;
    }
    private Integer size; //TODO: change to right size
    public AlchemicIngredient getContents(){
        return ingredient;
    }

    public void setIngredient(AlchemicIngredient ingredient){
        ingredient = ingredient;
    }
    public void Destroy(){
        ingredient = null;
    }


}
