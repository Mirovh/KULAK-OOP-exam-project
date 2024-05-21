package com.alchemy.recipes;

import be.kuleuven.cs.som.annotate.*;
import com.alchemy.AlchemicIngredient;

/**
 * Represents a recipe for alchemic ingredients.
 * Defensively programmed.
 *
 * @invar The actions array always ends with ActionType.MIX.
 * @invar Every element of the ingredients array is non-null.
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class Recipe {

    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * Represents the type of action that can be performed on an ingredient.
     */
    public enum ActionType {
        ADD, HEAT, COOL, MIX
    }
    /**
     * The actions to be performed in the recipe.
     */
    private final ActionType[] actions;
    /**
     * The ingredients to be used in the recipe.
     */
    private final AlchemicIngredient[] ingredients;


    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Constructs a new Recipe with the specified actions and ingredients.
     *
     * @param actions the actions to be performed in the recipe
     * @param ingredients the ingredients to be used in the recipe
     *                    | for (AlchemicIngredient ingredient : ingredients) { ingredient != null }
     * @post If the last action is not ActionType.MIX, ActionType.MIX is added to the end of the actions array.
     *   | if (actions[actions.length - 1] != ActionType.MIX) { this.actions = new ActionType[actions.length + 1]; this.actions[actions.length] = ActionType.MIX; }
     * @post The actions and ingredients of the Recipe are set to the specified values.
     *    | this.actions = actions
     *    | this.ingredients = ingredients
     */
    @Raw
    public Recipe(ActionType[] actions, AlchemicIngredient[] ingredients) {
        if (actions[actions.length - 1] != ActionType.MIX) {
            this.actions = new ActionType[actions.length + 1];
            System.arraycopy(actions, 0, this.actions, 0, actions.length);
            this.actions[actions.length] = ActionType.MIX;
        } else {
            this.actions = actions;
        }
        for (AlchemicIngredient ingredient : ingredients) {
            if (ingredient == null) {
                throw new IllegalArgumentException("The ingredients cannot be null.");
            }
        }
        this.ingredients = ingredients;
    }


    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * Returns the actions of this Recipe.
     *
     * @return the actions of this Recipe
     */
    @Basic
    public ActionType[] getActions() {
        return actions;
    }

    /**
     * Returns the ingredients of this Recipe.
     *
     * @return the ingredients of this Recipe
     */
    @Basic
    public AlchemicIngredient[] getIngredients() {
        return ingredients;
    }
}