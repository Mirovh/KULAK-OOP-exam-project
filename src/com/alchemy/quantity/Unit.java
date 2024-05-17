package com.alchemy.quantity;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public interface Unit {
    /**
     * The conversion map for this unit.
     * This map contains the conversion rates to all other units.
     */
    Map<Unit, Long> conversionMap = new HashMap<>();

    /**
     * Converts the specified amount to the specified unit.
     *
     * @param unit The unit to convert to
     * @param amount The amount to convert
     * @return The converted amount
     */
    @Basic
    default Long convertTo(Unit unit, Long amount) {
        return amount * conversionMap.get(unit);
    }

    /**
     * Converts the specified amount to the base unit.
     *
     * @param amount The amount to convert
     * @return The converted amount
     */
    @Basic
    default Long convertToBase(Long amount) {
        return convertTo(getBaseUnit(), amount);
    }

    /**
     * Calculates the conversion maps for the specified units.
     *
     * @param units The units to calculate the conversion maps for
     */
    @Raw
    static void calculateConversionMaps(Unit... units) {
        Unit baseUnit = units[0];
        units = Arrays.copyOfRange(units, 1, units.length);
        boolean finished = false;
        // Keep looping until we have converted all units
        while (!finished) {
            finished = true;
            // for every element in our conversion map, search all other elements which contain this element in their conversion map
            for (Map.Entry<Unit, Long> entry : baseUnit.conversionMap.entrySet()) {
                for (Unit otherUnit : units) {
                    if ((!baseUnit.conversionMap.containsKey(otherUnit)) && otherUnit.conversionMap.containsKey(entry.getKey())) {
                        finished = false;
                        // Add the conversion to PINCH's map
                        baseUnit.conversionMap.put(otherUnit, entry.getValue() / otherUnit.conversionMap.get(entry.getKey()));
                    }
                }
            }
        }
        // Update the conversion map for all other units
        for (Unit unit : units) {
            if (unit != baseUnit) {
                for (Map.Entry<Unit, Long> entry : baseUnit.conversionMap.entrySet()) {
                    unit.conversionMap.put(entry.getKey(), entry.getValue() / baseUnit.conversionMap.get(unit));
                }
            }
        }
    }

    /**
     * Returns the base unit of this unit.
     *
     * @return The base unit
     */
    @Basic
    Unit getBaseUnit();

    /**
     * Returns the name of this unit.
     *
     * @return The name of this unit
     */
    @Basic
    String getName();
}
