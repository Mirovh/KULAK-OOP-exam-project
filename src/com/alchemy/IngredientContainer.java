package com.alchemy;

import com.alchemy.quantity.FluidUnit;
import com.alchemy.quantity.PowderUnit;
import com.alchemy.quantity.Unit;
/**********************************************************
 * Represents a container for alchemic ingredients.
 * @invar The containerUnit of the IngredientContainer is always non-null.
 * nominally programmed
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 **********************************************************/
public class IngredientContainer {
    private AlchemicIngredient content;
    private final Unit containerUnit;
    private static final Unit[] blacklistedUnits = {PowderUnit.values()[0], PowderUnit.values()[PowderUnit.values().length - 1], FluidUnit.values()[0], FluidUnit.values()[FluidUnit.values().length - 1]};

    /**
     * Constructs a new IngredientContainer with the specified content and container unit.
     *
     * @param content the alchemic ingredient to be contained
     * @param containerUnit the unit of the container
     *                      | containerUnit != null
     * @post The content and containerUnit of the IngredientContainer are set to the specified values.
     *    | this.content = content
     *    | this.containerUnit = containerUnit
     */
    public IngredientContainer(AlchemicIngredient content, Unit containerUnit) {
        this.content = content;
        for (Unit unit : blacklistedUnits) {
            if (unit.equals(containerUnit)) {
                throw new IllegalArgumentException("The unit of the container cannot be the smallest or largest unit.");
            }
        }
        this.containerUnit = containerUnit;
    }

    /**
     * Constructs a new IngredientContainer with the specified container unit and no content.
     *
     * @param containerUnit the unit of the container
     * @pre The containerUnit must be non-null.
     *     | containerUnit != null
     * @post The containerUnit of the IngredientContainer is set to the specified value and the content is null.
     *     | this.containerUnit = containerUnit
     *     | this.content = null
     */
    public IngredientContainer(Unit containerUnit) {
        this(null, containerUnit);
    }

    /**
     * Checks if the specified alchemic ingredient fits in this IngredientContainer.
     *
     * @param ingredient the alchemic ingredient to check
     *                   | ingredient != null
     * @return true if the quantity of the ingredient is smaller than or equal to the size of the container unit
     *         | result == ingredient.getQuantity().isSmallerThanOrEqualTo(this.containerUnit)
     */
    public boolean fits(AlchemicIngredient ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("The ingredient cannot be null.");
        }
        return ingredient.getQuantity().isSmallerThanOrEqualTo(this.containerUnit);
    }

    /**
     * Returns the unit of this IngredientContainer.
     *
     * @return the unit of this IngredientContainer
     */
    public Unit getContainerUnit() {
        return containerUnit;
    }

    /**
     * Returns the content of this IngredientContainer.
     *
     * @return the content of this IngredientContainer, or null if it is empty
     */
    public AlchemicIngredient getContent() {
        return content;
    }

    /**
     * Sets the content of this IngredientContainer.
     *
     * @param content the alchemic ingredient to be contained
     *                | content != null
     *                | this.fits(content)
     * @post The content of the IngredientContainer is set to the specified value.
     *   | this.content = content
     */
    public void setContent(AlchemicIngredient content){
        if (!this.fits(content)) {
            throw new IllegalArgumentException("The ingredient does not fit in the container.");
        }
        this.content = content;
    }

    /**
     * Empties the content of this IngredientContainer.
     *
     * @post The content of the IngredientContainer is null.
     *      | content == null
     */
    public void empty(){
        this.setContent(null);
    }

    /**
     * Destroys the content of this IngredientContainer.
     *
     * @post The content of the IngredientContainer is null.
     *     | content == null
     */
    public void destroy(){
        this.empty();
    }

}