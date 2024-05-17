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
     *           | amount >= 0
     * @param unit the unit of the quantity
     *           | unit != null
     * @post The amount and unit of the quantity are set to the specified values.
     *          | this.amount = amount
     *          | this.unit = unit
     */
    public Quantity(Long amount, Unit unit) {
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
     */
    public void convertTo(Unit unit) {
        this.amount = this.unit.convertTo(unit, this.amount);
        this.unit = unit;
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
     * @return the amount of the quantity in the new Unit
     */
    public Long convertToFluidUnit(FluidUnit unit) {
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
     * @return the amount of the quantity in the new Unit
     */
    public Long convertToPowderUnit(PowderUnit unit) {
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
    public boolean isFluidUnit() {
        return unit instanceof FluidUnit;
    }

    /**
     * Returns whether the unit of this quantity is a powder unit.
     *
     * @return true if the unit is a powder unit, false otherwise
     */
    public boolean isPowderUnit() {
        return unit instanceof PowderUnit;
    }

    /**
     * Returns the smallest fluid unit for which a container would fit this quantity.
     *
     * @return the smallest container unit for this quantity
     * @pre The unit of this quantity is a fluid unit.
     */
    public Unit getSmallestFluidContainer() {
        Unit smallestContainerUnit = null;
        Long smallestContainerAmount = Long.MAX_VALUE;

        for (Unit other : FluidUnit.values()) {
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
     * Returns the smallest powder unit for which a container would fit this quantity.
     *
     * @return the smallest container unit for this quantity
     * @pre The unit of this quantity is a powder unit.
     */
    public Unit getSmallestPowderContainer() {
        Unit smallestContainerUnit = null;
        Long smallestContainerAmount = Long.MAX_VALUE;

            for (Unit other : PowderUnit.values()) {
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
     * Returns the current unit of this quantity.
     *
     * @return the unit of this quantity
     */
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
    public boolean isGreaterThan(Quantity quantity) {
        return this.unit.convertToBase(this.amount) > quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is greater than (one of) the specified unit.
     *
     * @param unit the unit to compare with
     *             | unit != null
     * @return true if this quantity is greater, false otherwise
     * @pre The unit of this quantity is of the same type as the specified unit.
     */
    public boolean isGreaterThan(Unit unit) {
        return this.isGreaterThan(new Quantity(1L, unit));
    }

    /**
     * Checks if this quantity is smaller than the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is smaller, false otherwise
     * @pre The unit of this quantity is of the same type as the unit of the specified quantity.
     */
    public boolean isSmallerThan(Quantity quantity) {
        return this.unit.convertToBase(this.amount) < quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is smaller than (one of) the specified unit.
     *
     * @param unit   the unit to compare with
     *               | unit != null
     * @param amount
     * @return true if this quantity is smaller, false otherwise
     * @pre The unit of this quantity is of the same type as the specified unit.
     */
    public boolean isSmallerThan(Unit unit, Long amount) {
        return this.isSmallerThan(new Quantity(1L, unit));
    }

    /**
     * Checks if this quantity is equal to the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is equal, false otherwise
     * @pre The unit of this quantity is of the same type as the unit of the specified quantity.
     */
    public boolean isEqualTo(Quantity quantity) {
        return this.unit.convertToBase(this.amount) == quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is equal to (one of) the specified unit.
     *
     * @param unit the unit to compare with
     *             | unit != null
     * @return true if this quantity is equal, false otherwise
     * @pre The unit of this quantity is of the same type as the specified unit.
     */
    public boolean isEqualTo(Unit unit) {
        return this.isEqualTo(new Quantity(1L, unit));
    }

    /**
     * Checks if this quantity is greater than or equal to the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is greater than or equal, false otherwise
     * @pre The unit of this quantity is of the same type as the unit of the specified quantity.
     */
    public boolean isGreaterThanOrEqualTo(Quantity quantity) {
        return this.unit.convertToBase(this.amount) >= quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is greater than or equal to (one of) the specified unit.
     *
     * @param unit the unit to compare with
     *             | unit != null
     * @return true if this quantity is greater than or equal, false otherwise
     * @pre The unit of this quantity is of the same type as the specified unit.
     */
    public boolean isGreaterThanOrEqualTo(Unit unit) {
        return this.isGreaterThanOrEqualTo(new Quantity(1L, unit));
    }

    /**
     * Checks if this quantity is smaller than or equal to the specified quantity.
     *
     * @param quantity the quantity to compare with
     *                 | quantity != null
     * @return true if this quantity is smaller than or equal, false otherwise
     * @pre The unit of this quantity is of the same type as the unit of the specified quantity.
     */
    public boolean isSmallerThanOrEqualTo(Quantity quantity) {
        return this.unit.convertToBase(this.amount) <= quantity.unit.convertToBase(quantity.amount);
    }

    /**
     * Checks if this quantity is smaller than or equal to (one of) the specified unit.
     *
     * @param unit   the unit to compare with
     *               | unit != null
     * @param amount
     * @return true if this quantity is smaller than or equal, false otherwise
     * @pre The unit of this quantity is of the same type as the specified unit.
     */
    public boolean isSmallerThanOrEqualTo(Unit unit, Long amount) {
        return this.isSmallerThanOrEqualTo(new Quantity(1L, unit));
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
