import com.alchemy.AlchemicIngredient;
import com.alchemy.IngredientName;

import com.alchemy.IngredientConditions.Temperature;
import com.alchemy.IngredientConditions.IngredientState;
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
            ingredient2 = new AlchemicIngredient("Test Ingredient mixed with Another Ingredient",temp,state, 10);
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
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
        assertThrows(IngredientName.IllegalNameException.class, () -> ingredient.setName("Invalid Name1"));
        assertThrows(IngredientName.IllegalNameException.class, () -> ingredient.setName(""));
        assertThrows(IngredientName.IllegalNameException.class, () -> ingredient.setName("Ba"));
        assertThrows(IngredientName.IllegalNameException.class, () -> ingredient.setName("abC"));
        assertThrows(IngredientName.IllegalNameException.class, () -> ingredient.setName("ABc mixed with abC"));
        assertThrows(IngredientName.IllegalNameException.class, () -> ingredient.setName("FULL CAPS"));
        assertThrows(IngredientName.IllegalNameException.class, () -> ingredient.setName("Not \"Valid\" Name"));
        try{
            ingredient.setName("Valid \'name");
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try{
            ingredient.setName("Valid (name)");
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient.setName("Valid Name mixed with Another Valid Name");
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient.setName("Ab Cd mixed with Ef");
        } catch (IngredientName.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient.setName("Abc");
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
            assertEquals("Special Name (Test Ingredient mixed with Another Ingredient)", ingredient2.getFullName());
        } catch (IngredientName.IllegalSpecialNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }
}