package com.alchemy.quantity;

public enum PowderUnit implements Unit {
    /**
     * The base unit for powder measurements.
     * @note Always initialize the base unit first.
     * @note DO NOT initialize a unit with a comparison to itself or a unit that has not been initialized yet.
     */
    PINCH("pinch"),
    SPOON("spoon", 6L, PINCH),
    SACHET("sachet", 7L, SPOON),
    BOX("box", 6L, SACHET),
    SACK("sack", 3L, BOX),
    CHEST("chest", 10L, SACK),
    STOREROOM("storeroom", 5L, CHEST);

    private final String name;

    PowderUnit(String name) {
        this.name = name;
        this.conversionMap.put(this, 1L);
    }

    PowderUnit(String name, Long amount, PowderUnit unit) {
        this(name);
        this.conversionMap.put(unit, amount);
    }

    static {
        Unit.calculateConversionMaps(values());
    }

    @Override
    public Unit getBaseUnit() {
        return values()[0];
    }

    @Override
    public String getName() {
        return name;
    }
}
