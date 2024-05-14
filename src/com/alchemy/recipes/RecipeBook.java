package com.alchemy.recipes;

import java.util.List;

/**
 * Represents a collection of recipes.
 * @invar Each recipe in a recipe book is non-null.
 */
public class RecipeBook {
    private List<Recipe> recipes;

    /**
     * Constructs a new RecipeBook with the specified recipes.
     *
     * @param recipes the recipes to be added to the recipe book
     *                | for (Recipe recipe : recipes) { recipe != null }
     * @post The recipes of the RecipeBook are set to the specified values.
     *       | this.recipes = recipes
     */
    public RecipeBook(Recipe[] recipes) {
        for (Recipe recipe : recipes) {
            if (recipe == null) {
                throw new IllegalArgumentException("The recipes cannot be null.");
            }
        }
        this.recipes = List.of(recipes);
    }

    /**
     * Returns the recipes of this RecipeBook.
     *
     * @return the recipes of this RecipeBook
     */
    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Adds a recipe to this RecipeBook.
     *
     * @param recipe the recipe to add
     *               | recipe != null
     * @post The recipe is added to the end of the RecipeBook.
     *      | new.getRecipes().contains(recipe)
     *      | new.getRecipes().get(new.getRecipes().size() - 1) == recipe
     */
    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    /**
     * Removes a recipe from this RecipeBook.
     *
     * @param recipe the recipe to remove
     *               | recipe != null
     * @post The recipe is removed from the RecipeBook.
     *       | !new.getRecipes().contains(recipe)
     */
    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
    }

    /**
     * Returns the recipe at the specified index.
     *
     * @param index the index of the recipe to return
     *              | 0 <= index < recipes.size()
     * @return the recipe at the specified index
     */
    public Recipe getRecipe(int index) {
        return recipes.get(index);
    }

}