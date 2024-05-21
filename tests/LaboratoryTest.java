import com.alchemy.*;
import com.alchemy.IngredientConditions.*;
import com.alchemy.quantity.Quantity;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.alchemy.quantity.FluidUnit.*;
import static com.alchemy.quantity.PowderUnit.*;

public class LaboratoryTest {
    AlchemicIngredient ingredientSolid;
    AlchemicIngredient ingredientLiquid;
    AlchemicIngredient exceedingIngredient;
    IngredientContainer containerLiquid;
    IngredientContainer containerSolid;
    IngredientContainer emptyContainer;
    IngredientContainer exceedingAmount;
    Laboratory testLab;
    Temperature temp = new Temperature(0,20);
    IngredientState stateSolid = new IngredientState(true);
    IngredientState stateLiquid = new IngredientState(false);

    @Before
    public void setUpFixture(){
        testLab = new Laboratory(1);
        try {
            ingredientSolid = new AlchemicIngredient("Test Solid", temp, stateSolid, 50);
            ingredientLiquid = new AlchemicIngredient("Test Liquid",temp,stateLiquid, 50);
            exceedingIngredient = new AlchemicIngredient("Exceeding",temp, stateSolid, 7560);
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        containerLiquid = new IngredientContainer(ingredientLiquid, BOTTLE);
        containerSolid = new IngredientContainer(ingredientSolid, BOX);
        exceedingAmount = new IngredientContainer(exceedingIngredient, CHEST);
        emptyContainer = new IngredientContainer(BOTTLE);
    }
    @Test
    public void testLaboratory(){
        assertEquals(1, testLab.getStoreroom());
        assertTrue(testLab.isEmpty());
    }

    @Test
    public void testAddContainer() throws IngredientName.IllegalNameException {
        assertThrows(IllegalArgumentException.class, () -> testLab.addContainer(emptyContainer));
        testLab.addContainer(containerLiquid, 40);
        assertTrue(testLab.getContainers().getFirst().getContent().getQuantity().isEqualTo(new Quantity(40, DROP)));
        testLab.addContainer(containerSolid);       // no second argument adds all contents of container
        assertEquals(testLab.getContainers().get(1), containerSolid);
    }

    @Test
    public void testRemoveContainer() throws IngredientName.IllegalNameException {
        testLab.addContainer(containerLiquid);
        testLab.addContainer(containerSolid);
        assertThrows(IllegalArgumentException.class, () -> testLab.removeIngredient("Test Liquid", BOTTLE, 0));
        testLab.removeIngredient("Test Liquid", DROP, 20);
        assertFalse(testLab.getLabContainers().isEmpty());
        testLab.removeIngredient("Test Liquid", DROP, 30);
        IngredientContainer removedIngredient = testLab.removeIngredient("Test Solid", PINCH, 50);
        assert(removedIngredient.getContent().getQuantity().isEqualTo(new Quantity(50, PINCH)));
        assertTrue(testLab.isEmpty());
        assertThrows(IllegalArgumentException.class, () -> testLab.removeIngredient("Test Liquid", BOTTLE, 50));      //exceeds amount of ingredient
        assertThrows(IllegalArgumentException.class, () -> testLab.removeIngredient("Test Liquid", BOTTLE, -50));
    }

    @Test
    public void testAddStoreroom() {
        testLab.addStorerooms(0);
        assertEquals(1, testLab.getStoreroom());
        testLab.addStorerooms(2);
        assertEquals(3, testLab.getStoreroom());
        assertThrows(IllegalArgumentException.class, () -> testLab.addStorerooms(-5));
    }

    @Test
    public void testRemoveStoreroom() {
        assertThrows(IllegalArgumentException.class, () -> testLab.removeStorerooms(-5));
        testLab.addStorerooms(3);
        assertThrows(IllegalArgumentException.class, () -> testLab.removeStorerooms(0));
        assertEquals(4, testLab.getStoreroom());
        testLab.removeStorerooms(1);
        assertEquals(3, testLab.getStoreroom());
        testLab.addContainer(containerLiquid);
        IngredientContainer exceeding1;
        IngredientContainer exceeding2;
        IngredientContainer exceeding3;
        IngredientContainer exceeding4;
        IngredientContainer exceeding5;
        IngredientContainer exceeding6;
        try {
            exceeding1 = new IngredientContainer(new AlchemicIngredient("Exceeding", temp, stateSolid, 7560), CHEST);
            exceeding2 = new IngredientContainer(new AlchemicIngredient("Exceeding", temp, stateSolid, 7560), CHEST);
            exceeding3 = new IngredientContainer(new AlchemicIngredient("Exceeding", temp, stateSolid, 7560), CHEST);
            exceeding4 = new IngredientContainer(new AlchemicIngredient("Exceeding", temp, stateSolid, 7560), CHEST);
            exceeding5 = new IngredientContainer(new AlchemicIngredient("Exceeding", temp, stateSolid, 7560), CHEST);
            exceeding6 = new IngredientContainer(new AlchemicIngredient("Exceeding", temp, stateSolid, 7560), CHEST);
        } catch (IngredientName.IllegalNameException e) {
            throw new RuntimeException(e);
        }
        testLab.addContainer(exceeding1);
        testLab.addContainer(exceeding2);
        testLab.addContainer(exceeding3);
        testLab.addContainer(exceeding4);
        testLab.addContainer(exceeding5);
        testLab.addContainer(exceeding6);
        assertThrows(IllegalArgumentException.class, () -> testLab.removeStorerooms(2));
        testLab.removeStorerooms(1);
        assertEquals(2, testLab.getStoreroom());
    }

    @Test
    public void testGetContents() {
        assertEquals("The lab is empty", testLab.getContents());        //empty lab gives special string
        testLab.addContainer(containerLiquid);
        testLab.addContainer(containerSolid);
        assertEquals("The lab contains: 50.0 drop of Test Liquid, 50.0 pinch of Test Solid", testLab.getContents());    //different String might be better
        assertEquals("The lab contains: 50.0 drop of Test Liquid", testLab.getContents(ingredientLiquid));
    }

    @Test
    public void LaboratoryDeviceTest() throws Laboratory.LaboratoryFullException {
        CoolingBox fridge = new CoolingBox();
        CoolingBox fridge2 = new CoolingBox();
        //test that only one device of each type can be added to the Laboratory
        testLab.addDevice(fridge);
        assertThrows(Laboratory.LaboratoryFullException.class,()-> testLab.addDevice(fridge2));
        assertThrows(Device.NotInLaboratoryException.class, fridge2::react);

    }
}

