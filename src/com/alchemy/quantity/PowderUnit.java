package com.alchemy.quantity;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public enum PowderUnit implements Unit {
    /**
     * The base unit for powder measurements.
     * @note DO NOT initialize a unit with a comparison to itself or a unit that has not been initialized yet.
     * @note Always initialize the base unit first and for all other units, initialize them in order of increasing size.
     * @note If you want to modify the base unit, also modify the reference to the base unit in the static block.
     */
    PINCH("pinch", 1L, null),
    SPOON("spoon", 6L, PINCH),
    SACHET("sachet", 7L, SPOON),
    BOX("box", 6L, SACHET),
    SACK("sack", 3L, BOX),
    CHEST("chest", 10L, SACK),
    STOREROOM("storeroom", 5L, CHEST);

    private final String name;
    private EnumMap<PowderUnit, Long> conversionMap;

    PowderUnit(String name, Long amount, PowderUnit unit) {
        this.name = name;
        this.conversionMap = new EnumMap<>(PowderUnit.class);
        if (unit != null) {
            conversionMap.put(unit, amount);
        }
        conversionMap.put(this, 1L);
    }

    static {
        final PowderUnit baseUnit = PINCH;
        boolean finished = false;
        while (!finished) {
            finished = true;
            // for every element in our conversion map, search all other elements which contain this element in their conversion map
            for (Map.Entry<PowderUnit, Long> entry : baseUnit.conversionMap.entrySet()) {
                for (PowderUnit otherUnit : values()) {
                    if (otherUnit.conversionMap.containsKey(entry.getKey())) {
                        finished = false;
                        // Add the conversion to PINCH's map (eg. SPOON -> PINCH = 6, PINCH -> SPOON = 1/6, but also SPOON -> CHEST = 6 * 6 * 7 * 3 * 6 * 10, then we have to convert using the conversion present in PINCH's map, which is 1/6)
                        baseUnit.conversionMap.put(otherUnit, otherUnit.conversionMap.get(entry.getKey()) / entry.getValue());
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
