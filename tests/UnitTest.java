import com.alchemy.quantity.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.alchemy.quantity.FluidUnit.*;
import static com.alchemy.quantity.PowderUnit.*;


public class UnitTest {
    Quantity smallTestFluid;
    Quantity smallTestPowder;
    Quantity mediumTestFluid;
    Quantity mediumTestPowder;
    Quantity largeTestFluid;
    Quantity largeTestPowder;
    @Before
    public void setUpFixture(){
        smallTestFluid = new Quantity(840, DROP);
        smallTestPowder = new Quantity(756, PINCH);
        mediumTestFluid = new Quantity(1, JUG);
        mediumTestPowder = new Quantity(1, SACK);
        largeTestFluid = new Quantity(1, BARREL);
        largeTestPowder = new Quantity(1, CHEST);
    }
    @Test
    public void testFluidCreation(){
        assertTrue(smallTestFluid.isEqualTo(mediumTestFluid));
        assertEquals(DROP, smallTestFluid.getUnit());               //TODO: niet zeker hoe ik deze test moet implementeren
        assertTrue(smallTestFluid.isFluidUnit());
        assertFalse(mediumTestFluid.isPowderUnit());
    }
    @Test
    public void testPowderCreation(){
        assertTrue(smallTestPowder.isEqualTo(mediumTestPowder));
        assertEquals(DROP, smallTestPowder.getUnit());          //TODO: boven
        assertTrue(smallTestPowder.isPowderUnit());
        assertFalse(mediumTestPowder.isFluidUnit());
    }
    @Test
    public void testComparison(){
        assertTrue(largeTestFluid.isGreaterThan(smallTestFluid));
        assertTrue(largeTestPowder.isGreaterThan(smallTestPowder));
        assertTrue(smallTestFluid.isGreaterThanOrEqualTo(mediumTestFluid));
        assertTrue(smallTestPowder.isGreaterThanOrEqualTo(mediumTestPowder));
        assertFalse(mediumTestPowder.isSmallerThan(largeTestPowder));
        assertFalse(mediumTestFluid.isSmallerThan(largeTestFluid));
        assertTrue(mediumTestFluid.isSmallerThanOrEqualTo(smallTestFluid));
        assertTrue(mediumTestPowder.isSmallerThanOrEqualTo(smallTestPowder));
    }

    //TODO: conversionTests
}
