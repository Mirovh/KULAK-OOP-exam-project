package com.alchemy.quantity;

import be.kuleuven.cs.som.annotate.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the units for powder measurements.
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
public enum PowderUnit implements Unit {
    /**
     * The base unit for powder measurements.
     */
    PINCH("pinch"),
    SPOON("spoon", 6F, PINCH),
    SACHET("sachet", 7F, SPOON),
    BOX("box", 6F, SACHET),
    SACK("sack", 3F, BOX),
    CHEST("chest", 10F, SACK),
    STOREROOM("storeroom", 5F, CHEST);

    /**
     * The name of the unit in human-readable format.
     */
    private final String name;
    final Map<Unit, Float> conversionMap = new HashMap<>();


    /**
     * Initializes a new PowderUnit with the specified name.
     *
     * @param name The name of the unit
     */
    @Raw
    PowderUnit(String name) {
        this.name = name;
        this.conversionMap.put(this, 1F);
        System.out.println("PowderUnit constructor called for " + name);
    }

    /**
     * Initializes a new PowderUnit with the specified name, and a conversion rate to another unit. (SPOON("spoon", 6L, PINCH) means that 6 PINCH is equal to 1 SPOON)
     *
     * @param name The name of the unit
     * @param amount The conversion rate to the other unit
     * @param unit The other unit to convert to
     */
    @Raw
    PowderUnit(String name, Float amount, PowderUnit unit) {
        this(name);
        this.conversionMap.put(unit, amount);
    }

    static {
        Unit.calculateConversionMaps(values());
    }

    /**
     * Returns the base unit of PowderUnit.
     *
     * @return The base unit
     */
    @Override @Basic
    public PowderUnit getBaseUnit() {
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
        return conversionMap;
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
