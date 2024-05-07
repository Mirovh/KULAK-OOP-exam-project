package com.alchemy;

public class IngredientContainer {
    private AlchemicIngredient ingredient;

    public IngredientContainer(AlchemicIngredient ingredient,Integer size){
        this.ingredient=ingredient;
        this.size=size;
    }
    private Integer size; //TODO: change to right size
    public AlchemicIngredient getContents(){
        return ingredient;
    }

    public void Destroy(){
        ingredient = null;
    }


}
