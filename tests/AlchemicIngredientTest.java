import com.alchemy.AlchemicIngredient;
import com.alchemy.Name;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class AlchemicIngredientTest {

    AlchemicIngredient ingredient;
    AlchemicIngredient ingredient2;
    
    @Before
    public void setUpFixture() {
        try {
            ingredient = new AlchemicIngredient("Test Ingredient", 10);
            ingredient2 = new AlchemicIngredient("Test Ingredient mixed with Another Ingredient", 10);
        } catch (Name.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor() {
        assertThrows(Name.IllegalNameException.class, () -> new AlchemicIngredient("Invalid Name1", 10));
        assertThrows(Name.IllegalNameException.class, () -> new AlchemicIngredient("", 10));
        try {
            new AlchemicIngredient("Valid Name", 10);
        } catch (Name.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetName() {
        assertThrows(Name.IllegalNameException.class, () -> ingredient.setName("Invalid Name1"));
        assertThrows(Name.IllegalNameException.class, () -> ingredient.setName(""));
        assertThrows(Name.IllegalNameException.class, () -> ingredient.setName("Ba"));
        assertThrows(Name.IllegalNameException.class, () -> ingredient.setName("abC"));
        assertThrows(Name.IllegalNameException.class, () -> ingredient.setName("ABc mixed with abC"));
        assertThrows(Name.IllegalNameException.class, () -> ingredient.setName("FULL CAPS"));
        assertThrows(Name.IllegalNameException.class, () -> ingredient.setName("Not \"Valid\" Name"));
        try{
            ingredient.setName("Valid \'name");
        } catch (Name.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try{
            ingredient.setName("Valid (name)");
        } catch (Name.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient.setName("Valid Name mixed with Another Valid Name");
        } catch (Name.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient.setName("Ab Cd mixed with Ef");
        } catch (Name.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient.setName("Abc");
        } catch (Name.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetBasicName() {
        try {
            AlchemicIngredient ingredient2 = new AlchemicIngredient("Test Ingredient", 10);
            assertEquals("Test Ingredient", ingredient2.getBasicName());
        } catch (Name.IllegalNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSpecialName() {
        assertNull(ingredient.getSpecialName());
        assertThrows(Name.IllegalSpecialNameException.class, () -> ingredient.setSpecialName("Invalid Mixture"));
        assertThrows(Name.IllegalSpecialNameException.class, () -> ingredient.setSpecialName("invalid name"));
        assertThrows(Name.IllegalSpecialNameException.class, () -> ingredient.setSpecialName(""));
        assertThrows(Name.IllegalSpecialNameException.class, () -> ingredient.setSpecialName("Ba"));
        assertThrows(Name.IllegalSpecialNameException.class, () -> ingredient.setSpecialName("FULL CAPS"));
        assertThrows(Name.IllegalSpecialNameException.class, () -> ingredient.setSpecialName("Not \"Valid\" Name"));
        try {
            ingredient2.setSpecialName("Valid Name");
            assertEquals("Valid Name", ingredient2.getSpecialName());
        } catch (Name.IllegalSpecialNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient2.setSpecialName("Valid \'name");
            assertEquals("Valid \\'name", ingredient2.getSpecialName());
        } catch (Name.IllegalSpecialNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
        try {
            ingredient2.setSpecialName("Valid (name)");
            assertEquals("Valid (name)", ingredient2.getSpecialName());
        } catch (Name.IllegalSpecialNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetFullName() {
        assertEquals("Test Ingredient", ingredient.getFullName());
        try {
            ingredient2.setSpecialName("Special Name");
            assertEquals("Special Name (Test Ingredient mixed with Another Ingredient)", ingredient2.getFullName());
        } catch (Name.IllegalSpecialNameException e) {
            fail("Valid name should not throw an exception: " + e.getMessage());
        }
    }
}