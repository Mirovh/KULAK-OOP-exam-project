package com.alchemy.quantity;

import be.kuleuven.cs.som.annotate.*;

import java.util.Objects;

/**
 * Represents a quantity with a specific amount and unit.
 * Nominally programmed.
 *
 * @invar The amount of the quantity is always non-negative.
 * @invar The unit of the quantity is always non-null.
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class Quantity {
    /**
     * The amount of the quantity. (in the unit specified by the unit variable)
     */
    private Float amount;
    /**
     * The unit of the quantity.
     */
    private Unit unit;

    /**
     * Constructs a new Quantity with the specified amount and unit.
     *
     * @param amount the amount of the quantity
     *               | amount >= 0
     * @param unit the unit of the quantity
     *             | unit != null
     * @post The amount and unit of the quantity are set to the specified values.
     * | this.amount = amount
     */
    @Raw
    public Quantity(int amount, Unit unit) {
        this((float) amount, unit);
    }

    /**
     * Constructs a new Quantity with the specified amount and unit.
     *
     * @param amount the amount of the quantity
     *           | amount >= 0
     * @param unit the unit of the quantity
     *           | unit != null
     * @post The amount and unit of the quantity are set to the specified values.
     *          | this.amount = amount
     *          | this.unit = unit
     */
    @Raw
    public Quantity(Float amount, Unit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    /**
     * Converts this quantity to the specified unit.
     *
     * @param unit the unit to convert to
     *      | unit != null
     *      | if (unit instanceof FluidUnit) { this.isFluidUnit() } else { this.isPowderUnit() }
     * @post The amount of the quantity is converted to the specified unit.
     *      | this.amount = this.unit.convertTo(unit, this.amount)
     * @post The unit of the quantity is set to the specified unit.
     *     | this.unit = unit
     * @effect The amount of the quantity is converted to the specified fluid unit and the unit is set to the specified fluid unit.
     * @return the amount of the quantity in the new Unit
     */
    @Raw
    public Float convertTo(Unit unit) {
        this.amount = this.unit.convertTo(unit, this.amount);
        this.unit = unit;
        return amount;
    }

    /**
     * Converts this quantity to the base unit.
     *
     * @post The amount of the quantity is converted to the base unit.
     *      | this.amount = this.unit.convertToBase(this.amount)
     * @post The unit of the quantity is set to the base unit.
     *     | this.unit = this.unit.getBaseUnit()
     */
    public void convertToBase() {
        this.amount = this.unit.convertToBase(this.amount);
        this.unit = this.unit.getBaseUnit();
    }

    /**
     * Converts this quantity to the specified fluid unit.
     *
     * @param unit the fluid unit to convert to
     *      | unit != null
     * @post The amount of the quantity is converted to the specified fluid unit.
     *      | this.amount = this.unit.convertTo(unit, this.amount)
     * @post The unit of the quantity is set to the specified fluid unit.
     *     | this.unit = unit
     * @effect The amount of the quantity is converted to the specified fluid unit and the unit is set to the specified fluid unit.
     * @return the amount of the quantity in the new Unit
     */
    @Raw
    public Float convertToFluidUnit(FluidUnit unit) {
        // storerooms and spoons are the same for both fluid and powder units
        this.convertTo(PowderUnit.SPOON);
        this.unit = FluidUnit.SPOON;
        this.convertTo(unit);
        return amount;
    }

    /**
     * Converts this quantity to the specified powder unit.
     *
     * @param unit the powder unit to convert to
     *             | unit != null
     * @post The amount of the quantity is converted to the specified powder unit.
     *      | this.amount = this.unit.convertTo(unit, this.amount)
     * @post The unit of the quantity is set to the specified powder unit.
     *    | this.unit = unit
     * @effect The amount of the quantity is converted to the specified powder unit and the unit is set to the specified powder unit.
     * @return the amount of the quantity in the new Unit
     */
    @Raw
    public Float convertToPowderUnit(PowderUnit unit) {
        // storerooms and spoons are the same for both fluid and powder units
        this.convertTo(FluidUnit.SPOON);
        this.unit = PowderUnit.SPOON;
        this.convertTo(unit);
        return(amount);
    }

    /**
     * Returns whether this quantity is a fluid unit.
     *
     * @return true if the unit is a fluid unit, false otherwise
     */
    @Basic
    public boolean isFluidUnit() {
        return unit instanceof FluidUnit;
    }

    /**
     * Returns whether the unit of this quantity is a powder unit.
     *
     * @return true if the unit is a powder unit, false otherwise
     */
    @Basic
    public boolean isPowderUnit() {
        return unit instanceof PowderUnit;
    }

    /**
     * Returns the smallest fluid unit for which a container would fit this quantity.
     *
     * @return the smallest container unit for this quantity
     * @pre The unit of this quantity is a fluid unit.
     */
    @Basic
    public Unit getSmallestFluidContainer() {
        Unit smallestContainerUnit = null;
        Float smallestContainerAmount = Float.MAX_VALUE;

        for (Unit other : FluidUnit.values()) {
            Float converted = unit.convertTo(other, amount);
            if (smallestContainerAmount > 1 && converted < smallestContainerAmount) {
                smallestContainerAmount = converted;
                smallestContainerUnit = other;
            } else if (smallestContainerAmount <= 1 && converted > smallestContainerAmount && converted <= 1) {
                smallestContainerAmount = converted;
                smallestContainerUnit = other;

            }
        }

        return smallestContainerUnit;
    }

    /**
     * Returns the smallest powder unit for which a container would fit this quantity.
     *
     * @return the smallest container unit for this quantity
     * @pre The unit of this quantity is a powder unit.
     */
    @Basic
    public Unit getSmallestPowderContainer() {
        Unit smallestContainerUnit = null;
        Float smallestContainerAmount = Float.MAX_VALUE;

            for (Unit other : PowderUnit.values()) {
                Float converted = unit.convertTo(other, amount);
                if (smallestContainerAmount > 1 && converted < smallestContainerAmount) {
                    smallestContainerAmount = converted;
                    smallestContainerUnit = other;
                } else if (smallestContainerAmount <= 1 && converted > smallestContainerAmount && converted <= 1) {
                    smallestContainerAmount = converted;
                    smallestContainerUnit = other;

                }
            }
        return smallestContainerUnit;
    }

    /**
     * Returns the current unit of this quantity.
     *
     * @return the unit of this quantity
     */
    @Basic
    public Unit getUnit(){
        return this.unit;
    }

    /**
     * Checks if this quantity is greater than the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is greater, false otherwise
     * @pre The unit of this quantity is of the same type as the unit of the specified quantity.
     */
    @Basic
    public boolean isGreaterThan(Quantity quantity) {
        return this.unit.convertToBase(this.amount) > quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is greater than (one of) the specified unit.
     *
     * @param unit the unit to compare with
     *             | unit != null
     * @param amount the amount of the unit to compare with
     * @return true if this quantity is greater, false otherwise
     * @pre The unit of this quantity is of the same type as the specified unit.
     */
    @Basic
    public boolean isGreaterThan(Unit unit, float amount) {
        return this.isGreaterThan(new Quantity(amount, unit));
    }

    /**
     * Checks if this quantity is smaller than the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is smaller, false otherwise
     * @pre The unit of this quantity is of the same type as the unit of the specified quantity.
     */
    @Basic
    public boolean isSmallerThan(Quantity quantity) {
        return this.unit.convertToBase(this.amount) < quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is smaller than (one of) the specified unit.
     *
     * @param unit the unit to compare with
     *             | unit != null
     * @return true if this quantity is smaller, false otherwise
     * @pre The unit of this quantity is of the same type as the specified unit.
     */
    @Basic
    public boolean isSmallerThan(Unit unit, float amount) {
        return this.isSmallerThan(new Quantity(amount, unit));
    }

    /**
     * Checks if this quantity is equal to the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is equal, false otherwise
     * @pre The unit of this quantity is of the same type as the unit of the specified quantity.
     */
    @Basic
    public boolean isEqualTo(Quantity quantity) {
        float EPSILON = 0.001f;
        return Math.abs(this.unit.convertToBase(this.amount) - quantity.unit.convertToBase(quantity.amount)) < EPSILON;
    }

    /**
     * Checks if this quantity is equal to (one of) the specified unit.
     *
     * @param unit the unit to compare with
     *             | unit != null
     * @param amount the amount of the unit to compare with
     * @return true if this quantity is equal, false otherwise
     * @pre The unit of this quantity is of the same type as the specified unit.
     */
    @Basic
    public boolean isEqualTo(Unit unit, float amount) {
        return this.isEqualTo(new Quantity(amount, unit));
    }

    /**
     * Checks if this quantity is greater than or equal to the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is greater than or equal, false otherwise
     * @pre The unit of this quantity is of the same type as the unit of the specified quantity.
     */
    @Basic
    public boolean isGreaterThanOrEqualTo(Quantity quantity) {
        return this.unit.convertToBase(this.amount) >= quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is greater than or equal to (one of) the specified unit.
     *
     * @param unit the unit to compare with
     *             | unit != null
     * @param amount the amount of the unit to compare with
     * @return true if this quantity is greater than or equal, false otherwise
     * @pre The unit of this quantity is of the same type as the specified unit.
     */
    @Basic
    public boolean isGreaterThanOrEqualTo(Unit unit, float amount) {
        return this.isGreaterThanOrEqualTo(new Quantity(amount, unit));
    }

    /**
     * Checks if this quantity is smaller than or equal to the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is smaller than or equal, false otherwise
     * @pre The unit of this quantity is of the same type as the unit of the specified quantity.
     */
    @Basic
    public boolean isSmallerThanOrEqualTo(Quantity quantity) {
        return this.unit.convertToBase(this.amount) <= quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is smaller than or equal to (one of) the specified unit.
     *
     * @param unit the unit to compare with
     *             | unit != null
     * @return true if this quantity is smaller than or equal, false otherwise
     * @pre The unit of this quantity is of the same type as the specified unit.
     */
    @Basic
    public boolean isSmallerThanOrEqualTo(Unit unit, float amount) {
        return this.isSmallerThanOrEqualTo(new Quantity(amount, unit));
    }

    /**
     * Returns a string representation of this quantity.
     *
     * @return a string representation of this quantity
     */
    public String toString() {
        return amount + " " + unit.getName();
    }

}
