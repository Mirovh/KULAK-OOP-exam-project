package com.alchemy.quantity;

public enum FluidUnit implements Unit {
    /**
     * The base unit for powder measurements.
     * @note Always initialize the base unit first.
     * @note DO NOT initialize a unit with a comparison to itself or a unit that has not been initialized yet.
     */
    DROP("drop"),
    SPOON("spoon", 8L, DROP),
    VIAL("sachet", 5L, SPOON),
    BOTTLE("box", 3L, VIAL),
    JUG("sack", 7L, BOTTLE),
    BARREL("chest", 12L, JUG),
    STOREROOM("storeroom", 5L, BARREL);

    private final String name;

    FluidUnit(String name) {
        this.name = name;
        this.conversionMap.put(this, 1L);
    }

    FluidUnit(String name, Long amount, FluidUnit unit) {
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
