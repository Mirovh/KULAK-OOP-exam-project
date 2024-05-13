package com.alchemy.quantity;

/**
 * Represents a quantity with a specific amount and unit.
 * @invar The amount of the quantity is always non-negative.
 * @invar The unit of the quantity is always non-null.
 */
public class Quantity {
    private Long amount;
    private Unit unit;

    /**
     * Constructs a new Quantity with the specified amount and unit.
     *
     * @param amount the amount of the quantity
     *               | amount >= 0
     * @param unit the unit of the quantity
     *             | unit != null
     * @post The amount and unit of the quantity are set to the specified values.
     */
    public Quantity(int amount, Unit unit) {
        this((long) amount, unit);
    }

    /**
     * Constructs a new Quantity with the specified amount and unit.
     *
     * @param amount the amount of the quantity
     *               | amount >= 0
     * @param unit the unit of the quantity
     *             | unit != null
     * @post The amount and unit of the quantity are set to the specified values.
     */
    public Quantity(Long amount, Unit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    /**
     * Converts this quantity to the specified unit.
     *
     * @param unit the unit to convert to
     *             | unit != null
     * @post The amount of the quantity is converted to the specified unit.
     * @post The unit of the quantity is set to the specified unit.
     */
    public void convertTo(Unit unit) {
        this.amount = this.unit.convertTo(unit, this.amount);
        this.unit = unit;
    }

    /**
     * Returns the smallest unit for which a container would fit this quantity.
     *
     * @return the smallest container unit for this quantity
     */
    public Unit getSmallestContainer() {
        Unit smallestContainerUnit = null;
        Long smallestContainerAmount = Long.MAX_VALUE;
        for (Unit other : unit.values()) {
            Long converted = unit.convertTo(other, amount);
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
     * Checks if this quantity is greater than the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is greater, false otherwise
     */
    public boolean isGreaterThan(Quantity quantity) {
        return this.unit.convertToBase(this.amount) > quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is smaller than the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is smaller, false otherwise
     */
    public boolean isSmallerThan(Quantity quantity) {
        return this.unit.convertToBase(this.amount) < quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is equal to the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is equal, false otherwise
     */
    public boolean isEqualTo(Quantity quantity) {
        return this.unit.convertToBase(this.amount) == quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is greater than or equal to the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is greater than or equal, false otherwise
     */
    public boolean isGreaterThanOrEqualTo(Quantity quantity) {
        return this.unit.convertToBase(this.amount) >= quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is smaller than or equal to the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is smaller than or equal, false otherwise
     */
    public boolean isSmallerThanOrEqualTo(Quantity quantity) {
        return this.unit.convertToBase(this.amount) <= quantity.unit.convertToBase(quantity.amount);
    }
}
