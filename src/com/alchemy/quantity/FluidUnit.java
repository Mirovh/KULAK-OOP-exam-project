package com.alchemy.quantity;

import be.kuleuven.cs.som.annotate.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the units for fluid measurements.
 *
 * @note Always initialize the base unit first.
 * @note DO NOT initialize a unit with a comparison to itself or a unit that has not been initialized yet.
 * @note The smallest unit should be the first and the largest unit should be the last in the enum.
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public enum FluidUnit implements Unit {

    /**
     * The base unit for fluid measurements.
     */
    DROP("drop"),

    SPOON("spoon", 8F, DROP),
    VIAL("sachet", 5F, SPOON),
    BOTTLE("box", 3F, VIAL),
    JUG("sack", 7F, BOTTLE),
    BARREL("chest", 12F, JUG),
    STOREROOM("storeroom", 5F, BARREL);


    /******************************************************************
     * Variables
     **********************************************************/

    /**
     * The name of the unit in human-readable format.
     */
    private final String name;
    /**
     * The conversion map of the unit to other untis of the same type.
     */
    final Map<Unit, Float> conversionMap = new HashMap<>();


    /******************************************************************
     * Constructors
     **********************************************************/

    /**
     * Initializes a new FluidUnit with the specified name.
     *
     * @param name The name of the unit
     */
    @Raw
    FluidUnit(String name) {
        this.name = name;
        this.conversionMap.put(this, 1F);
    }

    /**
     * Initializes a new FluidUnit with the specified name, and a conversion rate to another unit. (SPOON("spoon", 8L, DROP) means that 8 DROP is equal to 1 SPOON)
     *
     * @param name The name of the unit
     * @param amount The conversion rate to the other unit
     * @param unit The other unit to convert to
     */
    @Raw
    FluidUnit(String name, Float amount, FluidUnit unit) {
        this(name);
        this.conversionMap.put(unit, amount);
    }

    static {
        Unit.calculateConversionMaps(values());
    }


    /******************************************************************
     * Methods
     **********************************************************/

    /**
     * Returns the base unit of FluidUnit.
     *
     * @return The base unit
     */
    @Override @Basic
    public FluidUnit getBaseUnit() {
        return values()[0];
    }

    /**
     * Returns the name of the unit.
     *
     * @return The name of the unit
     */
    @Override @Basic
    public String getName() {
        return name;
    }

    /**
     * Returns the conversion map of the unit.
     *
     * @return The conversion map of the unit
     */
    @Override @Basic
    public Map<Unit, Float> getConversionMap() {
        return Map.copyOf(conversionMap);
    }

    /**
     * Adds a conversion rate to the conversion map.
     *
     * @param unit The unit to convert to
     * @param amount The conversion rate
     */
    @Override @Raw
    public void addConversionRate(Unit unit, Float amount) {
        conversionMap.put(unit, amount);
    }
}
