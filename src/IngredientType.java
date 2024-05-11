import com.alchemy.AlchemicIngredient;
import com.alchemy.Temperature;

/**
 * A class representing a type for Ingredient
 *
 * @author MiroVanHoef
 * @author BenDeMets
 * @author SimonVandeputte
 * @version 1.0
 */
public class IngredientType {
    /**********************************************************
     * Variables
     **********************************************************/
    final private Temperature standardTemperature;

    final private String name;
    final private AlchemicIngredient.IngredientState standardState;
    /**********************************************************
     * Constructors
     **********************************************************/
    public IngredientType(String name, Temperature standardTemperature, AlchemicIngredient.IngredientState standardState) {
        this.standardTemperature = standardTemperature;
        this.name = name;
        this.standardState = standardState;
    }

    public IngredientType() {
        name = "Water";
        standardTemperature = new Temperature(0L,20L);
        standardState = AlchemicIngredient.IngredientState.Liquid;
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/
    public Temperature getStandardTemperature() {
        return standardTemperature;
    }

    public String getName() {
        return name;
    }

    public AlchemicIngredient.IngredientState getStandardState() {
        return standardState;
    }
}
