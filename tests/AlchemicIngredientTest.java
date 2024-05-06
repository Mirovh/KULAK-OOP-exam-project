import com.alchemy.AlchemicIngredient;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class AlchemicIngredientTest {

    AlchemicIngredient ingredient;

    @Before
    public void setUpFixture() {
        try {
            ingredient = new AlchemicIngredient("Test Ingredient", 10);
        } catch (AlchemicIngredient.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor() {
        assertThrows(AlchemicIngredient.IllegalNameException.class, () -> new AlchemicIngredient("Invalid Name1", 10));
        assertThrows(AlchemicIngredient.IllegalNameException.class, () -> new AlchemicIngredient("", 10));
        try {
            new AlchemicIngredient("Valid Name", 10);
        } catch (AlchemicIngredient.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetName() {
        assertThrows(AlchemicIngredient.IllegalNameException.class, () -> ingredient.setName("Invalid Name1"));
        assertThrows(AlchemicIngredient.IllegalNameException.class, () -> ingredient.setName(""));
        assertThrows(AlchemicIngredient.IllegalNameException.class, () -> ingredient.setName("Ba"));
        assertThrows(AlchemicIngredient.IllegalNameException.class, () -> ingredient.setName("abC"));
        assertThrows(AlchemicIngredient.IllegalNameException.class, () -> ingredient.setName("ABc mixed with abC"));
        assertThrows(AlchemicIngredient.IllegalNameException.class, () -> ingredient.setName("FULL CAPS"));
        assertThrows(AlchemicIngredient.IllegalNameException.class, () -> ingredient.setName("Not \"Valid\" Name"));
        try{
            ingredient.setName("Valid \'name");
        } catch (AlchemicIngredient.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try{
            ingredient.setName("Valid (name)");
        } catch (AlchemicIngredient.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient.setName("Valid Name mixed with Another Valid Name");
        } catch (AlchemicIngredient.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient.setName("Ab Cd mixed with Ef");
        } catch (AlchemicIngredient.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient.setName("Abc");
        } catch (AlchemicIngredient.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetBasicName() {
        try {
            AlchemicIngredient ingredient2 = new AlchemicIngredient("Test Ingredient", 10);
            assertEquals("Test Ingredient", ingredient2.getBasicName());
        } catch (AlchemicIngredient.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSpecialName() {
        AlchemicIngredient ingredient2 = new AlchemicIngredient("Test Ingredient", 10);
        assertNull(ingredient2.getSpecialName());
        assertThrows(AlchemicIngredient.IllegalSpecialNameException.class, () -> ingredient2.setSpecialName("Invalid Mixture"));
        assertThrows(AlchemicIngredient.IllegalSpecialNameException.class, () -> ingredient2.setSpecialName("invalid name"));
        assertThrows(AlchemicIngredient.IllegalSpecialNameException.class, () -> ingredient2.setSpecialName(""));
        assertThrows(AlchemicIngredient.IllegalSpecialNameException.class, () -> ingredient2.setSpecialName("Ba"));
        assertThrows(AlchemicIngredient.IllegalSpecialNameException.class, () -> ingredient2.setSpecialName("FULL CAPS"));
        assertThrows(AlchemicIngredient.IllegalSpecialNameException.class, () -> ingredient2.setSpecialName("Not \"Valid\" Name"));
        AlchemicIngredient ingredient3 = new AlchemicIngredient("Test Ingredient mixed with Another Ingredient", 10);
        try {
            ingredient3.setSpecialName("Valid Name");
            assertEquals("Valid Name", ingredient3.getSpecialName());
        } catch (Exception e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient3.setSpecialName("Valid \'name");
            assertEquals("Valid \\'name", ingredient3.getSpecialName());
        } catch (Exception e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient3.setSpecialName("Valid (name)");
            assertEquals("Valid (name)", ingredient3.getSpecialName());
        } catch (Exception e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetFullName() {
        AlchemicIngredient ingredient2 = new AlchemicIngredient("Test Ingredient", 10);
        AlchemicIngredient ingredient3 = new AlchemicIngredient("Test Ingredient mixed with Another Ingredient", 10);
        assertEquals("Test Ingredient", ingredient2.getFullName());
        ingredient3.setSpecialName("Special Name");
        assertEquals("Special Name (Test Ingredient mixed with Another Ingredient)", ingredient3.getFullName());
    }
}