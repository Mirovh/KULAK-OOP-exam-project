package com.alchemy.quantity;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public interface Unit {
    Map<Unit, Long> conversionMap = new HashMap<>();

    default Long convertTo(Unit unit, Long amount) {
        return amount * conversionMap.get(unit);
    }

    default Long convertToBase(Long amount) {
        return convertTo(getBaseUnit(), amount);
    }

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

    Unit[] values();

    Unit getBaseUnit();

    String getName();
}
