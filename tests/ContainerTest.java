import com.alchemy.*;
import com.alchemy.IngredientConditions.*;
import com.alchemy.quantity.FluidUnit;
import com.alchemy.quantity.PowderUnit;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.alchemy.quantity.FluidUnit.*;
import static com.alchemy.quantity.PowderUnit.*;

public class ContainerTest {
    AlchemicIngredient wrongState;
    AlchemicIngredient ingredientPowder;
    AlchemicIngredient ingredientFluid;
    AlchemicIngredient exceedingIngredient;
    IngredientContainer containerFluid;
    IngredientContainer containerPowder;
    IngredientContainer emptyContainer;
    Temperature temp = new Temperature(0,20);
    IngredientState statePowder = new IngredientState(true);
    IngredientState stateFluid = new IngredientState(false);

    @Before
    public void setUpFixture(){
        try {
            ingredientFluid = new AlchemicIngredient("Test Fluid", temp, stateFluid, 100);
            ingredientPowder = new AlchemicIngredient("Test Powder", temp, statePowder, 100);
            exceedingIngredient = new AlchemicIngredient("Test Exceeding", temp, stateFluid, 10000);
            wrongState = new AlchemicIngredient("Fake Fluid", temp, statePowder, 50);
        } catch (IngredientName.IllegalNameException e) {
            throw new RuntimeException(e);
        }
        containerFluid = new IngredientContainer(ingredientFluid, BOTTLE);
        containerPowder = new IngredientContainer(ingredientPowder, BOX);
        emptyContainer = new IngredientContainer(BARREL);
    }

    @Test
    public void testConstructor(){
        assertEquals(ingredientFluid, containerFluid.getContent());
        assertEquals(BOTTLE, containerFluid.getContainerUnit());
        assertEquals(ingredientPowder, containerPowder.getContent());
        assertEquals(BOX, containerPowder.getContainerUnit());
        assertThrows(IllegalArgumentException.class, () -> new IngredientContainer(exceedingIngredient, BOTTLE));    //can be implemented differently, not sure how we handle this situation
        assertNull(emptyContainer.getContent());
        assertThrows(IllegalArgumentException.class, () ->  new IngredientContainer(DROP));                              //not really containers
        assertThrows(IllegalArgumentException.class, () ->  new IngredientContainer(PINCH));
        assertThrows(IllegalArgumentException.class, () ->  new IngredientContainer(PowderUnit.STOREROOM));             //not sure if these are allowed as containers
        assertThrows(IllegalArgumentException.class, () ->  new IngredientContainer(FluidUnit.STOREROOM));
        assertThrows(IllegalArgumentException.class, () ->  new IngredientContainer(wrongState, BARREL));
    }

    @Test
    public void testSetContent() {
        assertThrows(IllegalArgumentException.class, () ->  emptyContainer.setContent(wrongState));
        emptyContainer.setContent(exceedingIngredient);
        assertEquals(exceedingIngredient, emptyContainer.getContent());
    }

    @Test
    public void testEmptyAndDestroy() {
        containerPowder.empty();
        assertNull(emptyContainer.getContent());
        containerFluid.destroy();
        containerPowder.destroy();
        emptyContainer.destroy();
        assertNull(containerPowder.getContent());
        assertNull(emptyContainer.getContent());
    }
}
