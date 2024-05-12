import com.alchemy.*;
import com.alchemy.Temperature.Temperature;
import com.alchemy.transmorgrify.IngredientState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LaboratoryTest {
    AlchemicIngredient ingredientSolid;
    AlchemicIngredient ingredientLiquid;
    IngredientContainer containerLiquid;
    IngredientContainer containerSolid;
    IngredientContainer emptyContainer;
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
        } catch (Name.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        containerLiquid = new IngredientContainer("bottle", ingredientLiquid, 50);   //TODO:containerconstructer moet nog geimplement worden
        containerSolid = new IngredientContainer("box", ingredientSolid, 50);
        emptyContainer = new IngredientContainer("bottle");
    }
    @Test
    public void testLaboratory(){
        assertEquals(1, testLab.getStorerooms);
        assertTrue(testLab.getIngredients().isEmpty());  //new lab is empty
    }
    @Test
    public void testAddContainer(){
        IngredientContainer exceedingAmount;
        exceedingAmount = new IngredientContainer("storeroom",ingredientSolid, 37800)       //full storeroom
        testLab.addContainer(emptyContainer);
        assertEquals(0, testLab.getContentsAmount);
        assertThrows(IllegalArgumentException.class, () -> testLab.addContainer(containerLiquid, 100));    //exceeds amount of ingredient
        assertThrows(IllegalArgumentException.class, () -> testLab.addContainer(containerLiquid, -50));
        assertThrows(IllegalArgumentException.class, () -> testLab.addContainer(exceedingAmount, 37800));
        testLab.addContainer(containerLiquid, 40);
        assertEquals(40, testLab.getContentsAmount);    //TODO: op 50 moet een conversie gebeuren om assertie te doen kloppen
        assertEquals(10, containerLiquid.getContents());
        testLab.addContainer(containerLiquid, 10);
        testLab.addContainer(containerSolid);       // no second argument adds all contents of container
        assertEquals(50 + 50, testLab.getContentsAmount);       //TODO: op 50 + 50 moet een conversie gebeuren om assertie te doen kloppen
        assertNull(containerLiquid);    //container should be destroyed //TODO: destructor for container
        assertNull(containerSolid);    //container should be destroyed
    }
    @Test
    public void testRemoveContainer(){
        IngredientContainer containerLabLiquid;
        IngredientContainer containerLabSolid;
        testLab.addContainer(containerLiquid);
        testLab.addContainer(containerSolid);
        assertThrows(IllegalArgumentException.class, () -> testLab.removeIngredient(ingredientLiquid, 50));      //exceeds amount of ingredient
        assertThrows(IllegalArgumentException.class, () -> testLab.removeIngredient(ingredientLiquid, -50));
        testLab.removeIngredient(ingredientLiquid, containerLabLiquid, "bottle", 0);
        assertNull(containerLabLiquid);     //no new container when nothing gets removed
        testLab.removeIngredient(ingredientLiquid, containerLabLiquid, "bottle", 50);   //remove creates a new container
        assertEquals(50, testLab.getContentsAmount);    //TODO: op 50 moet een conversie gebeuren om assertie te doen kloppen
        testLab.removeIngredient(ingredientLiquid, containerLabLiquid, "bottle", 50);   //remove creates a new container
        assertEquals(0, testLab.getContentsAmount);
        assertThrows(IllegalArgumentException.class, () -> testLab.removeIngredient(ingredientLiquid, 50));      //exceeds amount of ingredient
        assertThrows(IllegalArgumentException.class, () -> testLab.removeIngredient(ingredientLiquid, -50));
    }
    @Test
    public void testAddStoreroom() {
        testLab.addStoreroom(0);
        assertEquals(1, testLab.getStoreroom);
        testLab.addStoreroom(2);
        assertEquals(3, testLab.getStoreroom);
    }
    @Test
    public void testGetContents() {
        assertEquals("The lab is empty", testLab.getContents());        //empty lab gives special string
        testLab.addContainer(containerLiquid);
        testLab.addContainer(containerSolid);
        assertEquals("The lab contains: 50 spoons of testLiquid and 50 spoons of testSolid", testLab.getContents());    //different String might be better
        assertEquals("The lab contains: 50 spoons of testLiquid", testLab.getContents(ingredientLiquid));
    }
}

