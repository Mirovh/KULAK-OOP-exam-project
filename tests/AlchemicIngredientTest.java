import com.alchemy.AlchemicIngredient;
import com.alchemy.IngredientConditions.Device;
import com.alchemy.IngredientConditions.Kettle;
import com.alchemy.IngredientContainer;
import com.alchemy.IngredientName;

import com.alchemy.IngredientConditions.Temperature;
import com.alchemy.IngredientConditions.IngredientState;
import com.alchemy.Laboratory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class AlchemicIngredientTest {

    AlchemicIngredient ingredient;
    AlchemicIngredient ingredient2;

    Temperature temp = new Temperature(0,20);

    IngredientState state = new IngredientState(true);
    
    @Before
    public void setUpFixture() {
        try {
            ingredient = new AlchemicIngredient("Test Ingredient",temp,state,10);
            ingredient2 = new AlchemicIngredient("Test Ingredient",temp,state, 10);
            AlchemicIngredient ingredient3 = new AlchemicIngredient("Another Ingredient",temp,state, 10);
            // mix 2 and 3
            Laboratory lab = new Laboratory(1);
            IngredientContainer container1 = new IngredientContainer(ingredient2, ingredient2.getQuantity().getSmallestPowderContainer());
            IngredientContainer container2 = new IngredientContainer(ingredient3, ingredient3.getQuantity().getSmallestPowderContainer());
            Kettle kettle = new Kettle();
            lab.addDevice(kettle);
            kettle.addIngredient(container1);
            kettle.addIngredient(container2);
            kettle.react();
            ingredient2 = kettle.getContents().getContent();
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        } catch (Device.NotInLaboratoryException | Laboratory.LaboratoryFullException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConstructor() {
        assertThrows(IngredientName.IllegalNameException.class, () -> new AlchemicIngredient("Invalid Name1",temp,state,10));
        assertThrows(IngredientName.IllegalNameException.class, () -> new AlchemicIngredient("",temp,state,10));
        try {
            new AlchemicIngredient("Valid Name",temp,state,10);
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetName() {
        assertThrows(IngredientName.IllegalNameException.class, () -> new AlchemicIngredient("Ba", temp,state,10));
        assertThrows(IngredientName.IllegalNameException.class, () -> new AlchemicIngredient("abC",temp,state,10));
        assertThrows(IngredientName.IllegalNameException.class, () -> new AlchemicIngredient("ABc mixed with abC", temp,state,10));
        assertThrows(IngredientName.IllegalNameException.class, () -> new AlchemicIngredient("FULL CAPS", temp,state,10));
        assertThrows(IngredientName.IllegalNameException.class, () -> new AlchemicIngredient("Not \"Valid\" Name", temp,state,10));
        try{
            new AlchemicIngredient("Valid 'name", temp,state,10);
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try{
            new AlchemicIngredient("Valid (name)", temp,state,10);
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        Assert.assertThrows(IngredientName.IllegalNameException.class,() ->new AlchemicIngredient("Valid Name mixed with Another Valid Name", temp,state,10));
        try {
            new AlchemicIngredient("Abc", temp, state,10);
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetBasicName() {
        try {
            AlchemicIngredient ingredient2 = new AlchemicIngredient("Test Ingredient", temp, state,10);
            assertEquals("Test Ingredient", ingredient2.getBasicName());
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSpecialName() {
        assertNull(ingredient.getSpecialName());
        assertThrows(IngredientName.IllegalSpecialNameException.class, () -> ingredient.setSpecialName("Invalid Mixture"));
        assertThrows(IngredientName.IllegalSpecialNameException.class, () -> ingredient.setSpecialName("invalid name"));
        assertThrows(IngredientName.IllegalSpecialNameException.class, () -> ingredient.setSpecialName(""));
        assertThrows(IngredientName.IllegalSpecialNameException.class, () -> ingredient.setSpecialName("Ba"));
        assertThrows(IngredientName.IllegalSpecialNameException.class, () -> ingredient.setSpecialName("FULL CAPS"));
        assertThrows(IngredientName.IllegalSpecialNameException.class, () -> ingredient.setSpecialName("Not \"Valid\" Name"));
        try {
            ingredient2.setSpecialName("Valid Name");
            assertEquals("Valid Name", ingredient2.getSpecialName());
        } catch (IngredientName.IllegalSpecialNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient2.setSpecialName("Valid 'name");
            assertEquals("Valid 'name", ingredient2.getSpecialName());
        } catch (IngredientName.IllegalSpecialNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient2.setSpecialName("Valid (name)");
            assertEquals("Valid (name)", ingredient2.getSpecialName());
        } catch (IngredientName.IllegalSpecialNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetFullName() {
        assertEquals("Test Ingredient", ingredient.getFullName());
        try {
            ingredient2.setSpecialName("Special Name");
            assertEquals("Special Name (Another Ingredient mixed with Test Ingredient)", ingredient2.getFullName());
        } catch (IngredientName.IllegalSpecialNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }
}