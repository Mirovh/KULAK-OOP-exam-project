import com.alchemy.*;
import com.alchemy.Temperature.CoolingBox;
import com.alchemy.Temperature.Temperature;
import com.alchemy.quantity.PowderUnit;
import com.alchemy.transmorgrify.IngredientState;
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
        Laboratory testLab = new Laboratory(1); // amount of storerooms
        try {
            AlchemicIngredient ingredientSolid = new AlchemicIngredient("testSolid", temp, stateSolid, 50);
            AlchemicIngredient ingredientLiquid = new AlchemicIngredient("testLiquid",temp,stateLiquid, 50);
            exceedingIngredient = new AlchemicIngredient("exceeding",temp, stateSolid, 37800);
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        containerLiquid = new IngredientContainer(ingredientLiquid, BOTTLE);
        containerSolid = new IngredientContainer(ingredientSolid, BOX);
        exceedingAmount = new IngredientContainer(exceedingIngredient, PowderUnit.STOREROOM);
        emptyContainer = new IngredientContainer(BOTTLE);
    }
    @Test
    public void testLaboratory(){
        assertEquals(1, testLab.getStorerooms);
        assertTrue(testLab.getIngredients().isEmpty());  //new lab is empty
    }
    @Test
    public void testAddContainer(){
        testLab.addContainer(emptyContainer);
        assertEquals(0, testLab.getContentsAmount);
        assertThrows(IllegalArgumentException.class, () -> testLab.addContainer(exceedingAmount));
        testLab.addContainer(containerLiquid, 40);
        assertEquals(40, testLab.getContentsAmount);    //klopt nog niet hangt af van implementatie
        assertEquals(10, containerLiquid.getContents());
        testLab.addContainer(containerLiquid, 10);
        testLab.addContainer(containerSolid);       // no second argument adds all contents of container
        assertEquals(50 + 50, testLab.getContentsAmount);       //same as before, 46
        assertNull(containerLiquid);    //container should be destroyed //TODO: destructor for container
        assertNull(containerSolid);    //container should be destroyed
    }
    @Test
    public void testRemoveContainer(){
        IngredientContainer containerLabLiquid;
        IngredientContainer containerLabSolid;
        testLab.addContainer(containerLiquid);
        testLab.addContainer(containerSolid);
        testLab.removeIngredient(ingredientLiquid, BOTTLE, 0);
        assertNull(containerLabLiquid);     //no new container when nothing gets removed
        testLab.removeIngredient(ingredientLiquid, BOTTLE, 20);   //remove creates a new container
        assertEquals(80, testLab.getContentsAmount);    //TODO: op 80 moet een conversie gebeuren om assertie te doen kloppen
        assertEquals(20, containerLabLiquid.getContent());
        testLab.removeIngredient(ingredientLiquid, BOTTLE, 30);
        testLab.removeIngredient(ingredientLiquid, BOTTLE, 50);   //remove creates a new container
        assertTrue(testLab.isEmpty());
        assertThrows(IllegalArgumentException.class, () -> testLab.removeIngredient(ingredientLiquid, 50));      //exceeds amount of ingredient
        assertThrows(IllegalArgumentException.class, () -> testLab.removeIngredient(ingredientLiquid, -50));
    }
    @Test
    public void testAddStoreroom() {
        testLab.addStoreroom(0);
        assertEquals(1, testLab.getStoreroom);
        testLab.addStoreroom(2);
        assertEquals(3, testLab.getStoreroom);
        assertThrows(IllegalArgumentException.class, () -> testLab.addStoreroom(-5));
    }
    @Test
    public void testRemoveStoreroom() {
        assertThrows(IllegalArgumentException.class, () -> testLab.removeStoreroom(-5));
        testLab.addStoreroom(3);
        testLab.removeStoreroom(0);
        assertEquals(4, testLab.getStoreroom());
        testLab.removeStoreroom(1);
        assertEquals(3, testLab.getStoreroom());
        testLab.addContainer(exceedingAmount);
        testLab.addContainer(containerLiquid);
        assertThrows(IllegalArgumentException.class, () -> testLab.removeStoreroom(2));
        testLab.removeStoreroom(1);
        assertEquals(2, testLab.getStoreroom);
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
        Assert.assertThrows(Laboratory.LaboratoryFullException.class,()->{testLab.addDevice(fridge2);});
        Assert.assertThrows(Device.NotInLaboratoryException.class, fridge2::react);

    }
}

