package com.alchemy.quantity;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a unit for measurements.
 * Nominally programmed.
 *
 * @invar The conversion map always contains the unit itself.
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public interface Unit {

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * Converts the specified amount to the specified unit.
     *
     * @param unit The unit to convert to
     * @param amount The amount to convert
     * @return The converted amount
     */
    default Float convertTo(Unit unit, Float amount) {
        return amount * getConversionMap().get(unit);
    }

    /**
     * Converts the specified amount to the base unit.
     *
     * @param amount The amount to convert
     * @return The converted amount
     */
    default Float convertToBase(Float amount) {
        return convertTo(getBaseUnit(), amount);
    }

    /**
     * Returns the base unit of this unit.
     *
     * @return The base unit
     */
    Unit getBaseUnit();

    /**
     * Returns the name of this unit.
     *
     * @return The name of this unit
     */
    @Basic
    String getName();

    /**
     * Returns the conversion map of this unit.
     *
     * @return The conversion map of this unit
     */
    @Basic
    Map<Unit, Float> getConversionMap();

    /**
     * Adds a conversion rate to the conversion map.
     */
    @Raw
    void addConversionRate(Unit unit, Float amount);

    /**
     * Calculates the conversion maps for the specified units.
     *
     * @param units The units to calculate the conversion maps for
     * @effect The conversion maps for the specified units are calculated
     */
    @Raw
    static void calculateConversionMaps(Unit... units) {
        Unit baseUnit = units[0];
        units = Arrays.copyOfRange(units, 1, units.length);

        // use a separate map to fix issues with concurrent access during building of the conversion map
        Map<Unit, Float> baseConversionMap = new HashMap<>(baseUnit.getConversionMap());

        boolean finished = false;
        // Keep looping until we have converted all units
        Map<Unit, Float> tempMap = new HashMap<>();
        while (!finished) {
            finished = true;
            // for every element in our conversion map, search all other elements which contain this element in their conversion map
            for (Map.Entry<Unit, Float> entry : baseConversionMap.entrySet()) {
                for (Unit otherUnit : units) {
                    if ((!baseConversionMap.containsKey(otherUnit)) && otherUnit.getConversionMap().containsKey(entry.getKey())) {
                        finished = false;
                        // Add the conversion to the temporary map
                        tempMap.put(otherUnit, entry.getValue() / otherUnit.getConversionMap().get(entry.getKey()));
                    }
                }
            }
            // Add all entries from the temporary map to the baseConversionMap
            baseConversionMap.putAll(tempMap);
            // Clear the temporary map for the next iteration
            tempMap.clear();
        }

        // Update the conversion map for the base unit
        for (Map.Entry<Unit, Float> entry : baseConversionMap.entrySet()) {
            baseUnit.addConversionRate(entry.getKey(), entry.getValue());
        }

        // Update the conversion map for all other units
        for (Unit unit : units) {
            if (unit != baseUnit) {
                for (Map.Entry<Unit, Float> entry : baseUnit.getConversionMap().entrySet()) {
                    unit.addConversionRate(entry.getKey(), entry.getValue() / baseUnit.getConversionMap().get(unit));
                }
            }
        }
    }
}
