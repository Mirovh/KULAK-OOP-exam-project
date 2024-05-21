package com.alchemy.recipes;

import be.kuleuven.cs.som.annotate.*;

import java.util.List;

/**
 * Represents a collection of recipes.
 * Defensively programmed.
 *
 * @invar Each recipe in a recipe book is non-null.
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class RecipeBook {

    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * The recipes in the recipe book.
     */
    private List<Recipe> recipes;


    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Constructs a new RecipeBook with the specified recipes.
     *
     * @param recipes the recipes to be added to the recipe book
     *                | for (Recipe recipe : recipes) { recipe != null }
     * @post The recipes of the RecipeBook are set to the specified values.
     *       | this.recipes = recipes
     */
    @Raw
    public RecipeBook(Recipe[] recipes) {
        for (Recipe recipe : recipes) {
            if (recipe == null) {
                throw new IllegalArgumentException("The recipes cannot be null.");
            }
        }
        this.recipes = List.of(recipes);
    }


    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * Returns the recipes of this RecipeBook.
     *
     * @return the recipes of this RecipeBook
     */
    @Basic
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
        if (recipe == null) {
            throw new IllegalArgumentException("The recipe cannot be null.");
        }
        recipes.add(recipe);
    }

    /**
     * Removes a recipe from this RecipeBook.
     *
     * @param recipe the recipe to remove
     *               | recipe != null
     *               | getRecipes().contains(recipe)
     * @post The recipe is removed from the RecipeBook.
     *       | !new.getRecipes().contains(recipe)
     */
    public void removeRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("The recipe cannot be null.");
        }
        recipes.remove(recipe);
    }

    /**
     * Returns the recipe at the specified index.
     *
     * @param index the index of the recipe to return
     *              | 0 <= index < recipes.size()
     * @return the recipe at the specified index
     */
    @Basic
    public Recipe getRecipe(int index) {
        if (index < 0 || index >= recipes.size()) {
            throw new IllegalArgumentException("The index is out of bounds.");
        }
        return recipes.get(index);
    }
}