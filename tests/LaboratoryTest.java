import com.alchemy.*;
import com.alchemy.IngredientConditions.*;
import com.alchemy.quantity.PowderUnit;
import org.junit.Assert;
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
            exceedingIngredient = new AlchemicIngredient("Exceeding",temp, stateSolid, 10800);
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
        assertThrows(IllegalArgumentException.class, () -> testLab.addContainer(exceedingAmount));
        testLab.addContainer(containerLiquid, 40);
        testLab.addContainer(containerSolid);       // no second argument adds all contents of container
        assertNull(containerLiquid);    //container should be destroyed
        assertNull(containerSolid);    //container should be destroyed
    }

    @Test
    public void testRemoveContainer() throws IngredientName.IllegalNameException {
        IngredientContainer containerLabLiquid = null;
        testLab.addContainer(containerLiquid);
        testLab.addContainer(containerSolid);
        testLab.removeIngredient(ingredientLiquid, BOTTLE, 0L);
        assertNull(testLab.getLabContainers());     //no new container when nothing gets removed
        testLab.removeIngredient(ingredientLiquid, BOTTLE, 20L);   //remove creates a new container
        assertNotNull(testLab.getLabContainers());
        testLab.removeIngredient(ingredientLiquid, BOTTLE, 30L);
        testLab.removeIngredient(ingredientLiquid, BOTTLE, 50L);   //remove creates a new container
        assertTrue(testLab.isEmpty());
        assertThrows(IllegalArgumentException.class, () -> testLab.removeIngredient(ingredientLiquid, BOTTLE, 50L));      //exceeds amount of ingredient
        assertThrows(IllegalArgumentException.class, () -> testLab.removeIngredient(ingredientLiquid, BOTTLE, -50L));
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
        assertThrows(IllegalArgumentException.class, () -> testLab.removeStorerooms(2));
        testLab.removeStorerooms(1);
        assertEquals(2, testLab.getStoreroom());
    }

    @Test
    public void testGetContents() {
        assertEquals("The lab is empty", testLab.getContents());        //empty lab gives special string
        testLab.addContainer(containerLiquid);
        testLab.addContainer(containerSolid);
        assertEquals("The lab contains: 50 spoons of testLiquid, 50 spoons of testSolid", testLab.getContents());    //different String might be better
        assertEquals("The lab contains: 50 spoons of testLiquid", testLab.getContents(ingredientLiquid));
    }

    @Test
    public void LaboratoryDeviceTest() throws Laboratory.LaboratoryFullException {
        CoolingBox fridge = new CoolingBox();
        CoolingBox fridge2 = new CoolingBox();
        //test that only one device of each type can be added to the Laboratory
        testLab.addDevice(fridge);
        assertThrows(Laboratory.LaboratoryFullException.class,()->{testLab.addDevice(fridge2);});
        assertThrows(Device.NotInLaboratoryException.class, fridge2::react);

    }
}

